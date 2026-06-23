package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.dto.QuestionVO;

public interface FavoriteService {

    boolean toggleFavorite(Long userId, Long questionId);

    boolean hasFavorited(Long userId, Long questionId);

    IPage<QuestionVO> listMyFavorites(Long userId, Integer page, Integer pageSize);
}
