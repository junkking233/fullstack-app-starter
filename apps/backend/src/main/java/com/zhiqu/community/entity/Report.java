package com.zhiqu.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("t_report")
public class Report {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("reporter_id")
    private Long reporterId;

    @TableField("target_type")
    private String targetType;

    @TableField("target_id")
    private Long targetId;

    private String reason;

    private String detail;

    private String status;

    @TableField("handler_id")
    private Long handlerId;

    @TableField("result_note")
    private String resultNote;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("handled_at")
    private LocalDateTime handledAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReporterId() { return reporterId; }
    public void setReporterId(Long reporterId) { this.reporterId = reporterId; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getHandlerId() { return handlerId; }
    public void setHandlerId(Long handlerId) { this.handlerId = handlerId; }

    public String getResultNote() { return resultNote; }
    public void setResultNote(String resultNote) { this.resultNote = resultNote; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getHandledAt() { return handledAt; }
    public void setHandledAt(LocalDateTime handledAt) { this.handledAt = handledAt; }
}
