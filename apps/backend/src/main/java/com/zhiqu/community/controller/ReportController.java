package com.zhiqu.community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.common.Result;
import com.zhiqu.community.entity.Report;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.ReportService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/api/reports")
    public Result<Report> createReport(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        String targetType = (String) body.get("targetType");
        Long targetId = Long.valueOf(body.get("targetId").toString());
        String reason = (String) body.get("reason");
        String detail = (String) body.get("detail");
        Report report = reportService.createReport(currentUser.getUserId(), targetType, targetId, reason, detail);
        return Result.ok("举报已提交，管理员会尽快处理", report);
    }

    @GetMapping("/api/admin/reports")
    public Result<IPage<Report>> listReports(@RequestParam(defaultValue = "pending") String status,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.ok(reportService.listByStatus(status, page, pageSize));
    }

    @PostMapping("/api/admin/reports/{id}/handle")
    public Result<Void> handleReport(@PathVariable Long id,
                                      @RequestBody Map<String, Object> body,
                                      HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        String action = (String) body.get("action");
        String resultNote = (String) body.get("resultNote");
        reportService.handleReport(id, currentUser.getUserId(), action, resultNote);
        return Result.ok("处理成功", null);
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject ts) return ts;
        throw new BusinessException(401, "请先登录");
    }
}
