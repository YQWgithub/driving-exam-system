package com.drivingexam.controller;

import com.drivingexam.common.Result;
import com.drivingexam.entity.Category;
import com.drivingexam.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public Result<?> addCategory(@RequestBody Category category) {
        try {
            categoryService.addCategory(category);
            return Result.success("添加成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> deleteCategory(@PathVariable Integer id) {
        try {
            categoryService.deleteCategory(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> updateCategory(@RequestBody Category category) {
        try {
            categoryService.updateCategory(category);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info/{id}")
    public Result<Category> getCategoryInfo(@PathVariable Integer id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return Result.success(category);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<List<Category>> getCategoryList() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list/parent/{parentId}")
    public Result<List<Category>> getCategoriesByParentId(@PathVariable Integer parentId) {
        try {
            List<Category> categories = categoryService.getCategoriesByParentId(parentId);
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
