package com.zhiqu.community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.common.Result;
import com.zhiqu.community.dto.AnswerVO;
import com.zhiqu.community.dto.CreateAnswerRequest;
import com.zhiqu.community.dto.ReviewRequest;
import com.zhiqu.community.entity.Answer;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.AnswerService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/api/questions/{questionId}/answers")
    public Result<Answer> create(@PathVariable Long questionId,
                                 @Valid @RequestBody CreateAnswerRequest request,
                                 HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        Answer answer = answerService.createAnswer(request, questionId, currentUser.getUserId());
        return Result.ok("发布成功，等待审核", answer);
    }

    @GetMapping("/api/questions/{questionId}/answers")
    public Result<IPage<AnswerVO>> list(@PathVariable Long questionId,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(answerService.getAnswersByQuestionId(questionId, page, pageSize));
    }

    @PutMapping("/api/answers/{id}")
    public Result<AnswerVO> update(@PathVariable Long id,
                                   @Valid @RequestBody CreateAnswerRequest request,
                                   HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        AnswerVO vo = answerService.updateAnswer(id, request.getContent(), currentUser.getUserId());
        return Result.ok("更新成功", vo);
    }

    @DeleteMapping("/api/answers/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        answerService.deleteAnswer(id, currentUser.getUserId());
        return Result.ok("删除成功", null);
    }

    @PostMapping("/api/answers/{id}/accept")
    public Result<Void> accept(@PathVariable Long id, HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        answerService.acceptAnswer(id, currentUser.getUserId());
        return Result.ok("采纳成功", null);
    }

    // Admin endpoints
    @GetMapping("/api/admin/review/answers")
    public Result<IPage<Answer>> getPendingList(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(answerService.getPendingList(page, pageSize));
    }

    @PostMapping("/api/admin/review/answers/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        answerService.approveAnswer(id);
        return Result.ok("审核通过", null);
    }

    @PostMapping("/api/admin/review/answers/{id}/reject")
    public Result<Void> reject(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        answerService.rejectAnswer(id, request.getReason());
        return Result.ok("已拒绝", null);
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject tokenSubject) {
            return tokenSubject;
        }
        throw new BusinessException(401, "请先登录");
    }
}
