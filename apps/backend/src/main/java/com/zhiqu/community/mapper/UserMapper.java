package com.zhiqu.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.dto.UserQuery;
import com.zhiqu.community.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    IPage<User> selectByCondition(Page<User> page, @Param("query") UserQuery query);
}
