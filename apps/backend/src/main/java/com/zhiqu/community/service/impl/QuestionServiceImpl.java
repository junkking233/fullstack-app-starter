package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.dto.CreateQuestionRequest;
import com.zhiqu.community.dto.QuestionListQuery;
import com.zhiqu.community.dto.QuestionVO;
import com.zhiqu.community.dto.UpdateQuestionRequest;
import com.zhiqu.community.entity.Category;
import com.zhiqu.community.entity.Question;
import com.zhiqu.community.entity.QuestionTag;
import com.zhiqu.community.entity.Tag;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.CategoryMapper;
import com.zhiqu.community.mapper.QuestionMapper;
import com.zhiqu.community.mapper.QuestionTagMapper;
import com.zhiqu.community.mapper.TagMapper;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.service.QuestionService;
import com.zhiqu.community.service.SensitiveWordService;
import com.zhiqu.community.service.UserAchievementService;
import com.zhiqu.community.util.TokenSubject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionTagMapper questionTagMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final UserMapper userMapper;
    private final SensitiveWordService sensitiveWordService;
    private final UserAchievementService achievementService;

    public QuestionServiceImpl(QuestionMapper questionMapper,
                               QuestionTagMapper questionTagMapper,
                               CategoryMapper categoryMapper,
                               TagMapper tagMapper,
                               UserMapper userMapper,
                               SensitiveWordService sensitiveWordService,
                               UserAchievementService achievementService) {
        this.questionMapper = questionMapper;
        this.questionTagMapper = questionTagMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.userMapper = userMapper;
        this.sensitiveWordService = sensitiveWordService;
        this.achievementService = achievementService;
    }

    @Override
    @Transactional
    public Question createQuestion(CreateQuestionRequest request, Long userId) {
        if (sensitiveWordService.containsSensitiveWord(request.getTitle())) {
            throw new BusinessException(4001, "标题包含不当用语，请修改");
        }
        if (request.getContent() != null && sensitiveWordService.containsSensitiveWord(request.getContent())) {
            throw new BusinessException(4001, "内容包含不当用语，请修改");
        }

        Category category = categoryMapper.selectById(request.getCategoryId());
        if (category == null || !"active".equals(category.getStatus())) {
            throw new BusinessException(400, "分类不存在或已停用");
        }

        Question question = new Question();
        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setUserId(userId);
        question.setCategoryId(request.getCategoryId());
        question.setViewCount(0);
        question.setAnswerCount(0);
        question.setVoteCount(0);
        question.setFavoriteCount(0);
        question.setStatus("pending");
        question.setIsPinned(0);
        question.setIsFeatured(0);
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());

        questionMapper.insert(question);

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            questionTagMapper.insertBatch(question.getId(), request.getTagIds());
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagMapper.selectById(tagId);
                if (tag != null) {
                    tag.setUsageCount(tag.getUsageCount() == null ? 1 : tag.getUsageCount() + 1);
                    tagMapper.updateById(tag);
                }
            }
        }

        return question;
    }

    @Override
    public QuestionVO getQuestionDetail(Long questionId, Long currentUserId) {
        Question question = questionMapper.selectById(questionId);
        if (question == null || "deleted".equals(question.getStatus())) {
            throw new BusinessException(404, "内容不存在");
        }

        // Author or admin can always view. Others can only view published content.
        if (!"published".equals(question.getStatus())) {
            boolean canView = false;
            if (currentUserId != null && question.getUserId().equals(currentUserId)) {
                canView = true;
            }
            if (!canView && currentUserId != null) {
                User viewer = userMapper.selectById(currentUserId);
                if (viewer != null && "ADMIN".equals(viewer.getRole())) {
                    canView = true;
                }
            }
            if (!canView) {
                throw new BusinessException(404, "内容不存在");
            }
        }

        // Increment view count for published questions
        if ("published".equals(question.getStatus())) {
            questionMapper.updateViewCount(questionId);
            question.setViewCount(question.getViewCount() + 1);
        }

        return buildQuestionVO(question);
    }

    @Override
    public IPage<QuestionVO> listQuestions(QuestionListQuery query) {
        int pageNum = query.getPage() != null ? query.getPage() : 1;
        int pageSize = query.getPageSize() != null ? Math.min(query.getPageSize(), 50) : 20;

        Page<Question> page = new Page<>(pageNum, pageSize);
        IPage<Question> result = questionMapper.selectByCondition(page, query);

        IPage<QuestionVO> voPage = new Page<>(pageNum, pageSize, result.getTotal());
        List<QuestionVO> vos = result.getRecords().stream()
                .map(this::buildQuestionVO)
                .collect(Collectors.toList());
        voPage.setRecords(vos);
        return voPage;
    }

    @Override
    @Transactional
    public QuestionVO updateQuestion(Long id, UpdateQuestionRequest request, Long userId) {
        Question question = questionMapper.selectById(id);
        if (question == null || "deleted".equals(question.getStatus())) {
            throw new BusinessException(404, "内容不存在");
        }
        if (!question.getUserId().equals(userId)) {
            throw new BusinessException(403, "只能编辑自己的问题");
        }

        if (sensitiveWordService.containsSensitiveWord(request.getTitle())) {
            throw new BusinessException(4001, "标题包含不当用语，请修改");
        }
        if (request.getContent() != null && sensitiveWordService.containsSensitiveWord(request.getContent())) {
            throw new BusinessException(4001, "内容包含不当用语，请修改");
        }

        question.setTitle(request.getTitle());
        if (request.getContent() != null) {
            question.setContent(request.getContent());
        }

        // Editing a rejected question auto-resets to pending
        if ("rejected".equals(question.getStatus())) {
            question.setStatus("pending");
            question.setAuditReason(null);
        }

        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);

        return buildQuestionVO(question);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id, Long userId) {
        Question question = questionMapper.selectById(id);
        if (question == null || "deleted".equals(question.getStatus())) {
            throw new BusinessException(404, "内容不存在");
        }

        User user = userMapper.selectById(userId);
        boolean isAdmin = user != null && "ADMIN".equals(user.getRole());

        if (!question.getUserId().equals(userId) && !isAdmin) {
            throw new BusinessException(403, "无权删除此问题");
        }

        question.setStatus("deleted");
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
    }

    @Override
    public IPage<QuestionVO> search(String keyword, Long categoryId, Long tagId, Integer page, Integer pageSize, String sort) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        Page<Question> questionPage = new Page<>(pn, ps);

        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getStatus, "published");

        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(Question::getTitle, keyword).or().like(Question::getContent, keyword));
        }
        if (categoryId != null) {
            wrapper.eq(Question::getCategoryId, categoryId);
        }

        if (tagId != null) {
            List<QuestionTag> qtList = questionTagMapper.selectList(
                    new LambdaQueryWrapper<QuestionTag>().eq(QuestionTag::getTagId, tagId)
            );
            List<Long> questionIds = qtList.stream().map(QuestionTag::getQuestionId).collect(Collectors.toList());
            if (questionIds.isEmpty()) {
                IPage<QuestionVO> emptyPage = new Page<>(pn, ps, 0);
                emptyPage.setRecords(new ArrayList<>());
                return emptyPage;
            }
            wrapper.in(Question::getId, questionIds);
        }

        if ("hot".equals(sort)) {
            wrapper.orderByDesc(Question::getAnswerCount, Question::getViewCount);
        }
        wrapper.orderByDesc(Question::getIsPinned, Question::getIsFeatured);
        wrapper.orderByDesc(Question::getCreatedAt);

        IPage<Question> result = questionMapper.selectPage(questionPage, wrapper);

        IPage<QuestionVO> voPage = new Page<>(pn, ps, result.getTotal());
        List<QuestionVO> vos = result.getRecords().stream()
                .map(this::buildQuestionVO)
                .collect(Collectors.toList());
        voPage.setRecords(vos);
        return voPage;
    }

    @Override
    public IPage<Question> getPendingList(Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? pageSize : 20;

        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getStatus, "pending")
                .orderByAsc(Question::getCreatedAt);

        return questionMapper.selectPage(new Page<>(pn, ps), wrapper);
    }

    @Override
    public void approveQuestion(Long id, String reason) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException(404, "问题不存在");
        }
        if (!"pending".equals(question.getStatus())) {
            throw new BusinessException(400, "只能审核待审核的问题");
        }

        question.setStatus("published");
        question.setAuditReason(reason);
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);

        // Add exp and check achievements
        if (question.getUserId() != null) {
            User author = userMapper.selectById(question.getUserId());
            if (author != null) {
                author.setExp((author.getExp() != null ? author.getExp() : 0) + 1);
                author.setLevel(calculateLevel(author.getExp()));
                userMapper.updateById(author);
                achievementService.checkAndUnlock(author.getId(), "registered");
                achievementService.checkAndUnlock(author.getId(), "first_question");
            }
        }
    }

    private Integer calculateLevel(int exp) {
        if (exp >= 20000) return 10;
        if (exp >= 12000) return 9;
        if (exp >= 7000) return 8;
        if (exp >= 4000) return 7;
        if (exp >= 2000) return 6;
        if (exp >= 1000) return 5;
        if (exp >= 600) return 4;
        if (exp >= 300) return 3;
        if (exp >= 100) return 2;
        return 1;
    }

    @Override
    public void rejectQuestion(Long id, String reason) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException(404, "问题不存在");
        }
        if (!"pending".equals(question.getStatus())) {
            throw new BusinessException(400, "只能审核待审核的问题");
        }
        if (reason == null || reason.isBlank()) {
            throw new BusinessException(400, "拒绝原因不能为空");
        }

        question.setStatus("rejected");
        question.setAuditReason(reason);
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
    }

    @Override
    public void pinQuestion(Long id) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException(404, "问题不存在");
        }

        question.setIsPinned(question.getIsPinned() == 1 ? 0 : 1);
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
    }

    @Override
    public void featureQuestion(Long id) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException(404, "问题不存在");
        }

        question.setIsFeatured(question.getIsFeatured() == 1 ? 0 : 1);
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
    }

    @Override
    public List<QuestionVO> getHotlist(String period, Integer limit) {
        int lim = limit != null ? Math.min(limit, 50) : 20;

        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getStatus, "published");

        // Only filter by time if period is specified and data exists
        if ("daily".equals(period)) {
            wrapper.gt(Question::getCreatedAt, LocalDateTime.now().minusDays(1));
        } else if ("weekly".equals(period)) {
            wrapper.gt(Question::getCreatedAt, LocalDateTime.now().minusDays(7));
        } else if ("monthly".equals(period)) {
            wrapper.gt(Question::getCreatedAt, LocalDateTime.now().minusDays(30));
        }

        wrapper.orderByDesc(Question::getViewCount, Question::getAnswerCount, Question::getVoteCount)
                .last("LIMIT " + lim);

        List<Question> questions = questionMapper.selectList(wrapper);

        // Fallback: if no questions in time range, return top questions regardless
        if (questions.isEmpty() && !"all".equals(period)) {
            LambdaQueryWrapper<Question> fallback = new LambdaQueryWrapper<>();
            fallback.eq(Question::getStatus, "published")
                    .orderByDesc(Question::getViewCount, Question::getAnswerCount, Question::getVoteCount)
                    .last("LIMIT " + lim);
            questions = questionMapper.selectList(fallback);
        }

        return questions.stream().map(this::buildQuestionVO).collect(Collectors.toList());
    }

    @Override
    public List<QuestionVO> getRelatedQuestions(Long questionId, int limit) {
        Question current = questionMapper.selectById(questionId);
        if (current == null) return List.of();

        // Get current question's tags
        List<QuestionTag> currentTags = questionTagMapper.selectList(
                new LambdaQueryWrapper<QuestionTag>().eq(QuestionTag::getQuestionId, questionId));
        List<Long> tagIds = currentTags.stream().map(QuestionTag::getTagId).toList();

        // Find questions with same category or overlapping tags
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getStatus, "published")
                .ne(Question::getId, questionId);

        if (tagIds.isEmpty()) {
            // No tags — just find same category
            wrapper.eq(Question::getCategoryId, current.getCategoryId());
        } else {
            // Find questions sharing tags or same category
            List<QuestionTag> relatedQT = questionTagMapper.selectList(
                    new LambdaQueryWrapper<QuestionTag>().in(QuestionTag::getTagId, tagIds));
            List<Long> relatedIds = relatedQT.stream()
                    .map(QuestionTag::getQuestionId)
                    .filter(id -> !id.equals(questionId))
                    .distinct()
                    .toList();

            if (relatedIds.isEmpty()) {
                wrapper.eq(Question::getCategoryId, current.getCategoryId());
            } else {
                wrapper.and(w -> w.in(Question::getId, relatedIds)
                        .or().eq(Question::getCategoryId, current.getCategoryId()));
            }
        }

        wrapper.orderByDesc(Question::getViewCount)
                .last("LIMIT " + Math.min(limit, 10));

        return questionMapper.selectList(wrapper).stream()
                .map(this::buildQuestionVO)
                .collect(Collectors.toList());
    }

    private QuestionVO buildQuestionVO(Question question) {
        if (question == null) {
            return null;
        }

        QuestionVO vo = new QuestionVO();
        vo.setId(question.getId());
        vo.setTitle(question.getTitle());
        vo.setContent(question.getContent());
        vo.setUserId(question.getUserId());
        vo.setCategoryId(question.getCategoryId());
        vo.setViewCount(question.getViewCount() != null ? question.getViewCount() : 0);
        vo.setAnswerCount(question.getAnswerCount() != null ? question.getAnswerCount() : 0);
        vo.setVoteCount(question.getVoteCount() != null ? question.getVoteCount() : 0);
        vo.setFavoriteCount(question.getFavoriteCount() != null ? question.getFavoriteCount() : 0);
        vo.setStatus(question.getStatus());
        vo.setIsPinned(question.getIsPinned());
        vo.setIsFeatured(question.getIsFeatured());
        vo.setAuditReason(question.getAuditReason());
        vo.setCreatedAt(question.getCreatedAt());
        vo.setUpdatedAt(question.getUpdatedAt());

        // Author info
        if (question.getUserId() != null) {
            User author = userMapper.selectById(question.getUserId());
            if (author != null) {
                vo.setUsername(author.getUsername());
                vo.setNickname(author.getNickname());
            }
        }

        // Category name
        if (question.getCategoryId() != null) {
            Category category = categoryMapper.selectById(question.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }

        // Tags
        List<QuestionTag> qtList = questionTagMapper.selectList(
                new LambdaQueryWrapper<QuestionTag>().eq(QuestionTag::getQuestionId, question.getId())
        );
        List<String> tagNames = new ArrayList<>();
        for (QuestionTag qt : qtList) {
            Tag tag = tagMapper.selectById(qt.getTagId());
            if (tag != null) {
                tagNames.add(tag.getName());
            }
        }
        vo.setTags(tagNames);

        return vo;
    }
}
