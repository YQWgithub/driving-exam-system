package com.drivingexam.service;

import com.drivingexam.entity.ExamPaper;

import java.util.List;

public interface ExamPaperService {
    int addExamPaper(ExamPaper examPaper);
    
    int deleteExamPaper(Integer id);
    
    int updateExamPaper(ExamPaper examPaper);
    
    ExamPaper getExamPaperById(Integer id);
    
    List<ExamPaper> getAllExamPapers();
}
