package com.zhiqu.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiqu.community.entity.SensitiveWord;

import java.util.List;

public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {

    List<SensitiveWord> selectActiveWords();
}
