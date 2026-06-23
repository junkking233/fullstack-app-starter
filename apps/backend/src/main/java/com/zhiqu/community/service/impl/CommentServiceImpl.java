package com.zhiqu.community.service.impl;

import com.zhiqu.community.dto.CommentVO;
import com.zhiqu.community.dto.CreateCommentRequest;
import com.zhiqu.community.entity.Answer;
import com.zhiqu.community.entity.Comment;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.AnswerMapper;
import com.zhiqu.community.mapper.CommentMapper;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.service.CommentService;
import com.zhiqu.community.service.SensitiveWordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final AnswerMapper answerMapper;
    private final UserMapper userMapper;
    private final SensitiveWordService sensitiveWordService;

    public CommentServiceImpl(CommentMapper commentMapper,
                              AnswerMapper answerMapper,
                              UserMapper userMapper,
                              SensitiveWordService sensitiveWordService) {
        this.commentMapper = commentMapper;
        this.answerMapper = answerMapper;
        this.userMapper = userMapper;
        this.sensitiveWordService = sensitiveWordService;
    }

    @Override
    @Transactional
    public Comment createComment(CreateCommentRequest request, Long answerId, Long userId) {
        Answer answer = answerMapper.selectById(answerId);
        if (answer == null || "deleted".equals(answer.getStatus())) {
            throw new BusinessException(404, "回答不存在");
        }
        if (!"published".equals(answer.getStatus())) {
            throw new BusinessException(400, "该回答不可评论");
        }

        if (sensitiveWordService.containsSensitiveWord(request.getContent())) {
            throw new BusinessException(4001, "内容包含不当用语，请修改");
        }

        // Validate 2-level nesting
        if (request.getParentId() != null) {
            Comment parentComment = commentMapper.selectById(request.getParentId());
            if (parentComment == null) {
                throw new BusinessException(404, "父评论不存在");
            }
            // Only allow replying to top-level comments (parent_id is null)
            if (parentComment.getParentId() != null) {
                throw new BusinessException(400, "不允许对回复再回复");
            }
            if (!parentComment.getAnswerId().equals(answerId)) {
                throw new BusinessException(400, "评论不属于该回答");
            }
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUserId(userId);
        comment.setAnswerId(answerId);
        comment.setParentId(request.getParentId());
        comment.setStatus("published");
        comment.setCreatedAt(LocalDateTime.now());

        commentMapper.insert(comment);

        // Update answer comment count
        answer.setCommentCount((answer.getCommentCount() == null ? 0 : answer.getCommentCount()) + 1);
        answerMapper.updateById(answer);

        return comment;
    }

    @Override
    public List<CommentVO> getCommentsByAnswerId(Long answerId) {
        List<Comment> allComments = commentMapper.selectByAnswerId(answerId);

        Map<Long, CommentVO> voMap = new HashMap<>();
        Map<Long, List<Comment>> repliesMap = new HashMap<>();
        List<Long> topLevelIds = new ArrayList<>();

        for (Comment comment : allComments) {
            if (comment.getParentId() == null) {
                topLevelIds.add(comment.getId());
            } else {
                repliesMap.computeIfAbsent(comment.getParentId(), k -> new ArrayList<>()).add(comment);
            }
        }

        for (Comment comment : allComments) {
            CommentVO vo = buildCommentVO(comment);
            voMap.put(comment.getId(), vo);
        }

        // Build tree
        List<CommentVO> result = new ArrayList<>();
        for (Long topId : topLevelIds) {
            CommentVO topVo = voMap.get(topId);
            if (topVo == null) {
                continue;
            }
            List<Comment> replies = repliesMap.getOrDefault(topId, new ArrayList<>());
            List<CommentVO> replyVos = new ArrayList<>();
            for (Comment reply : replies) {
                CommentVO replyVo = voMap.get(reply.getId());
                if (replyVo != null) {
                    replyVos.add(replyVo);
                }
            }
            topVo.setReplies(replyVos);
            result.add(topVo);
        }

        return result;
    }

    @Override
    @Transactional
    public void deleteComment(Long id, Long userId) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(404, "评论不存在");
        }

        User user = userMapper.selectById(userId);
        boolean isAdmin = user != null && "ADMIN".equals(user.getRole());

        if (!comment.getUserId().equals(userId) && !isAdmin) {
            throw new BusinessException(403, "无权删除此评论");
        }

        comment.setStatus("deleted");
        commentMapper.updateById(comment);

        // Decrement answer comment count
        Answer answer = answerMapper.selectById(comment.getAnswerId());
        if (answer != null && answer.getCommentCount() != null && answer.getCommentCount() > 0) {
            answer.setCommentCount(answer.getCommentCount() - 1);
            answerMapper.updateById(answer);
        }
    }

    private CommentVO buildCommentVO(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setContent(comment.getContent());
        vo.setUserId(comment.getUserId());
        vo.setAnswerId(comment.getAnswerId());
        vo.setParentId(comment.getParentId());
        vo.setStatus(comment.getStatus());
        vo.setCreatedAt(comment.getCreatedAt());

        // Author info
        if (comment.getUserId() != null) {
            User author = userMapper.selectById(comment.getUserId());
            if (author != null) {
                vo.setUsername(author.getUsername());
                vo.setNickname(author.getNickname());
            }
        }

        return vo;
    }
}
