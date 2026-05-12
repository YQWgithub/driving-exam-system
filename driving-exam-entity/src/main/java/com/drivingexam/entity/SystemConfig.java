package com.drivingexam.entity;

import java.io.Serializable;
import java.util.Date;

public class SystemConfig implements Serializable {
    private Integer id;
    private String paramKey;
    private String paramValue;
    private String description;
    private Date createTime;

    public SystemConfig() {
    }

    public SystemConfig(String paramKey, String paramValue, String description) {
        this.paramKey = paramKey;
        this.paramValue = paramValue;
        this.description = description;
        this.createTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SystemConfig{" +
                "id=" + id +
                ", paramKey='" + paramKey + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
