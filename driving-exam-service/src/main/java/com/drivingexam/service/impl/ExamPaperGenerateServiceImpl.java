package com.drivingexam.service.impl;

import com.drivingexam.entity.Question;
import com.drivingexam.service.ExamPaperGenerateService;
import com.drivingexam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamPaperGenerateServiceImpl implements ExamPaperGenerateService {

    @Autowired
    private QuestionService questionService;

    @Override
    public List<Question> generateExamPaper(Integer categoryId, int totalQuestions) {
        List<Question> allQuestions = questionService.getQuestionsByCategoryId(categoryId);
        
        if (allQuestions.size() < totalQuestions) {
            throw new RuntimeException("题库题目数量不足，当前只有" + allQuestions.size() + "道题");
        }
        
        return questionService.getRandomQuestions(categoryId, null, null, totalQuestions);
    }

    @Override
    public List<Question> generateExamPaperByConfig(Integer categoryId, 
                                                     int singleChoiceCount, 
                                                     int multipleChoiceCount, 
                                                     int judgmentCount) {
        List<Question> examPaper = new ArrayList<>();
        
        List<Question> singleChoiceQuestions = questionService.getRandomQuestions(
            categoryId, "单选题", null, singleChoiceCount);
        if (singleChoiceQuestions.size() < singleChoiceCount) {
            throw new RuntimeException("单选题数量不足，需要" + singleChoiceCount + "道，实际只有" + singleChoiceQuestions.size() + "道");
        }
        examPaper.addAll(singleChoiceQuestions);
        
        List<Question> multipleChoiceQuestions = questionService.getRandomQuestions(
            categoryId, "多选题", null, multipleChoiceCount);
        if (multipleChoiceQuestions.size() < multipleChoiceCount) {
            throw new RuntimeException("多选题数量不足，需要" + multipleChoiceCount + "道，实际只有" + multipleChoiceQuestions.size() + "道");
        }
        examPaper.addAll(multipleChoiceQuestions);
        
        List<Question> judgmentQuestions = questionService.getRandomQuestions(
            categoryId, "判断题", null, judgmentCount);
        if (judgmentQuestions.size() < judgmentCount) {
            throw new RuntimeException("判断题数量不足，需要" + judgmentCount + "道，实际只有" + judgmentQuestions.size() + "道");
        }
        examPaper.addAll(judgmentQuestions);
        
        return examPaper;
    }
}
