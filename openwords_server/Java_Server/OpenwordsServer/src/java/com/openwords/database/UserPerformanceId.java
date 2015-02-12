package com.openwords.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserPerformanceId implements Serializable {

    private static final long serialVersionUID = 1L;
    private int userId;
    private int wordConnectionId;
    private String learningType;

    public UserPerformanceId() {
    }

    public UserPerformanceId(int userId, int wordConnectionId, String learningType) {
        this.userId = userId;
        this.wordConnectionId = wordConnectionId;
        this.learningType = learningType;
    }

    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "word_connection_id")
    public int getWordConnectionId() {
        return wordConnectionId;
    }

    public void setWordConnectionId(int wordConnectionId) {
        this.wordConnectionId = wordConnectionId;
    }

    @Column(name = "learning_type")
    public String getLearningType() {
        return learningType;
    }

    public void setLearningType(String learningType) {
        this.learningType = learningType;
    }

}
