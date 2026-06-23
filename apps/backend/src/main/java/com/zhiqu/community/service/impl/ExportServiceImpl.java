package com.zhiqu.community.service.impl;

import com.zhiqu.community.entity.Answer;
import com.zhiqu.community.entity.Question;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.mapper.AnswerMapper;
import com.zhiqu.community.mapper.QuestionMapper;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.service.ExportService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportServiceImpl implements ExportService {

    private final UserMapper userMapper;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;

    public ExportServiceImpl(UserMapper userMapper,
                             QuestionMapper questionMapper,
                             AnswerMapper answerMapper) {
        this.userMapper = userMapper;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
    }

    @Override
    public List<Map<String, Object>> exportUsers() {
        List<User> users = userMapper.selectList(null);
        List<Map<String, Object>> result = new ArrayList<>();
        for (User u : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("username", u.getUsername());
            map.put("nickname", u.getNickname());
            map.put("role", u.getRole());
            map.put("status", u.getStatus());
            map.put("exp", u.getExp());
            map.put("level", u.getLevel());
            map.put("createdAt", u.getCreatedAt());
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> exportQuestions() {
        List<Question> questions = questionMapper.selectList(null);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Question q : questions) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", q.getId());
            map.put("title", q.getTitle());
            map.put("userId", q.getUserId());
            map.put("categoryId", q.getCategoryId());
            map.put("viewCount", q.getViewCount());
            map.put("answerCount", q.getAnswerCount());
            map.put("voteCount", q.getVoteCount());
            map.put("favoriteCount", q.getFavoriteCount());
            map.put("status", q.getStatus());
            map.put("createdAt", q.getCreatedAt());
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> exportAnswers() {
        List<Answer> answers = answerMapper.selectList(null);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Answer a : answers) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", a.getId());
            map.put("questionId", a.getQuestionId());
            map.put("userId", a.getUserId());
            map.put("isAccepted", a.getIsAccepted());
            map.put("voteCount", a.getVoteCount());
            map.put("commentCount", a.getCommentCount());
            map.put("status", a.getStatus());
            map.put("createdAt", a.getCreatedAt());
            result.add(map);
        }
        return result;
    }
}
