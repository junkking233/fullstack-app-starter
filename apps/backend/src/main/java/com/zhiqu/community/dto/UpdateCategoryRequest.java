package com.zhiqu.community.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateCategoryRequest {

    @NotBlank(message = "分类名称不能为空")
    private String name;

    private Integer sortOrder;

    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
