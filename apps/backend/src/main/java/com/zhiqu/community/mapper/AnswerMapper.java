package com.zhiqu.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.entity.Answer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface AnswerMapper extends BaseMapper<Answer> {

    IPage<Answer> selectByQuestionId(Page<Answer> page, @Param("questionId") Long questionId);

    @Update("UPDATE t_answer SET vote_count = vote_count + #{delta} WHERE id = #{id}")
    int updateVoteCount(@Param("id") Long id, @Param("delta") Integer delta);

    @Update("UPDATE t_answer SET comment_count = comment_count + #{delta} WHERE id = #{id}")
    int updateCommentCount(@Param("id") Long id, @Param("delta") Integer delta);
}
