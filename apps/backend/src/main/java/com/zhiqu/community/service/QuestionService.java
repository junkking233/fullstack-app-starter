package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.dto.CreateQuestionRequest;
import com.zhiqu.community.dto.QuestionListQuery;
import com.zhiqu.community.dto.QuestionVO;
import com.zhiqu.community.dto.UpdateQuestionRequest;
import com.zhiqu.community.entity.Question;

import java.util.List;

public interface QuestionService {

    Question createQuestion(CreateQuestionRequest request, Long userId);

    QuestionVO getQuestionDetail(Long questionId, Long currentUserId);

    IPage<QuestionVO> listQuestions(QuestionListQuery query);

    QuestionVO updateQuestion(Long id, UpdateQuestionRequest request, Long userId);

    void deleteQuestion(Long id, Long userId);

    IPage<QuestionVO> search(String keyword, Long categoryId, Long tagId, Integer page, Integer pageSize, String sort);

    IPage<Question> getPendingList(Integer page, Integer pageSize);

    void approveQuestion(Long id, String reason);

    void rejectQuestion(Long id, String reason);

    void pinQuestion(Long id);

    void featureQuestion(Long id);

    List<QuestionVO> getHotlist(String period, Integer limit);

    List<QuestionVO> getRelatedQuestions(Long questionId, int limit);
}
