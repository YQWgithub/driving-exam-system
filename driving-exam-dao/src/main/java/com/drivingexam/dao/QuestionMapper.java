package com.drivingexam.dao;

import com.drivingexam.entity.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionMapper {
    int insert(Question question);
    
    int deleteById(Integer id);
    
    int update(Question question);
    
    Question selectById(Integer id);
    
    List<Question> selectAll();
    
    List<Question> selectByCategoryId(Integer categoryId);
    
    List<Question> selectByType(String type);
    
    List<Question> selectByDifficulty(Integer difficulty);
    
    List<Question> selectByChapter(String chapter);
    
    List<Question> selectByKnowledgePoint(String knowledgePoint);
    
    List<Question> selectRandomQuestions(@Param("categoryId") Integer categoryId, 
                                        @Param("type") String type,
                                        @Param("difficulty") Integer difficulty,
                                        @Param("limit") Integer limit);
    
    int countByCondition(@Param("categoryId") Integer categoryId,
                        @Param("type") String type,
                        @Param("difficulty") Integer difficulty);
}
