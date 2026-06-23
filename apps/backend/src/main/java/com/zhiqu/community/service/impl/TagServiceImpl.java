package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqu.community.dto.CreateTagRequest;
import com.zhiqu.community.entity.Tag;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.TagMapper;
import com.zhiqu.community.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public List<Tag> listAll() {
        return tagMapper.selectList(
                new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getUsageCount)
        );
    }

    @Override
    public Tag create(CreateTagRequest req, Long adminId) {
        Tag existing = tagMapper.selectOne(
                new LambdaQueryWrapper<Tag>().eq(Tag::getName, req.getName())
        );
        if (existing != null) {
            throw new BusinessException(409, "标签名称已存在");
        }

        Tag tag = new Tag();
        tag.setName(req.getName());
        tag.setUsageCount(0);
        tag.setCreatedBy(adminId);
        tag.setCreatedAt(LocalDateTime.now());

        tagMapper.insert(tag);
        return tag;
    }

    @Override
    public Tag update(Long id, String name) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new BusinessException(404, "标签不存在");
        }

        Tag existing = tagMapper.selectOne(
                new LambdaQueryWrapper<Tag>().eq(Tag::getName, name).ne(Tag::getId, id)
        );
        if (existing != null) {
            throw new BusinessException(409, "标签名称已存在");
        }

        tag.setName(name);
        tagMapper.updateById(tag);
        return tag;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new BusinessException(404, "标签不存在");
        }

        tagMapper.deleteById(id);
    }

    @Override
    public List<Tag> recommendTags(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return tagMapper.selectList(
                    new LambdaQueryWrapper<Tag>()
                            .orderByDesc(Tag::getUsageCount)
                            .last("LIMIT 5")
            );
        }
        return tagMapper.selectList(
                new LambdaQueryWrapper<Tag>()
                        .like(Tag::getName, keyword)
                        .orderByDesc(Tag::getUsageCount)
                        .last("LIMIT 5")
        );
    }
}
