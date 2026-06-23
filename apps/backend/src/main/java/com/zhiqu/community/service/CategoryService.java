package com.zhiqu.community.service;

import com.zhiqu.community.dto.CreateCategoryRequest;
import com.zhiqu.community.dto.UpdateCategoryRequest;
import com.zhiqu.community.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> tree();

    Category create(CreateCategoryRequest req);

    Category update(Long id, UpdateCategoryRequest req);

    void delete(Long id);

    Category getById(Long id);
}
