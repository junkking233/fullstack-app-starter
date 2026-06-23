package com.zhiqu.community.service;

import java.util.List;

public interface QuestionInvitationService {

    List<Long> invite(Long questionId, Long inviterId, List<Long> inviteeIds);
}
