package com.zhiqu.community.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateAnswerRequest {

    @NotBlank(message = "内容不能为空")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
