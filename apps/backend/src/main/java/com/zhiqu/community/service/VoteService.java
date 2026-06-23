package com.zhiqu.community.service;

public interface VoteService {

    boolean toggleVote(Long userId, String targetType, Long targetId);

    boolean hasVoted(Long userId, String targetType, Long targetId);
}
