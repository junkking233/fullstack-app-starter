package com.zhiqu.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiqu.community.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectByAnswerId(@Param("answerId") Long answerId);
}
