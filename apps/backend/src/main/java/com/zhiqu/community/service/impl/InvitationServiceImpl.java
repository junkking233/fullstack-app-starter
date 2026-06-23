package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.entity.Invitation;
import com.zhiqu.community.entity.Question;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.InvitationMapper;
import com.zhiqu.community.mapper.QuestionMapper;
import com.zhiqu.community.service.InvitationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final InvitationMapper invitationMapper;
    private final QuestionMapper questionMapper;

    public InvitationServiceImpl(InvitationMapper invitationMapper, QuestionMapper questionMapper) {
        this.invitationMapper = invitationMapper;
        this.questionMapper = questionMapper;
    }

    @Override
    @Transactional
    public void inviteToAnswer(Long questionId, Long inviterId, List<Long> inviteeIds) {
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new BusinessException(404, "问题不存在");
        }
        if (inviteeIds == null || inviteeIds.isEmpty()) {
            throw new BusinessException(400, "邀请人不能为空");
        }

        for (Long inviteeId : inviteeIds) {
            if (inviteeId.equals(inviterId)) {
                continue; // Skip self-invitation
            }
            // Check for duplicate invitation
            LambdaQueryWrapper<Invitation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Invitation::getQuestionId, questionId)
                   .eq(Invitation::getInviteeId, inviteeId)
                   .eq(Invitation::getStatus, "pending");
            if (invitationMapper.selectCount(wrapper) > 0) {
                continue; // Already invited
            }

            Invitation invitation = new Invitation();
            invitation.setQuestionId(questionId);
            invitation.setInviterId(inviterId);
            invitation.setInviteeId(inviteeId);
            invitation.setStatus("pending");
            invitation.setCreatedAt(LocalDateTime.now());
            invitationMapper.insert(invitation);
        }
    }

    @Override
    public IPage<Invitation> listMyInvitations(Long userId, Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        LambdaQueryWrapper<Invitation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Invitation::getInviteeId, userId)
               .orderByDesc(Invitation::getCreatedAt);
        return invitationMapper.selectPage(new Page<>(pn, ps), wrapper);
    }

    @Override
    public IPage<Invitation> listQuestionInvitations(Long questionId, Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        LambdaQueryWrapper<Invitation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Invitation::getQuestionId, questionId)
               .orderByDesc(Invitation::getCreatedAt);
        return invitationMapper.selectPage(new Page<>(pn, ps), wrapper);
    }
}
