package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqu.community.entity.QuestionInvitation;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.QuestionInvitationMapper;
import com.zhiqu.community.mapper.QuestionMapper;
import com.zhiqu.community.service.NotificationService;
import com.zhiqu.community.service.QuestionInvitationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionInvitationServiceImpl implements QuestionInvitationService {

    private final QuestionInvitationMapper questionInvitationMapper;
    private final QuestionMapper questionMapper;
    private final NotificationService notificationService;

    public QuestionInvitationServiceImpl(QuestionInvitationMapper questionInvitationMapper,
                                         QuestionMapper questionMapper,
                                         NotificationService notificationService) {
        this.questionInvitationMapper = questionInvitationMapper;
        this.questionMapper = questionMapper;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public List<Long> invite(Long questionId, Long inviterId, List<Long> inviteeIds) {
        if (inviteeIds == null || inviteeIds.isEmpty()) {
            throw new BusinessException(400, "邀请列表不能为空");
        }
        if (inviteeIds.size() > 3) {
            throw new BusinessException(400, "最多邀请3位用户");
        }

        // Verify question exists
        if (questionMapper.selectById(questionId) == null) {
            throw new BusinessException(404, "问题不存在");
        }

        List<Long> invitedIds = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Long inviteeId : inviteeIds) {
            // Cannot invite self
            if (inviteeId.equals(inviterId)) {
                continue;
            }

            // Check if already invited
            LambdaQueryWrapper<QuestionInvitation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(QuestionInvitation::getQuestionId, questionId)
                    .eq(QuestionInvitation::getInviteeId, inviteeId);
            Long count = questionInvitationMapper.selectCount(wrapper);
            if (count > 0) {
                continue;
            }

            QuestionInvitation invitation = new QuestionInvitation();
            invitation.setQuestionId(questionId);
            invitation.setInviterId(inviterId);
            invitation.setInviteeId(inviteeId);
            invitation.setStatus("pending");
            invitation.setCreatedAt(now);
            questionInvitationMapper.insert(invitation);

            // Send notification to invitee
            notificationService.createNotification(
                    inviteeId,
                    "invitation",
                    questionId,
                    inviterId,
                    "您收到了一个问题邀请"
            );

            invitedIds.add(inviteeId);
        }

        return invitedIds;
    }
}
