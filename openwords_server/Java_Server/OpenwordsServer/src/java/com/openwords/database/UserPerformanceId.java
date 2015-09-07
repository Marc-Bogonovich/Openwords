package com.openwords.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserPerformanceId implements Serializable {

    private static final long serialVersionUID = 1L;
    private long userId;
    private long wordConnectionId;
    private String learningType;

    public UserPerformanceId() {
    }

    public UserPerformanceId(long userId, long wordConnectionId, String learningType) {
        this.userId = userId;
        this.wordConnectionId = wordConnectionId;
        this.learningType = learningType;
    }

    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "word_connection_id")
    public long getWordConnectionId() {
        return wordConnectionId;
    }

    public void setWordConnectionId(long wordConnectionId) {
        this.wordConnectionId = wordConnectionId;
    }

    @Column(name = "learning_type")
    public String getLearningType() {
        return learningType;
    }

    public void setLearningType(String learningType) {
        this.learningType = learningType;
    }

    @Override
    public String toString() {
        return "[" + userId + "," + wordConnectionId + "," + learningType + "]";
    }
}
