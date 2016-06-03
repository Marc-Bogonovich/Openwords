package com.openwords.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private long courseId, userId, updated;
    private String name, content, fileCover, langOne, langTwo;

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
    public String getLangOne() {
        return langOne;
    }

    public void setLangOne(String langOne) {
        this.langOne = langOne;
    }

    @Column(name = "language_two")
    public String getLangTwo() {
        return langTwo;
    }

    public void setLangTwo(String langTwo) {
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

    @Column(name = "file_cover")
    public String getFileCover() {
        return fileCover;
    }

    public void setFileCover(String fileCover) {
        this.fileCover = fileCover;
    }

    @Column(name = "updated_time")
    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

}
