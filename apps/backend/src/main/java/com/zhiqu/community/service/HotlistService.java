package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.dto.QuestionVO;

public interface HotlistService {

    IPage<QuestionVO> getHotQuestions(String period, Integer page, Integer pageSize);
}
