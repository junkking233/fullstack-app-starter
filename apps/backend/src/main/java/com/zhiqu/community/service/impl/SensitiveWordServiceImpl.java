package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqu.community.dto.CreateSensitiveWordRequest;
import com.zhiqu.community.entity.SensitiveWord;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.SensitiveWordMapper;
import com.zhiqu.community.service.SensitiveWordService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {

    private final SensitiveWordMapper sensitiveWordMapper;

    private volatile List<String> cachedActiveWords = Collections.emptyList();

    public SensitiveWordServiceImpl(SensitiveWordMapper sensitiveWordMapper) {
        this.sensitiveWordMapper = sensitiveWordMapper;
    }

    @PostConstruct
    public void loadActiveWords() {
        refreshCache();
    }

    private void refreshCache() {
        List<SensitiveWord> activeWords = sensitiveWordMapper.selectActiveWords();
        List<String> words = new ArrayList<>();
        for (SensitiveWord sw : activeWords) {
            words.add(sw.getWord());
        }
        cachedActiveWords = Collections.unmodifiableList(words);
    }

    @Override
    public List<SensitiveWord> listAll() {
        return sensitiveWordMapper.selectList(
                new LambdaQueryWrapper<SensitiveWord>().orderByDesc(SensitiveWord::getCreatedAt)
        );
    }

    @Override
    public List<SensitiveWord> listActive() {
        return sensitiveWordMapper.selectList(
                new LambdaQueryWrapper<SensitiveWord>().eq(SensitiveWord::getStatus, "active")
        );
    }

    @Override
    public SensitiveWord create(CreateSensitiveWordRequest req, Long adminId) {
        SensitiveWord existing = sensitiveWordMapper.selectOne(
                new LambdaQueryWrapper<SensitiveWord>().eq(SensitiveWord::getWord, req.getWord())
        );
        if (existing != null) {
            throw new BusinessException(409, "敏感词已存在");
        }

        SensitiveWord sensitiveWord = new SensitiveWord();
        sensitiveWord.setWord(req.getWord());
        sensitiveWord.setStatus("active");
        sensitiveWord.setCreatedBy(adminId);
        sensitiveWord.setCreatedAt(LocalDateTime.now());

        sensitiveWordMapper.insert(sensitiveWord);
        refreshCache();
        return sensitiveWord;
    }

    @Override
    public void updateStatus(Long id, String status) {
        if (!"active".equals(status) && !"disabled".equals(status)) {
            throw new BusinessException(400, "状态值无效，只能是 active 或 disabled");
        }

        SensitiveWord sensitiveWord = sensitiveWordMapper.selectById(id);
        if (sensitiveWord == null) {
            throw new BusinessException(404, "敏感词不存在");
        }

        sensitiveWord.setStatus(status);
        sensitiveWordMapper.updateById(sensitiveWord);
        refreshCache();
    }

    @Override
    public void delete(Long id) {
        SensitiveWord sensitiveWord = sensitiveWordMapper.selectById(id);
        if (sensitiveWord == null) {
            throw new BusinessException(404, "敏感词不存在");
        }

        sensitiveWordMapper.deleteById(id);
        refreshCache();
    }

    @Override
    public SensitiveWord updateWord(Long id, String word) {
        SensitiveWord sensitiveWord = sensitiveWordMapper.selectById(id);
        if (sensitiveWord == null) {
            throw new BusinessException(404, "敏感词不存在");
        }

        SensitiveWord existing = sensitiveWordMapper.selectOne(
                new LambdaQueryWrapper<SensitiveWord>().eq(SensitiveWord::getWord, word)
        );
        if (existing != null && !existing.getId().equals(id)) {
            throw new BusinessException(409, "敏感词已存在");
        }

        sensitiveWord.setWord(word);
        sensitiveWordMapper.updateById(sensitiveWord);
        refreshCache();
        return sensitiveWord;
    }

    @Override
    public boolean containsSensitiveWord(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        String lower = text.toLowerCase();
        for (String word : cachedActiveWords) {
            if (lower.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
