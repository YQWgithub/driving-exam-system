package com.drivingexam.dao;

import com.drivingexam.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int insert(Category category);
    
    int deleteById(Integer id);
    
    int update(Category category);
    
    Category selectById(Integer id);
    
    List<Category> selectAll();
    
    List<Category> selectByParentId(Integer parentId);
}
