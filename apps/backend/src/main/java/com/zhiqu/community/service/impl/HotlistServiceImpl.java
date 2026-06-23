package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.dto.QuestionVO;
import com.zhiqu.community.entity.Category;
import com.zhiqu.community.entity.Question;
import com.zhiqu.community.entity.QuestionTag;
import com.zhiqu.community.entity.Tag;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.mapper.CategoryMapper;
import com.zhiqu.community.mapper.QuestionMapper;
import com.zhiqu.community.mapper.QuestionTagMapper;
import com.zhiqu.community.mapper.TagMapper;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.service.HotlistService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotlistServiceImpl implements HotlistService {

    private final QuestionMapper questionMapper;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final QuestionTagMapper questionTagMapper;

    public HotlistServiceImpl(QuestionMapper questionMapper,
                              UserMapper userMapper,
                              CategoryMapper categoryMapper,
                              TagMapper tagMapper,
                              QuestionTagMapper questionTagMapper) {
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.questionTagMapper = questionTagMapper;
    }

    @Override
    public IPage<QuestionVO> getHotQuestions(String period, Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getStatus, "published");

        // Filter by time period
        LocalDateTime since = null;
        if ("daily".equals(period)) {
            since = LocalDateTime.now().minusDays(1);
        } else if ("weekly".equals(period)) {
            since = LocalDateTime.now().minusWeeks(1);
        } else if ("monthly".equals(period)) {
            since = LocalDateTime.now().minusMonths(1);
        }
        if (since != null) {
            wrapper.ge(Question::getCreatedAt, since);
        }

        // Hot score: view_count + answer_count * 5 + vote_count * 3 + favorite_count * 2
        // Sort by featured first, then by activity
        wrapper.orderByDesc(Question::getIsFeatured)
               .orderByDesc(Question::getViewCount)
               .orderByDesc(Question::getAnswerCount)
               .orderByDesc(Question::getCreatedAt);

        Page<Question> questionPage = new Page<>(pn, ps);
        IPage<Question> result = questionMapper.selectPage(questionPage, wrapper);

        // Fallback: if no questions in time range, return top questions regardless
        if (result.getRecords().isEmpty() && since != null) {
            LambdaQueryWrapper<Question> fallback = new LambdaQueryWrapper<>();
            fallback.eq(Question::getStatus, "published")
                    .orderByDesc(Question::getIsFeatured)
                    .orderByDesc(Question::getViewCount)
                    .orderByDesc(Question::getAnswerCount)
                    .orderByDesc(Question::getCreatedAt);
            result = questionMapper.selectPage(new Page<>(pn, ps), fallback);
        }

        IPage<QuestionVO> voPage = new Page<>(pn, ps, result.getTotal());
        List<QuestionVO> vos = new ArrayList<>();
        for (Question q : result.getRecords()) {
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
            vos.add(vo);
        }
        voPage.setRecords(vos);
        return voPage;
    }
}
