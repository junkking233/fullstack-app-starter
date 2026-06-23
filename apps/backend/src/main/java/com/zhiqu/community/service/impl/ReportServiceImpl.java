package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.entity.Report;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.ReportMapper;
import com.zhiqu.community.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Override
    @Transactional
    public Report createReport(Long reporterId, String targetType, Long targetId, String reason, String detail) {
        if (reason == null || reason.isBlank()) {
            throw new BusinessException(400, "举报原因不能为空");
        }
        // Check duplicate
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Report::getReporterId, reporterId)
               .eq(Report::getTargetType, targetType)
               .eq(Report::getTargetId, targetId);
        if (reportMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(400, "您已举报过该内容");
        }

        Report report = new Report();
        report.setReporterId(reporterId);
        report.setTargetType(targetType);
        report.setTargetId(targetId);
        report.setReason(reason);
        report.setDetail(detail);
        report.setStatus("pending");
        report.setCreatedAt(LocalDateTime.now());
        reportMapper.insert(report);
        return report;
    }

    @Override
    public IPage<Report> listByStatus(String status, Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isBlank()) {
            wrapper.eq(Report::getStatus, status);
        }
        wrapper.orderByDesc(Report::getCreatedAt);
        return reportMapper.selectPage(new Page<>(pn, ps), wrapper);
    }

    @Override
    @Transactional
    public void handleReport(Long reportId, Long handlerId, String action, String resultNote) {
        Report report = reportMapper.selectById(reportId);
        if (report == null) {
            throw new BusinessException(404, "举报不存在");
        }
        if (!"pending".equals(report.getStatus())) {
            throw new BusinessException(400, "该举报已处理");
        }
        report.setStatus(action);
        report.setHandlerId(handlerId);
        report.setResultNote(resultNote);
        report.setHandledAt(LocalDateTime.now());
        reportMapper.updateById(report);
    }
}
