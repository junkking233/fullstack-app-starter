package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.dto.AnswerVO;
import com.zhiqu.community.dto.CreateAnswerRequest;
import com.zhiqu.community.entity.Answer;

public interface AnswerService {

    Answer createAnswer(CreateAnswerRequest request, Long questionId, Long userId);

    IPage<AnswerVO> getAnswersByQuestionId(Long questionId, int page, int pageSize);

    void acceptAnswer(Long answerId, Long userId);

    AnswerVO updateAnswer(Long id, String content, Long userId);

    void deleteAnswer(Long id, Long userId);

    IPage<Answer> getPendingList(int page, int pageSize);

    void approveAnswer(Long id);

    void rejectAnswer(Long id, String reason);
}
