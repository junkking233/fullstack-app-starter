package com.zhiqu.community.controller;

import com.zhiqu.community.common.Result;
import com.zhiqu.community.service.ExportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/api/admin/export/users")
    public Result<List<Map<String, Object>>> exportUsers() {
        return Result.ok(exportService.exportUsers());
    }

    @GetMapping("/api/admin/export/questions")
    public Result<List<Map<String, Object>>> exportQuestions() {
        return Result.ok(exportService.exportQuestions());
    }

    @GetMapping("/api/admin/export/answers")
    public Result<List<Map<String, Object>>> exportAnswers() {
        return Result.ok(exportService.exportAnswers());
    }
}
