package com.drivingexam.entity;

import java.io.Serializable;
import java.util.Date;

public class ExamPaper implements Serializable {
    private Integer id;
    private String title;
    private Integer duration;
    private Integer totalScore;
    private Integer passScore;
    private Integer questionCount;
    private Date createTime;

    public ExamPaper() {
    }

    public ExamPaper(String title, Integer duration, Integer totalScore, Integer passScore, Integer questionCount) {
        this.title = title;
        this.duration = duration;
        this.totalScore = totalScore;
        this.passScore = passScore;
        this.questionCount = questionCount;
        this.createTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getPassScore() {
        return passScore;
    }

    public void setPassScore(Integer passScore) {
        this.passScore = passScore;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ExamPaper{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", totalScore=" + totalScore +
                ", passScore=" + passScore +
                ", questionCount=" + questionCount +
                ", createTime=" + createTime +
                '}';
    }
}
