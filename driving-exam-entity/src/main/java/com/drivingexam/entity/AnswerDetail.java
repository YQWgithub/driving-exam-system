package com.drivingexam.entity;

import java.io.Serializable;

public class AnswerDetail implements Serializable {
    private Integer id;
    private Integer examRecordId;
    private Integer questionId;
    private String userAnswer;
    private Integer isCorrect;
    private Integer answerTime;

    public AnswerDetail() {
    }

    public AnswerDetail(Integer examRecordId, Integer questionId, String userAnswer, Integer isCorrect) {
        this.examRecordId = examRecordId;
        this.questionId = questionId;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
        this.answerTime = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExamRecordId() {
        return examRecordId;
    }

    public void setExamRecordId(Integer examRecordId) {
        this.examRecordId = examRecordId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public Integer getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Integer isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Integer answerTime) {
        this.answerTime = answerTime;
    }

    @Override
    public String toString() {
        return "AnswerDetail{" +
                "id=" + id +
                ", examRecordId=" + examRecordId +
                ", questionId=" + questionId +
                ", userAnswer='" + userAnswer + '\'' +
                ", isCorrect=" + isCorrect +
                ", answerTime=" + answerTime +
                '}';
    }
}
