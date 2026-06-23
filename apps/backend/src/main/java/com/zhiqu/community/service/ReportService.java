package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.entity.Report;

public interface ReportService {

    Report createReport(Long reporterId, String targetType, Long targetId, String reason, String detail);

    IPage<Report> listByStatus(String status, Integer page, Integer pageSize);

    void handleReport(Long reportId, Long handlerId, String action, String resultNote);
}
