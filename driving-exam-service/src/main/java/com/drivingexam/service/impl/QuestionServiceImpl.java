package com.drivingexam.service.impl;

import com.drivingexam.dao.QuestionMapper;
import com.drivingexam.entity.Question;
import com.drivingexam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public int addQuestion(Question question) {
        return questionMapper.insert(question);
    }

    @Override
    public int deleteQuestion(Integer id) {
        return questionMapper.deleteById(id);
    }

    @Override
    public int updateQuestion(Question question) {
        return questionMapper.update(question);
    }

    @Override
    public Question getQuestionById(Integer id) {
        return questionMapper.selectById(id);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionMapper.selectAll();
    }

    @Override
    public List<Question> getQuestionsByCategoryId(Integer categoryId) {
        return questionMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<Question> getQuestionsByType(String type) {
        return questionMapper.selectByType(type);
    }

    @Override
    public List<Question> getQuestionsByDifficulty(Integer difficulty) {
        return questionMapper.selectByDifficulty(difficulty);
    }

    @Override
    public List<Question> getQuestionsByChapter(String chapter) {
        return questionMapper.selectByChapter(chapter);
    }

    @Override
    public List<Question> getQuestionsByKnowledgePoint(String knowledgePoint) {
        return questionMapper.selectByKnowledgePoint(knowledgePoint);
    }

    @Override
    public List<Question> getRandomQuestions(Integer categoryId, String type, Integer difficulty, Integer limit) {
        return questionMapper.selectRandomQuestions(categoryId, type, difficulty, limit);
    }

    @Override
    public int countQuestions(Integer categoryId, String type, Integer difficulty) {
        return questionMapper.countByCondition(categoryId, type, difficulty);
    }
}
