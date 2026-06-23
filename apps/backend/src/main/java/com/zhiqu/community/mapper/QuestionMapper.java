package com.zhiqu.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.dto.QuestionListQuery;
import com.zhiqu.community.entity.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface QuestionMapper extends BaseMapper<Question> {

    IPage<Question> selectByCondition(Page<Question> page, @Param("query") QuestionListQuery query);

    int updateViewCount(@Param("id") Long id);

    @Update("UPDATE t_question SET vote_count = vote_count + #{delta} WHERE id = #{id}")
    int updateVoteCount(@Param("id") Long id, @Param("delta") Integer delta);

    @Update("UPDATE t_question SET favorite_count = favorite_count + #{delta} WHERE id = #{id}")
    int updateFavoriteCount(@Param("id") Long id, @Param("delta") Integer delta);
}
