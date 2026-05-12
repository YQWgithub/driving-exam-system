package com.drivingexam.service;

import com.drivingexam.entity.Question;

import java.util.List;

public interface ExamPaperGenerateService {
    List<Question> generateExamPaper(Integer categoryId, int totalQuestions);
    
    List<Question> generateExamPaperByConfig(Integer categoryId, 
                                             int singleChoiceCount, 
                                             int multipleChoiceCount, 
                                             int judgmentCount);
}
