package com.drivingexam.service.impl;

import com.drivingexam.dao.CategoryMapper;
import com.drivingexam.entity.Category;
import com.drivingexam.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public int addCategory(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    public int deleteCategory(Integer id) {
        return categoryMapper.deleteById(id);
    }

    @Override
    public int updateCategory(Category category) {
        return categoryMapper.update(category);
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectAll();
    }

    @Override
    public List<Category> getCategoriesByParentId(Integer parentId) {
        return categoryMapper.selectByParentId(parentId);
    }
}
