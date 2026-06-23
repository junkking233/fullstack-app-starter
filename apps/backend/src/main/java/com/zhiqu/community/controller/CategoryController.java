package com.zhiqu.community.controller;

import com.zhiqu.community.common.Result;
import com.zhiqu.community.dto.CreateCategoryRequest;
import com.zhiqu.community.dto.UpdateCategoryRequest;
import com.zhiqu.community.entity.Category;
import com.zhiqu.community.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public Result<List<Category>> tree() {
        return Result.ok(categoryService.tree());
    }

    @GetMapping("/categories/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        return Result.ok(categoryService.getById(id));
    }

    @PostMapping("/admin/categories")
    public Result<Category> create(@Valid @RequestBody CreateCategoryRequest req) {
        return Result.ok("创建成功", categoryService.create(req));
    }

    @PutMapping("/admin/categories/{id}")
    public Result<Category> update(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest req) {
        return Result.ok("更新成功", categoryService.update(id, req));
    }

    @DeleteMapping("/admin/categories/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.ok("删除成功", null);
    }
}
