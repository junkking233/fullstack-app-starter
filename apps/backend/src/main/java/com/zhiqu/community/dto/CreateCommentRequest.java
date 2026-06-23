package com.zhiqu.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCommentRequest {

    @NotBlank(message = "内容不能为空")
    @Size(max = 500, message = "评论最多500字符")
    private String content;

    private Long parentId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
