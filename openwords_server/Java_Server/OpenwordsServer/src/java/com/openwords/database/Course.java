package com.openwords.database;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "courses")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private long courseId, userId;
    private int langOne, langTwo;
    private String name, content;
    private Date updated;

    public Course() {
    }

    @Id
    @GeneratedValue
    @Column(name = "course_id")
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "language_one")
    public int getLangOne() {
        return langOne;
    }

    public void setLangOne(int langOne) {
        this.langOne = langOne;
    }

    @Column(name = "language_two")
    public int getLangTwo() {
        return langTwo;
    }

    public void setLangTwo(int langTwo) {
        this.langTwo = langTwo;
    }

    @Column(name = "course_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "updated_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
