package com.zhiqu.community.controller;

import com.zhiqu.community.common.Result;
import com.zhiqu.community.dto.CreateSensitiveWordRequest;
import com.zhiqu.community.dto.UpdateSensitiveWordRequest;
import com.zhiqu.community.entity.SensitiveWord;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.SensitiveWordService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SensitiveWordController {

    private final SensitiveWordService sensitiveWordService;

    public SensitiveWordController(SensitiveWordService sensitiveWordService) {
        this.sensitiveWordService = sensitiveWordService;
    }

    /** 公开接口：前端敏感词实时过滤用 */
    @GetMapping("/api/sensitive-words/active")
    public Result<List<String>> listActiveWords() {
        List<String> words = sensitiveWordService.listActive().stream()
                .map(SensitiveWord::getWord).toList();
        return Result.ok(words);
    }

    @GetMapping("/api/admin/sensitive-words")
    public Result<List<SensitiveWord>> listAll() {
        return Result.ok(sensitiveWordService.listAll());
    }

    @PostMapping("/api/admin/sensitive-words")
    public Result<SensitiveWord> create(@Valid @RequestBody CreateSensitiveWordRequest req, HttpServletRequest request) {
        return Result.ok("创建成功", sensitiveWordService.create(req, currentUser(request).getUserId()));
    }

    @PutMapping("/api/admin/sensitive-words/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        sensitiveWordService.updateStatus(id, status);
        return Result.ok("更新成功", null);
    }

    @PutMapping("/api/admin/sensitive-words/{id}")
    public Result<SensitiveWord> updateWord(@PathVariable Long id, @Valid @RequestBody UpdateSensitiveWordRequest request) {
        return Result.ok("更新成功", sensitiveWordService.updateWord(id, request.getWord()));
    }

    @DeleteMapping("/api/admin/sensitive-words/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sensitiveWordService.delete(id);
        return Result.ok("删除成功", null);
    }

    private TokenSubject currentUser(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject ts) {
            return ts;
        }
        throw new BusinessException(401, "请先登录");
    }
}
