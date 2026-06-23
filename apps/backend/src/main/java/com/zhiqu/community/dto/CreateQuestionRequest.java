package com.zhiqu.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateQuestionRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最长200字符")
    private String title;

    private String content;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    @Size(max = 5, message = "最多选择5个标签")
    private List<Long> tagIds;

    @Size(max = 3, message = "最多邀请3人")
    private List<Long> inviteeIds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public List<Long> getInviteeIds() {
        return inviteeIds;
    }

    public void setInviteeIds(List<Long> inviteeIds) {
        this.inviteeIds = inviteeIds;
    }
}
