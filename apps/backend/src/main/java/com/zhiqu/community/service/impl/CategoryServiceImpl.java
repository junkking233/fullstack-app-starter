package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqu.community.dto.CreateCategoryRequest;
import com.zhiqu.community.dto.UpdateCategoryRequest;
import com.zhiqu.community.entity.Category;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.CategoryMapper;
import com.zhiqu.community.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> tree() {
        List<Category> all = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getStatus, "active")
                        .orderByAsc(Category::getLevel, Category::getSortOrder)
        );
        return buildTree(all);
    }

    @Override
    public Category create(CreateCategoryRequest req) {
        Category category = new Category();
        category.setName(req.getName());

        if (req.getParentId() != null) {
            Category parent = categoryMapper.selectById(req.getParentId());
            if (parent == null) {
                throw new BusinessException(404, "父分类不存在");
            }
            category.setParentId(req.getParentId());
            category.setLevel(parent.getLevel() + 1);
        } else {
            category.setParentId(null);
            category.setLevel(1);
        }

        category.setSortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0);
        category.setQuestionCount(0);
        category.setStatus("active");
        category.setCreatedAt(LocalDateTime.now());

        categoryMapper.insert(category);
        return category;
    }

    @Override
    public Category update(Long id, UpdateCategoryRequest req) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }

        category.setName(req.getName());
        if (req.getSortOrder() != null) {
            category.setSortOrder(req.getSortOrder());
        }
        if (req.getStatus() != null) {
            category.setStatus(req.getStatus());
        }

        categoryMapper.updateById(category);
        return category;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }

        int count = categoryMapper.countQuestionsUnderCategory(id);
        if (count > 0) {
            throw new BusinessException(400, "该分类或其子分类下存在已发布问题，无法删除");
        }

        deleteChildrenRecursively(id);
        categoryMapper.deleteById(id);
    }

    @Override
    public Category getById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }
        return category;
    }

    private void deleteChildrenRecursively(Long parentId) {
        List<Category> children = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().eq(Category::getParentId, parentId)
        );
        for (Category child : children) {
            deleteChildrenRecursively(child.getId());
            categoryMapper.deleteById(child.getId());
        }
    }

    private List<Category> buildTree(List<Category> all) {
        Map<Long, List<Category>> parentIdMap = all.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        List<Category> roots = new ArrayList<>();
        for (Category category : all) {
            if (category.getParentId() == null) {
                roots.add(category);
            }
            List<Category> children = parentIdMap.get(category.getId());
            if (children != null) {
                category.setChildren(children);
            }
        }
        return roots;
    }
}
