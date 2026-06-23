package com.zhiqu.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReviewRequest {

    @NotBlank(message = "审核意见不能为空")
    @Size(max = 500, message = "审核意见最多500字符")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
