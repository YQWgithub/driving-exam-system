package com.drivingexam.service.impl;

import com.drivingexam.dao.WrongQuestionMapper;
import com.drivingexam.entity.WrongQuestion;
import com.drivingexam.service.WrongQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WrongQuestionServiceImpl implements WrongQuestionService {

    @Autowired
    private WrongQuestionMapper wrongQuestionMapper;

    @Override
    public int addWrongQuestion(WrongQuestion wrongQuestion) {
        WrongQuestion existing = wrongQuestionMapper.selectByUserIdAndQuestionId(
            wrongQuestion.getUserId(), wrongQuestion.getQuestionId());
        
        if (existing == null) {
            return wrongQuestionMapper.insert(wrongQuestion);
        }
        return 0;
    }

    @Override
    public int deleteWrongQuestion(Integer id) {
        return wrongQuestionMapper.deleteById(id);
    }

    @Override
    public int removeWrongQuestion(Integer userId, Integer questionId) {
        return wrongQuestionMapper.deleteByUserIdAndQuestionId(userId, questionId);
    }

    @Override
    public int clearWrongQuestions(Integer userId) {
        return wrongQuestionMapper.deleteByUserId(userId);
    }

    @Override
    public List<WrongQuestion> getWrongQuestionsByUserId(Integer userId) {
        return wrongQuestionMapper.selectByUserId(userId);
    }

    @Override
    public WrongQuestion getWrongQuestion(Integer userId, Integer questionId) {
        return wrongQuestionMapper.selectByUserIdAndQuestionId(userId, questionId);
    }

    @Override
    public int saveOrUpdateWrongQuestion(WrongQuestion wrongQuestion) {
        WrongQuestion existing = wrongQuestionMapper.selectByUserIdAndQuestionId(
            wrongQuestion.getUserId(), wrongQuestion.getQuestionId());
        
        if (existing == null) {
            return wrongQuestionMapper.insert(wrongQuestion);
        } else {
            return wrongQuestionMapper.updateWrongAnswer(wrongQuestion);
        }
    }

    @Override
    public List<Map<String, Object>> getWrongQuestionDetails(Integer userId) {
        return wrongQuestionMapper.selectDetailByUserId(userId);
    }
}
