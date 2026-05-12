package com.drivingexam.entity;

import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable {
    private Integer id;
    private String name;
    private Integer parentId;
    private Date createTime;

    public Category() {
    }

    public Category(String name, Integer parentId) {
        this.name = name;
        this.parentId = parentId;
        this.createTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", createTime=" + createTime +
                '}';
    }
}
