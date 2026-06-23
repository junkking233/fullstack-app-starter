package com.zhiqu.community.service;

import java.util.List;
import java.util.Map;

public interface ExportService {

    List<Map<String, Object>> exportUsers();

    List<Map<String, Object>> exportQuestions();

    List<Map<String, Object>> exportAnswers();
}
