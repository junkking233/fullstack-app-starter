package com.zhiqu.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiqu.community.entity.QuestionTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionTagMapper extends BaseMapper<QuestionTag> {

    int insertBatch(@Param("questionId") Long questionId, @Param("tagIds") List<Long> tagIds);

    int deleteByQuestionId(@Param("questionId") Long questionId);
}
