package com.openwords.database;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "user_languages")
public class UserLanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void setUserLearningLanguages(Session s, List<UserLanguage> langTwos) {
        int userId = langTwos.get(0).getUserId();
        int langOne = langTwos.get(0).getLangOne();
        String sqlDelete = "delete from user_languages where user_id=@user_id@ and lang_one_id=@lang_one_id@";
        sqlDelete = sqlDelete.replace("@user_id@", String.valueOf(userId))
                .replace("@lang_one_id@", String.valueOf(langOne));
        s.createSQLQuery(sqlDelete).executeUpdate();

        for (UserLanguage lang : langTwos) {
            s.save(lang);
        }
        s.beginTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public static List<UserLanguage> getUserLearningLanguages(Session s, int userId, int langOne) {
        return s.createCriteria(UserLanguage.class)
                .add(Restrictions.eq("userId", userId))
                .add(Restrictions.eq("langOne", langOne))
                .list();
    }

    private int userId, langOne, langTwo;

    public UserLanguage() {
    }

    public UserLanguage(int userId, int langOne, int langTwo) {
        this.userId = userId;
        this.langOne = langOne;
        this.langTwo = langTwo;
    }

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "lang_one_id")
    public int getLangOne() {
        return langOne;
    }

    public void setLangOne(int langOne) {
        this.langOne = langOne;
    }

    @Id
    @Column(name = "lang_two_id")
    public int getLangTwo() {
        return langTwo;
    }

    public void setLangTwo(int langTwo) {
        this.langTwo = langTwo;
    }

}
