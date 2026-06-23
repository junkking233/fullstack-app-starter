package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.dto.AnswerVO;
import com.zhiqu.community.dto.CreateAnswerRequest;
import com.zhiqu.community.entity.Answer;
import com.zhiqu.community.entity.Question;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.AnswerMapper;
import com.zhiqu.community.mapper.QuestionMapper;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.service.AnswerService;
import com.zhiqu.community.service.SensitiveWordService;
import com.zhiqu.community.service.UserAchievementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImpl implements AnswerService {

    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024;
    private static final int MAX_IMAGE_COUNT = 10;

    private final AnswerMapper answerMapper;
    private final QuestionMapper questionMapper;
    private final UserMapper userMapper;
    private final SensitiveWordService sensitiveWordService;
    private final UserAchievementService achievementService;

    public AnswerServiceImpl(AnswerMapper answerMapper,
                             QuestionMapper questionMapper,
                             UserMapper userMapper,
                             SensitiveWordService sensitiveWordService,
                             UserAchievementService achievementService) {
        this.answerMapper = answerMapper;
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
        this.sensitiveWordService = sensitiveWordService;
        this.achievementService = achievementService;
    }

    @Override
    @Transactional
    public Answer createAnswer(CreateAnswerRequest request, Long questionId, Long userId) {
        Question question = questionMapper.selectById(questionId);
        if (question == null || "deleted".equals(question.getStatus())) {
            throw new BusinessException(404, "问题不存在");
        }
        if (!"published".equals(question.getStatus())) {
            throw new BusinessException(400, "该问题不可回答");
        }

        if (sensitiveWordService.containsSensitiveWord(request.getContent())) {
            throw new BusinessException(4001, "内容包含不当用语，请修改");
        }

        validateBase64Images(request.getContent());

        Answer answer = new Answer();
        answer.setContent(request.getContent());
        answer.setQuestionId(questionId);
        answer.setUserId(userId);
        answer.setIsAccepted(0);
        answer.setVoteCount(0);
        answer.setCommentCount(0);
        answer.setStatus("pending");
        answer.setCreatedAt(LocalDateTime.now());
        answer.setUpdatedAt(LocalDateTime.now());

        answerMapper.insert(answer);

        question.setAnswerCount((question.getAnswerCount() == null ? 0 : question.getAnswerCount()) + 1);
        questionMapper.updateById(question);

        return answer;
    }

    @Override
    public IPage<AnswerVO> getAnswersByQuestionId(Long questionId, int page, int pageSize) {
        Page<Answer> answerPage = new Page<>(page, pageSize);
        IPage<Answer> result = answerMapper.selectByQuestionId(answerPage, questionId);

        IPage<AnswerVO> voPage = new Page<>(page, pageSize, result.getTotal());
        List<AnswerVO> vos = result.getRecords().stream()
                .map(this::buildAnswerVO)
                .collect(Collectors.toList());
        voPage.setRecords(vos);
        return voPage;
    }

    @Override
    @Transactional
    public void acceptAnswer(Long answerId, Long userId) {
        Answer answer = answerMapper.selectById(answerId);
        if (answer == null || "deleted".equals(answer.getStatus())) {
            throw new BusinessException(404, "回答不存在");
        }
        if (!"published".equals(answer.getStatus())) {
            throw new BusinessException(400, "只能采纳已发布的回答");
        }

        Question question = questionMapper.selectById(answer.getQuestionId());
        if (question == null) {
            throw new BusinessException(404, "问题不存在");
        }
        if (!question.getUserId().equals(userId)) {
            throw new BusinessException(403, "只有问题作者可以采纳回答");
        }

        List<Answer> acceptedList = answerMapper.selectList(
                new LambdaQueryWrapper<Answer>()
                        .eq(Answer::getQuestionId, answer.getQuestionId())
                        .eq(Answer::getIsAccepted, 1)
        );
        for (Answer accepted : acceptedList) {
            accepted.setIsAccepted(0);
            answerMapper.updateById(accepted);
        }

        answer.setIsAccepted(1);
        answerMapper.updateById(answer);

        // +10 exp for answer author, check achievements
        if (answer.getUserId() != null) {
            User author = userMapper.selectById(answer.getUserId());
            if (author != null) {
                author.setExp((author.getExp() != null ? author.getExp() : 0) + 10);
                author.setLevel(calculateLevel(author.getExp()));
                userMapper.updateById(author);
                achievementService.checkAndUnlock(author.getId(), "first_accepted");
            }
        }
    }

    @Override
    @Transactional
    public AnswerVO updateAnswer(Long id, String content, Long userId) {
        Answer answer = answerMapper.selectById(id);
        if (answer == null || "deleted".equals(answer.getStatus())) {
            throw new BusinessException(404, "内容不存在");
        }
        if (!answer.getUserId().equals(userId)) {
            throw new BusinessException(403, "只能编辑自己的回答");
        }

        if (sensitiveWordService.containsSensitiveWord(content)) {
            throw new BusinessException(4001, "内容包含不当用语，请修改");
        }

        validateBase64Images(content);

        answer.setContent(content);
        if ("rejected".equals(answer.getStatus())) {
            answer.setStatus("pending");
            answer.setAuditReason(null);
        }

        answer.setUpdatedAt(LocalDateTime.now());
        answerMapper.updateById(answer);

        return buildAnswerVO(answer);
    }

    @Override
    @Transactional
    public void deleteAnswer(Long id, Long userId) {
        Answer answer = answerMapper.selectById(id);
        if (answer == null || "deleted".equals(answer.getStatus())) {
            throw new BusinessException(404, "内容不存在");
        }

        User user = userMapper.selectById(userId);
        boolean isAdmin = user != null && "ADMIN".equals(user.getRole());

        if (!answer.getUserId().equals(userId) && !isAdmin) {
            throw new BusinessException(403, "无权删除此回答");
        }

        answer.setStatus("deleted");
        answer.setUpdatedAt(LocalDateTime.now());
        answerMapper.updateById(answer);

        Question question = questionMapper.selectById(answer.getQuestionId());
        if (question != null && question.getAnswerCount() != null && question.getAnswerCount() > 0) {
            question.setAnswerCount(question.getAnswerCount() - 1);
            questionMapper.updateById(question);
        }
    }

    @Override
    public IPage<Answer> getPendingList(int page, int pageSize) {
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getStatus, "pending").orderByAsc(Answer::getCreatedAt);
        return answerMapper.selectPage(new Page<>(page, pageSize), wrapper);
    }

    @Override
    @Transactional
    public void approveAnswer(Long id) {
        Answer answer = answerMapper.selectById(id);
        if (answer == null) throw new BusinessException(404, "回答不存在");
        if (!"pending".equals(answer.getStatus())) throw new BusinessException(400, "只能审核待审核的回答");

        answer.setStatus("published");
        answer.setUpdatedAt(LocalDateTime.now());
        answerMapper.updateById(answer);

        // +3 exp for answer author, check achievements
        if (answer.getUserId() != null) {
            User author = userMapper.selectById(answer.getUserId());
            if (author != null) {
                author.setExp((author.getExp() != null ? author.getExp() : 0) + 3);
                author.setLevel(calculateLevel(author.getExp()));
                userMapper.updateById(author);
                achievementService.checkAndUnlock(author.getId(), "first_answer");
                // Check total answers count for "fifty_answers"
                Long answerCount = answerMapper.selectCount(
                        new LambdaQueryWrapper<Answer>()
                                .eq(Answer::getUserId, answer.getUserId())
                                .eq(Answer::getStatus, "published"));
                if (answerCount >= 50) {
                    achievementService.checkAndUnlock(answer.getUserId(), "fifty_answers");
                }
            }
        }
    }

    @Override
    public void rejectAnswer(Long id, String reason) {
        Answer answer = answerMapper.selectById(id);
        if (answer == null) throw new BusinessException(404, "回答不存在");
        if (!"pending".equals(answer.getStatus())) throw new BusinessException(400, "只能审核待审核的回答");
        if (reason == null || reason.isBlank()) throw new BusinessException(400, "拒绝原因不能为空");

        answer.setStatus("rejected");
        answer.setAuditReason(reason);
        answer.setUpdatedAt(LocalDateTime.now());
        answerMapper.updateById(answer);
    }

    private Integer calculateLevel(int exp) {
        if (exp >= 20000) return 10;
        if (exp >= 12000) return 9;
        if (exp >= 7000) return 8;
        if (exp >= 4000) return 7;
        if (exp >= 2000) return 6;
        if (exp >= 1000) return 5;
        if (exp >= 600) return 4;
        if (exp >= 300) return 3;
        if (exp >= 100) return 2;
        return 1;
    }

    private void validateBase64Images(String content) {
        if (content == null) return;
        int count = 0, idx = 0;
        String prefix = "data:image/", base64Prefix = ";base64,";
        while ((idx = content.indexOf(prefix, idx)) != -1) {
            count++;
            if (count > MAX_IMAGE_COUNT) throw new BusinessException(400, "单条回答最多包含" + MAX_IMAGE_COUNT + "张图片");
            int base64Start = content.indexOf(base64Prefix, idx);
            if (base64Start == -1) { idx += prefix.length(); continue; }
            int dataStart = base64Start + base64Prefix.length();
            int dataEnd = dataStart;
            while (dataEnd < content.length()) {
                char c = content.charAt(dataEnd);
                if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '+' || c == '/' || c == '=') dataEnd++;
                else break;
            }
            try {
                byte[] decoded = Base64.getDecoder().decode(content.substring(dataStart, dataEnd));
                if (decoded.length > MAX_IMAGE_SIZE) throw new BusinessException(400, "单张图片不能超过2MB");
            } catch (IllegalArgumentException e) {
                throw new BusinessException(400, "图片格式无效");
            }
            idx = dataEnd;
        }
    }

    private AnswerVO buildAnswerVO(Answer answer) {
        if (answer == null) return null;
        AnswerVO vo = new AnswerVO();
        vo.setId(answer.getId());
        vo.setContent(answer.getContent());
        vo.setQuestionId(answer.getQuestionId());
        vo.setUserId(answer.getUserId());
        vo.setIsAccepted(answer.getIsAccepted());
        vo.setVoteCount(answer.getVoteCount() != null ? answer.getVoteCount() : 0);
        vo.setCommentCount(answer.getCommentCount() != null ? answer.getCommentCount() : 0);
        vo.setStatus(answer.getStatus());
        vo.setAuditReason(answer.getAuditReason());
        vo.setCreatedAt(answer.getCreatedAt());
        vo.setUpdatedAt(answer.getUpdatedAt());
        if (answer.getUserId() != null) {
            User author = userMapper.selectById(answer.getUserId());
            if (author != null) {
                vo.setUsername(author.getUsername());
                vo.setNickname(author.getNickname());
            }
        }
        return vo;
    }
}
