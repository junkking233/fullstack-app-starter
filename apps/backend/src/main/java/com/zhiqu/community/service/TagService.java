package com.zhiqu.community.service;

import com.zhiqu.community.dto.CreateTagRequest;
import com.zhiqu.community.entity.Tag;

import java.util.List;

public interface TagService {

    List<Tag> listAll();

    Tag create(CreateTagRequest req, Long adminId);

    Tag update(Long id, String name);

    void delete(Long id);

    List<Tag> recommendTags(String keyword);
}
