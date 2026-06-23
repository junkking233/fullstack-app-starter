package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.dto.QuestionVO;
import com.zhiqu.community.entity.Favorite;
import com.zhiqu.community.entity.Question;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.entity.Category;
import com.zhiqu.community.entity.Tag;
import com.zhiqu.community.entity.QuestionTag;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.*;
import com.zhiqu.community.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final QuestionMapper questionMapper;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final QuestionTagMapper questionTagMapper;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper,
                               QuestionMapper questionMapper,
                               UserMapper userMapper,
                               CategoryMapper categoryMapper,
                               TagMapper tagMapper,
                               QuestionTagMapper questionTagMapper) {
        this.favoriteMapper = favoriteMapper;
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.questionTagMapper = questionTagMapper;
    }

    @Override
    @Transactional
    public boolean toggleFavorite(Long userId, Long questionId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getQuestionId, questionId);
        Favorite existing = favoriteMapper.selectOne(wrapper);

        if (existing != null) {
            favoriteMapper.deleteById(existing.getId());
            Question question = questionMapper.selectById(questionId);
            if (question != null && question.getFavoriteCount() != null && question.getFavoriteCount() > 0) {
                question.setFavoriteCount(question.getFavoriteCount() - 1);
                questionMapper.updateById(question);
            }
            return false;
        } else {
            Favorite fav = new Favorite();
            fav.setUserId(userId);
            fav.setQuestionId(questionId);
            fav.setCreatedAt(LocalDateTime.now());
            favoriteMapper.insert(fav);
            Question question = questionMapper.selectById(questionId);
            if (question != null) {
                question.setFavoriteCount(question.getFavoriteCount() == null ? 1 : question.getFavoriteCount() + 1);
                questionMapper.updateById(question);
            }
            return true;
        }
    }

    @Override
    public boolean hasFavorited(Long userId, Long questionId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .eq(Favorite::getQuestionId, questionId);
        return favoriteMapper.selectCount(wrapper) > 0;
    }

    @Override
    public IPage<QuestionVO> listMyFavorites(Long userId, Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        Page<Favorite> favPage = new Page<>(pn, ps);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
               .orderByDesc(Favorite::getCreatedAt);
        IPage<Favorite> favs = favoriteMapper.selectPage(favPage, wrapper);

        IPage<QuestionVO> voPage = new Page<>(pn, ps, favs.getTotal());
        List<QuestionVO> vos = favs.getRecords().stream().map(fav -> {
            Question q = questionMapper.selectById(fav.getQuestionId());
            if (q == null) return null;
            QuestionVO vo = new QuestionVO();
            vo.setId(q.getId());
            vo.setTitle(q.getTitle());
            vo.setContent(q.getContent());
            vo.setUserId(q.getUserId());
            vo.setCategoryId(q.getCategoryId());
            vo.setViewCount(q.getViewCount() != null ? q.getViewCount() : 0);
            vo.setAnswerCount(q.getAnswerCount() != null ? q.getAnswerCount() : 0);
            vo.setVoteCount(q.getVoteCount() != null ? q.getVoteCount() : 0);
            vo.setFavoriteCount(q.getFavoriteCount() != null ? q.getFavoriteCount() : 0);
            vo.setStatus(q.getStatus());
            vo.setIsPinned(q.getIsPinned());
            vo.setIsFeatured(q.getIsFeatured());
            vo.setCreatedAt(q.getCreatedAt());
            vo.setUpdatedAt(q.getUpdatedAt());
            if (q.getUserId() != null) {
                User author = userMapper.selectById(q.getUserId());
                if (author != null) {
                    vo.setUsername(author.getUsername());
                    vo.setNickname(author.getNickname());
                }
            }
            if (q.getCategoryId() != null) {
                Category cat = categoryMapper.selectById(q.getCategoryId());
                if (cat != null) vo.setCategoryName(cat.getName());
            }
            List<QuestionTag> qtList = questionTagMapper.selectList(
                    new LambdaQueryWrapper<QuestionTag>().eq(QuestionTag::getQuestionId, q.getId()));
            List<String> tagNames = new ArrayList<>();
            for (QuestionTag qt : qtList) {
                Tag tag = tagMapper.selectById(qt.getTagId());
                if (tag != null) tagNames.add(tag.getName());
            }
            vo.setTags(tagNames);
            return vo;
        }).filter(v -> v != null).collect(Collectors.toList());
        voPage.setRecords(vos);
        return voPage;
    }
}
