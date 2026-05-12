package com.drivingexam.service.impl;

import com.drivingexam.dao.AnswerDetailMapper;
import com.drivingexam.dao.ExamPaperMapper;
import com.drivingexam.dao.ExamRecordMapper;
import com.drivingexam.dao.QuestionMapper;
import com.drivingexam.dao.WrongQuestionMapper;
import com.drivingexam.entity.AnswerDetail;
import com.drivingexam.entity.ExamPaper;
import com.drivingexam.entity.ExamRecord;
import com.drivingexam.entity.Question;
import com.drivingexam.entity.WrongQuestion;
import com.drivingexam.service.ExamRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ExamRecordServiceImpl implements ExamRecordService {

    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Autowired
    private AnswerDetailMapper answerDetailMapper;

    @Autowired
    private ExamPaperMapper examPaperMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private WrongQuestionMapper wrongQuestionMapper;

    @Override
    public int createExamRecord(ExamRecord examRecord) {
        return examRecordMapper.insert(examRecord);
    }

    @Override
    public int deleteExamRecord(Integer id) {
        return examRecordMapper.deleteById(id);
    }

    @Override
    public int updateExamRecord(ExamRecord examRecord) {
        return examRecordMapper.update(examRecord);
    }

    @Override
    public ExamRecord getExamRecordById(Integer id) {
        return examRecordMapper.selectById(id);
    }

    @Override
    public List<ExamRecord> getAllExamRecords() {
        return examRecordMapper.selectAll();
    }

    @Override
    public List<ExamRecord> getExamRecordsByUserId(Integer userId) {
        return examRecordMapper.selectByUserId(userId);
    }

    @Override
    public List<ExamRecord> getExamRecordsByUserIdAndStatus(Integer userId, Integer status) {
        return examRecordMapper.selectByUserIdAndStatus(userId, status);
    }

    @Override
    public int submitExam(Integer examRecordId) {
        ExamRecord examRecord = examRecordMapper.selectById(examRecordId);
        if (examRecord == null) {
            throw new RuntimeException("Exam record does not exist");
        }

        examRecord.setEndTime(new Date());
        examRecord.setStatus(1);

        int score = calculateScore(examRecordId);
        ExamPaper examPaper = examPaperMapper.selectById(examRecord.getPaperId());
        int passScore = 90;
        if (examPaper != null && examPaper.getPassScore() != null) {
            passScore = examPaper.getPassScore();
        }

        examRecord.setScore(score);
        examRecord.setIsPass(score >= passScore ? 1 : 0);

        return examRecordMapper.update(examRecord);
    }

    @Override
    public int saveAnswer(AnswerDetail answerDetail) {
        AnswerDetail existing = answerDetailMapper.selectByExamRecordIdAndQuestionId(
                answerDetail.getExamRecordId(), answerDetail.getQuestionId());

        if (existing != null) {
            existing.setUserAnswer(answerDetail.getUserAnswer());
            existing.setIsCorrect(answerDetail.getIsCorrect());
            existing.setAnswerTime(answerDetail.getAnswerTime());
            return answerDetailMapper.update(existing);
        } else {
            return answerDetailMapper.insert(answerDetail);
        }
    }

    @Override
    public List<AnswerDetail> getAnswerDetailsByExamRecordId(Integer examRecordId) {
        return answerDetailMapper.selectByExamRecordId(examRecordId);
    }

    @Override
    public AnswerDetail getAnswerDetail(Integer examRecordId, Integer questionId) {
        return answerDetailMapper.selectByExamRecordIdAndQuestionId(examRecordId, questionId);
    }

    @Override
    public int calculateScore(Integer examRecordId) {
        List<AnswerDetail> answerDetails = answerDetailMapper.selectByExamRecordId(examRecordId);
        int totalScore = 0;

        for (AnswerDetail detail : answerDetails) {
            Question question = questionMapper.selectById(detail.getQuestionId());
            if (question != null && detail.getIsCorrect() == 1) {
                totalScore += 1;
            }
        }

        return totalScore;
    }

    @Override
    public Map<String, Object> submitExamWithAnswers(Integer examRecordId, Integer userId, List<Map<String, Object>> answers) {
        ExamRecord examRecord = examRecordMapper.selectById(examRecordId);
        if (examRecord == null) {
            throw new RuntimeException("考试记录不存在");
        }

        int correctCount = 0;
        int wrongCount = 0;
        int totalQuestions = answers != null ? answers.size() : 0;

        if (answers != null) {
            for (Map<String, Object> answer : answers) {
                Integer questionId = answer.get("questionId") != null 
                        ? ((Number) answer.get("questionId")).intValue() : null;
                String userAnswer = (String) answer.get("answer");

                if (questionId == null || userAnswer == null || userAnswer.isEmpty()) {
                    continue;
                }

                Question question = questionMapper.selectById(questionId);
                if (question == null) {
                    continue;
                }

                boolean isCorrect = userAnswer.equals(question.getAnswer());

                AnswerDetail answerDetail = new AnswerDetail();
                answerDetail.setExamRecordId(examRecordId);
                answerDetail.setQuestionId(questionId);
                answerDetail.setUserAnswer(userAnswer);
                answerDetail.setIsCorrect(isCorrect ? 1 : 0);
                answerDetail.setAnswerTime(0);
                answerDetailMapper.insert(answerDetail);

                if (isCorrect) {
                    correctCount++;
                } else {
                    wrongCount++;
                    if (wrongQuestionMapper != null) {
                        WrongQuestion wrongQuestion = new WrongQuestion();
                        wrongQuestion.setUserId(userId);
                        wrongQuestion.setQuestionId(questionId);
                        wrongQuestion.setWrongAnswer(userAnswer);
                        wrongQuestion.setCorrectAnswer(question.getAnswer());
                        wrongQuestion.setCreateTime(new Date());
                        
                        WrongQuestion existing = wrongQuestionMapper.selectByUserIdAndQuestionId(userId, questionId);
                        if (existing == null) {
                            wrongQuestionMapper.insert(wrongQuestion);
                        } else {
                            wrongQuestionMapper.updateWrongAnswer(wrongQuestion);
                        }
                    }
                }
            }
        }

        int score = totalQuestions > 0 ? (int) ((correctCount * 100.0) / totalQuestions) : 0;
        int isPass = score >= 90 ? 1 : 0;

        examRecord.setEndTime(new Date());
        examRecord.setScore(score);
        examRecord.setTotal(100);
        examRecord.setIsPass(isPass);
        examRecord.setStatus(1);
        examRecordMapper.update(examRecord);

        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("total", 100);
        result.put("correctCount", correctCount);
        result.put("wrongCount", wrongCount);
        result.put("totalQuestions", totalQuestions);
        result.put("isPass", isPass);
        result.put("examRecordId", examRecordId);

        return result;
    }
}
