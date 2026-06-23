package com.zhiqu.community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.common.Result;
import com.zhiqu.community.dto.CreateQuestionRequest;
import com.zhiqu.community.dto.QuestionListQuery;
import com.zhiqu.community.dto.QuestionVO;
import com.zhiqu.community.dto.ReviewRequest;
import com.zhiqu.community.dto.UpdateQuestionRequest;
import com.zhiqu.community.entity.Question;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.QuestionService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/api/questions")
    public Result<Question> create(@Valid @RequestBody CreateQuestionRequest request,
                                   HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        Question question = questionService.createQuestion(request, currentUser.getUserId());
        return Result.ok("发布成功，等待审核", question);
    }

    @GetMapping("/api/questions")
    public Result<IPage<QuestionVO>> list(QuestionListQuery query) {
        return Result.ok(questionService.listQuestions(query));
    }

    @GetMapping("/api/questions/search")
    public Result<IPage<QuestionVO>> search(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) Long categoryId,
                                            @RequestParam(required = false) Long tagId,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "20") Integer pageSize,
                                            @RequestParam(defaultValue = "recommend") String sort) {
        return Result.ok(questionService.search(keyword, categoryId, tagId, page, pageSize, sort));
    }

    @GetMapping("/api/questions/{id}")
    public Result<QuestionVO> detail(@PathVariable Long id, HttpServletRequest request) {
        Long currentUserId = getOptionalUserId(request);
        return Result.ok(questionService.getQuestionDetail(id, currentUserId));
    }

    @PutMapping("/api/questions/{id}")
    public Result<QuestionVO> update(@PathVariable Long id,
                                     @Valid @RequestBody UpdateQuestionRequest updateRequest,
                                     HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        QuestionVO vo = questionService.updateQuestion(id, updateRequest, currentUser.getUserId());
        return Result.ok("更新成功", vo);
    }

    @DeleteMapping("/api/questions/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        questionService.deleteQuestion(id, currentUser.getUserId());
        return Result.ok("删除成功", null);
    }

    @GetMapping("/api/questions/{id}/related")
    public Result<?> relatedQuestions(@PathVariable Long id,
                                      @RequestParam(defaultValue = "5") Integer limit) {
        return Result.ok(questionService.getRelatedQuestions(id, limit != null ? limit : 5));
    }

    // Admin endpoints
    @GetMapping("/api/admin/review/questions")
    public Result<IPage<Question>> getPendingList(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.ok(questionService.getPendingList(page, pageSize));
    }

    @PostMapping("/api/admin/review/questions/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        questionService.approveQuestion(id, null);
        return Result.ok("审核通过", null);
    }

    @PostMapping("/api/admin/review/questions/{id}/reject")
    public Result<Void> reject(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        questionService.rejectQuestion(id, request.getReason());
        return Result.ok("已拒绝", null);
    }

    @PutMapping("/api/admin/questions/{id}/pin")
    public Result<Void> togglePin(@PathVariable Long id) {
        questionService.pinQuestion(id);
        return Result.ok("操作成功", null);
    }

    @PutMapping("/api/admin/questions/{id}/feature")
    public Result<Void> toggleFeature(@PathVariable Long id) {
        questionService.featureQuestion(id);
        return Result.ok("操作成功", null);
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject tokenSubject) {
            return tokenSubject;
        }
        throw new BusinessException(401, "请先登录");
    }

    private Long getOptionalUserId(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject tokenSubject) {
            return tokenSubject.getUserId();
        }
        return null;
    }
}
