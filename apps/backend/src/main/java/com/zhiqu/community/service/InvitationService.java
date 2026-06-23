package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.entity.Invitation;

import java.util.List;

public interface InvitationService {

    void inviteToAnswer(Long questionId, Long inviterId, List<Long> inviteeIds);

    IPage<Invitation> listMyInvitations(Long userId, Integer page, Integer pageSize);

    IPage<Invitation> listQuestionInvitations(Long questionId, Integer page, Integer pageSize);
}
