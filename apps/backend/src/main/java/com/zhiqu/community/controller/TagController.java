package com.zhiqu.community.controller;

import com.zhiqu.community.common.Result;
import com.zhiqu.community.dto.CreateTagRequest;
import com.zhiqu.community.entity.Tag;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.TagService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public Result<List<Tag>> listAll() {
        return Result.ok(tagService.listAll());
    }

    @GetMapping("/tags/recommend")
    public Result<List<Tag>> recommendTags(@RequestParam(required = false, defaultValue = "") String keyword) {
        return Result.ok(tagService.recommendTags(keyword));
    }

    @PostMapping("/admin/tags")
    public Result<Tag> create(@Valid @RequestBody CreateTagRequest req, HttpServletRequest request) {
        return Result.ok("创建成功", tagService.create(req, currentUser(request).getUserId()));
    }

    @PutMapping("/admin/tags/{id}")
    public Result<Tag> update(@PathVariable Long id, @RequestParam String name) {
        return Result.ok("更新成功", tagService.update(id, name));
    }

    @DeleteMapping("/admin/tags/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
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
