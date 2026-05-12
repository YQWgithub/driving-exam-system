package com.drivingexam.service;

import com.drivingexam.entity.Category;

import java.util.List;

public interface CategoryService {
    int addCategory(Category category);
    
    int deleteCategory(Integer id);
    
    int updateCategory(Category category);
    
    Category getCategoryById(Integer id);
    
    List<Category> getAllCategories();
    
    List<Category> getCategoriesByParentId(Integer parentId);
}
