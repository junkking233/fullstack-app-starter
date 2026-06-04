package com.example.controller;

import com.example.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/charts")
public class ChartController {

    @GetMapping("/user-growth")
    public Result<Map<String, Object>> userGrowth() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("xData", List.of("1月", "2月", "3月", "4月", "5月", "6月"));
        data.put("series", List.of(
            Map.of("name", "新增用户", "data", List.of(120, 182, 150, 212, 280, 248), "color", "#0d9488"),
            Map.of("name", "活跃用户", "data", List.of(80, 132, 110, 162, 220, 198), "color", "#3b82f6")
        ));
        return Result.ok(data);
    }

    @GetMapping("/role-distribution")
    public Result<List<Map<String, Object>>> roleDistribution() {
        return Result.ok(List.of(
            Map.of("name", "普通用户", "value", 1048),
            Map.of("name", "管理员", "value", 86),
            Map.of("name", "服务方", "value", 114)
        ));
    }

    @GetMapping("/task-trend")
    public Result<Map<String, Object>> taskTrend() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("xData", List.of("周一", "周二", "周三", "周四", "周五", "周六", "周日"));
        data.put("series", List.of(
            Map.of("name", "处理量", "data", List.of(28, 35, 42, 38, 45, 22, 18), "color", "#f59e0b"),
            Map.of("name", "完成量", "data", List.of(24, 30, 38, 35, 40, 20, 16), "color", "#22c55e")
        ));
        return Result.ok(data);
    }

    @GetMapping("/service-distribution")
    public Result<List<Map<String, Object>>> serviceDistribution() {
        return Result.ok(List.of(
            Map.of("name", "政务服务", "value", 320),
            Map.of("name", "生活服务", "value", 280),
            Map.of("name", "企业服务", "value", 220),
            Map.of("name", "金融服务", "value", 180)
        ));
    }

    @GetMapping("/satisfaction-trend")
    public Result<Map<String, Object>> satisfactionTrend() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("xData", List.of("1月", "2月", "3月", "4月", "5月", "6月"));
        data.put("series", List.of(
            Map.of("name", "满意度评分", "data", List.of(4.2, 4.3, 4.1, 4.5, 4.6, 4.7), "color", "#0d9488")
        ));
        return Result.ok(data);
    }

    @GetMapping("/dashboard-stats")
    public Result<Map<String, Object>> dashboardStats() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalUsers", 1248);
        data.put("activeUsers", 1106);
        data.put("apiRequests", 32800);
        data.put("pendingTasks", 18);
        return Result.ok(data);
    }
}
