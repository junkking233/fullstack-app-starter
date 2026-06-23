package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqu.community.entity.Vote;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.mapper.AnswerMapper;
import com.zhiqu.community.mapper.QuestionMapper;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.mapper.VoteMapper;
import com.zhiqu.community.service.UserAchievementService;
import com.zhiqu.community.service.VoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteMapper voteMapper;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;
    private final UserMapper userMapper;
    private final UserAchievementService achievementService;

    public VoteServiceImpl(VoteMapper voteMapper, QuestionMapper questionMapper,
                           AnswerMapper answerMapper, UserMapper userMapper,
                           UserAchievementService achievementService) {
        this.voteMapper = voteMapper;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
        this.userMapper = userMapper;
        this.achievementService = achievementService;
    }

    @Override
    @Transactional
    public boolean toggleVote(Long userId, String targetType, Long targetId) {
        LambdaQueryWrapper<Vote> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Vote::getUserId, userId)
               .eq(Vote::getTargetType, targetType)
               .eq(Vote::getTargetId, targetId);
        Vote existing = voteMapper.selectOne(wrapper);

        if (existing != null) {
            voteMapper.deleteById(existing.getId());
            updateTargetVoteCount(targetType, targetId, -1);
            return false;
        } else {
            Vote vote = new Vote();
            vote.setUserId(userId);
            vote.setTargetType(targetType);
            vote.setTargetId(targetId);
            vote.setCreatedAt(LocalDateTime.now());
            voteMapper.insert(vote);
            updateTargetVoteCount(targetType, targetId, 1);

            // +2 exp to the content author, check vote milestones
            Long authorId = getTargetAuthorId(targetType, targetId);
            if (authorId != null && !authorId.equals(userId)) {
                User author = userMapper.selectById(authorId);
                if (author != null) {
                    author.setExp((author.getExp() != null ? author.getExp() : 0) + 2);
                    author.setLevel(calculateLevel(author.getExp()));
                    userMapper.updateById(author);
                }
            }

            // Check total votes received by the author
            if (authorId != null) {
                long totalVotes = voteMapper.selectCount(
                        new LambdaQueryWrapper<Vote>().eq(Vote::getTargetId, targetId));
                if (totalVotes >= 100) {
                    achievementService.checkAndUnlock(authorId, "hundred_votes");
                }
                if (totalVotes >= 500) {
                    achievementService.checkAndUnlock(authorId, "five_hundred_votes");
                }
            }

            return true;
        }
    }

    @Override
    public boolean hasVoted(Long userId, String targetType, Long targetId) {
        LambdaQueryWrapper<Vote> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Vote::getUserId, userId)
               .eq(Vote::getTargetType, targetType)
               .eq(Vote::getTargetId, targetId);
        return voteMapper.selectCount(wrapper) > 0;
    }

    private void updateTargetVoteCount(String targetType, Long targetId, int delta) {
        if ("question".equals(targetType)) {
            questionMapper.updateVoteCount(targetId, delta);
        } else if ("answer".equals(targetType)) {
            answerMapper.updateVoteCount(targetId, delta);
        }
    }

    private Long getTargetAuthorId(String targetType, Long targetId) {
        if ("question".equals(targetType)) {
            var q = questionMapper.selectById(targetId);
            return q != null ? q.getUserId() : null;
        } else if ("answer".equals(targetType)) {
            var a = answerMapper.selectById(targetId);
            return a != null ? a.getUserId() : null;
        }
        return null;
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
}
