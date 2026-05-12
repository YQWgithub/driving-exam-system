package com.drivingexam.service;

import com.drivingexam.entity.WrongQuestion;

import java.util.List;
import java.util.Map;

public interface WrongQuestionService {
    int addWrongQuestion(WrongQuestion wrongQuestion);
    
    int deleteWrongQuestion(Integer id);
    
    int removeWrongQuestion(Integer userId, Integer questionId);
    
    int clearWrongQuestions(Integer userId);
    
    List<WrongQuestion> getWrongQuestionsByUserId(Integer userId);
    
    WrongQuestion getWrongQuestion(Integer userId, Integer questionId);
    
    int saveOrUpdateWrongQuestion(WrongQuestion wrongQuestion);
    
    List<Map<String, Object>> getWrongQuestionDetails(Integer userId);
}
