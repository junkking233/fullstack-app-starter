package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Comment;
import com.example.exception.BusinessException;
import com.example.mapper.CommentMapper;
import com.example.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentMapper commentMapper;

    public CommentController(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @GetMapping("/match/{matchId}")
    public Result<List<Comment>> approvedByMatch(@PathVariable Long matchId) {
        return Result.ok(commentMapper.selectList(new QueryWrapper<Comment>()
                .eq("match_id", matchId).eq("audit_status", "APPROVED").orderByDesc("create_time")));
    }

    @GetMapping("/my")
    public Result<List<Comment>> my(HttpServletRequest request) {
        return Result.ok(commentMapper.selectList(new QueryWrapper<Comment>()
                .eq("user_id", currentUser(request).getUserId()).orderByDesc("create_time")));
    }

    @PostMapping
    public Result<Comment> create(@RequestBody Comment comment, HttpServletRequest request) {
        if (comment.getMatchId() == null || comment.getContent() == null || comment.getContent().isBlank()) {
            throw new BusinessException(400, "比赛和评论内容不能为空");
        }
        comment.setUserId(currentUser(request).getUserId());
        comment.setAuditStatus("PENDING");
        commentMapper.insert(comment);
        return Result.ok("评论已提交，等待审核", comment);
    }

    @GetMapping
    public Result<IPage<Comment>> list(String auditStatus, @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer pageSize) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        if (auditStatus != null && !auditStatus.isBlank()) {
            wrapper.eq("audit_status", auditStatus);
        }
        wrapper.orderByDesc("create_time");
        return Result.ok(commentMapper.selectPage(new Page<>(page, pageSize), wrapper));
    }

    @PutMapping("/{id}/review")
    public Result<Comment> review(@PathVariable Long id, @RequestBody Map<String, String> body, HttpServletRequest request) {
        String status = body.get("auditStatus");
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new BusinessException(400, "审核状态不合法");
        }
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException(404, "评论不存在");
        }
        comment.setAuditStatus(status);
        comment.setAuditUserId(currentUser(request).getUserId());
        comment.setAuditTime(LocalDateTime.now());
        commentMapper.updateById(comment);
        return Result.ok("审核完成", comment);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentMapper.deleteById(id);
        return Result.ok("删除成功", null);
    }

    private TokenSubject currentUser(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject tokenSubject) {
            return tokenSubject;
        }
        throw new BusinessException(401, "请先登录");
    }
}
