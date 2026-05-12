package com.drivingexam.service;

import com.drivingexam.entity.AnswerDetail;
import com.drivingexam.entity.ExamRecord;

import java.util.List;
import java.util.Map;

public interface ExamRecordService {
    int createExamRecord(ExamRecord examRecord);
    
    int deleteExamRecord(Integer id);
    
    int updateExamRecord(ExamRecord examRecord);
    
    ExamRecord getExamRecordById(Integer id);

    List<ExamRecord> getAllExamRecords();

    List<ExamRecord> getExamRecordsByUserId(Integer userId);
    
    List<ExamRecord> getExamRecordsByUserIdAndStatus(Integer userId, Integer status);
    
    int submitExam(Integer examRecordId);
    
    int saveAnswer(AnswerDetail answerDetail);
    
    List<AnswerDetail> getAnswerDetailsByExamRecordId(Integer examRecordId);
    
    AnswerDetail getAnswerDetail(Integer examRecordId, Integer questionId);
    
    int calculateScore(Integer examRecordId);
    
    Map<String, Object> submitExamWithAnswers(Integer examRecordId, Integer userId, List<Map<String, Object>> answers);
}
