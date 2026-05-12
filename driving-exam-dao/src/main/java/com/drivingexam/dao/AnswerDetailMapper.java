package com.drivingexam.dao;

import com.drivingexam.entity.AnswerDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnswerDetailMapper {
    int insert(AnswerDetail answerDetail);
    
    int deleteById(Integer id);
    
    int update(AnswerDetail answerDetail);
    
    AnswerDetail selectById(Integer id);
    
    List<AnswerDetail> selectByExamRecordId(Integer examRecordId);
    
    AnswerDetail selectByExamRecordIdAndQuestionId(@Param("examRecordId") Integer examRecordId, 
                                               @Param("questionId") Integer questionId);
}
