package com.zhiqu.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTagRequest {

    @NotBlank(message = "标签名称不能为空")
    @Size(max = 30, message = "标签名称不能超过30个字符")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
