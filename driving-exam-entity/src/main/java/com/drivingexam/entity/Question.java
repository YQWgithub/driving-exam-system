package com.drivingexam.entity;

import java.io.Serializable;
import java.util.Date;

public class Question implements Serializable {
    private Integer id;
    private Integer categoryId;
    private String type;
    private String content;
    private String imageUrl;
    private String options;
    private String answer;
    private String analysis;
    private Integer difficulty;
    private String chapter;
    private String knowledgePoint;
    private Date createTime;
    private Date updateTime;

    public Question() {
    }

    public Question(Integer categoryId, String type, String content, String options, 
                   String answer, String analysis, Integer difficulty, String chapter, String knowledgePoint) {
        this.categoryId = categoryId;
        this.type = type;
        this.content = content;
        this.options = options;
        this.answer = answer;
        this.analysis = analysis;
        this.difficulty = difficulty;
        this.chapter = chapter;
        this.knowledgePoint = knowledgePoint;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", options='" + options + '\'' +
                ", answer='" + answer + '\'' +
                ", analysis='" + analysis + '\'' +
                ", difficulty=" + difficulty +
                ", chapter='" + chapter + '\'' +
                ", knowledgePoint='" + knowledgePoint + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
