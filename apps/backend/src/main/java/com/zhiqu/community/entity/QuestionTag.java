package com.zhiqu.community.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_question_tag")
public class QuestionTag {

    @TableField("question_id")
    private Long questionId;

    @TableField("tag_id")
    private Long tagId;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
