package com.zhiqu.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiqu.community.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> selectTree();

    int countQuestionsUnderCategory(@Param("categoryId") Long categoryId);
}
