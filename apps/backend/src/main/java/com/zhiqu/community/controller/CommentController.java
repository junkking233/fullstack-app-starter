package com.zhiqu.community.controller;

import com.zhiqu.community.common.Result;
import com.zhiqu.community.dto.CommentVO;
import com.zhiqu.community.dto.CreateCommentRequest;
import com.zhiqu.community.entity.Comment;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.CommentService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/api/answers/{answerId}/comments")
    public Result<Comment> create(@PathVariable Long answerId,
                                  @Valid @RequestBody CreateCommentRequest request,
                                  HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        Comment comment = commentService.createComment(request, answerId, currentUser.getUserId());
        return Result.ok("评论成功", comment);
    }

    @GetMapping("/api/answers/{answerId}/comments")
    public Result<List<CommentVO>> list(@PathVariable Long answerId) {
        return Result.ok(commentService.getCommentsByAnswerId(answerId));
    }

    @DeleteMapping("/api/comments/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        commentService.deleteComment(id, currentUser.getUserId());
        return Result.ok("删除成功", null);
    }

    // Admin endpoints
    @GetMapping("/api/admin/comments")
    public Result<List<CommentVO>> listAll(@RequestParam Long answerId) {
        return Result.ok(commentService.getCommentsByAnswerId(answerId));
    }

    @DeleteMapping("/api/admin/comments/{id}")
    public Result<Void> adminDelete(@PathVariable Long id, HttpServletRequest httpRequest) {
        TokenSubject currentUser = requireLogin(httpRequest);
        commentService.deleteComment(id, currentUser.getUserId());
        return Result.ok("删除成功", null);
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject tokenSubject) {
            return tokenSubject;
        }
        throw new BusinessException(401, "请先登录");
    }
}
