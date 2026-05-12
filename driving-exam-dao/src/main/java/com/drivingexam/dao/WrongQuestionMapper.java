package com.drivingexam.dao;

import com.drivingexam.entity.WrongQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WrongQuestionMapper {
    int insert(WrongQuestion wrongQuestion);
    
    int deleteById(Integer id);
    
    int deleteByUserIdAndQuestionId(@Param("userId") Integer userId, @Param("questionId") Integer questionId);
    
    int deleteByUserId(Integer userId);
    
    int updateWrongAnswer(WrongQuestion wrongQuestion);
    
    List<WrongQuestion> selectByUserId(Integer userId);
    
    WrongQuestion selectByUserIdAndQuestionId(@Param("userId") Integer userId, @Param("questionId") Integer questionId);
    
    List<Map<String, Object>> selectDetailByUserId(Integer userId);
}
