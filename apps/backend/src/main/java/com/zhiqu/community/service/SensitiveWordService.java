package com.zhiqu.community.service;

import com.zhiqu.community.dto.CreateSensitiveWordRequest;
import com.zhiqu.community.entity.SensitiveWord;

import java.util.List;

public interface SensitiveWordService {

    List<SensitiveWord> listAll();

    List<SensitiveWord> listActive();

    SensitiveWord create(CreateSensitiveWordRequest req, Long adminId);

    void updateStatus(Long id, String status);

    void delete(Long id);

    SensitiveWord updateWord(Long id, String word);

    boolean containsSensitiveWord(String text);
}
