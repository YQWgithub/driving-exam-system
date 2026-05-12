package com.drivingexam.entity;

import java.io.Serializable;
import java.util.Date;

public class WrongQuestion implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer questionId;
    private String wrongAnswer;
    private String correctAnswer;
    private Date createTime;

    public WrongQuestion() {
    }

    public WrongQuestion(Integer userId, Integer questionId) {
        this.userId = userId;
        this.questionId = questionId;
        this.createTime = new Date();
    }

    public WrongQuestion(Integer userId, Integer questionId, String wrongAnswer) {
        this.userId = userId;
        this.questionId = questionId;
        this.wrongAnswer = wrongAnswer;
        this.createTime = new Date();
    }

    public WrongQuestion(Integer userId, Integer questionId, String wrongAnswer, String correctAnswer) {
        this.userId = userId;
        this.questionId = questionId;
        this.wrongAnswer = wrongAnswer;
        this.correctAnswer = correctAnswer;
        this.createTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(String wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
