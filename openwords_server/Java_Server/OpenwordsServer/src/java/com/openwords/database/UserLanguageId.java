package com.openwords.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserLanguageId implements Serializable {

    private static final long serialVersionUID = 1L;
    private long userId;
    private int baseLang, learningLang;

    public UserLanguageId() {
    }

    public UserLanguageId(long userId, int baseLang, int learningLang) {
        this.userId = userId;
        this.baseLang = baseLang;
        this.learningLang = learningLang;
    }

    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "base_language")
    public int getBaseLang() {
        return baseLang;
    }

    public void setBaseLang(int baseLang) {
        this.baseLang = baseLang;
    }

    @Column(name = "learning_language")
    public int getLearningLang() {
        return learningLang;
    }

    public void setLearningLang(int learningLang) {
        this.learningLang = learningLang;
    }

}
