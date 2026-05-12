package com.drivingexam.dao;

import com.drivingexam.entity.ExamPaper;

import java.util.List;

public interface ExamPaperMapper {
    int insert(ExamPaper examPaper);
    
    int deleteById(Integer id);
    
    int update(ExamPaper examPaper);
    
    ExamPaper selectById(Integer id);
    
    List<ExamPaper> selectAll();
}
