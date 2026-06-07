package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@TableName("t_team")
public class Team {
    @TableId(type = IdType.AUTO)
    private Long id;
    @NotBlank(message = "球队中文名不能为空")
    private String nameCn;
    @NotBlank(message = "球队英文名不能为空")
    private String nameEn;
    @NotBlank(message = "国家或地区不能为空")
    private String country;
    @NotBlank(message = "洲际足联不能为空")
    private String confederation;
    private String groupName;
    private String flagUrl;
    private String description;
    private String source;
    private LocalDateTime sourceUpdatedAt;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNameCn() { return nameCn; }
    public void setNameCn(String nameCn) { this.nameCn = nameCn; }
    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getConfederation() { return confederation; }
    public void setConfederation(String confederation) { this.confederation = confederation; }
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public String getFlagUrl() { return flagUrl; }
    public void setFlagUrl(String flagUrl) { this.flagUrl = flagUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public LocalDateTime getSourceUpdatedAt() { return sourceUpdatedAt; }
    public void setSourceUpdatedAt(LocalDateTime sourceUpdatedAt) { this.sourceUpdatedAt = sourceUpdatedAt; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
