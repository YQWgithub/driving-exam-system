package com.drivingexam.service;

import com.drivingexam.entity.Question;

import java.util.List;

public interface QuestionService {
    int addQuestion(Question question);
    
    int deleteQuestion(Integer id);
    
    int updateQuestion(Question question);
    
    Question getQuestionById(Integer id);
    
    List<Question> getAllQuestions();
    
    List<Question> getQuestionsByCategoryId(Integer categoryId);
    
    List<Question> getQuestionsByType(String type);
    
    List<Question> getQuestionsByDifficulty(Integer difficulty);
    
    List<Question> getQuestionsByChapter(String chapter);
    
    List<Question> getQuestionsByKnowledgePoint(String knowledgePoint);
    
    List<Question> getRandomQuestions(Integer categoryId, String type, Integer difficulty, Integer limit);
    
    int countQuestions(Integer categoryId, String type, Integer difficulty);
}
