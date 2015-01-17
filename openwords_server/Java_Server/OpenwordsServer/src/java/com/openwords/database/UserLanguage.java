package com.openwords.database;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "user_languages")
public class UserLanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void setUserLearningLanguages(Session s, List<UserLanguage> userLangs) {
        int userId = userLangs.get(0).getUserId();
        int baseLang = userLangs.get(0).getBaseLang();
        String sqlDelete = "delete from user_languages where user_id=@user_id@ and base_language=@base_language@";
        sqlDelete = sqlDelete.replace("@user_id@", String.valueOf(userId))
                .replace("@base_language@", String.valueOf(baseLang));
        s.createSQLQuery(sqlDelete).executeUpdate();

        for (UserLanguage lang : userLangs) {
            s.save(lang);
        }
        s.beginTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getUserLearningLanguages(Session s, int userId, int baseLang) {
        List<Integer> ids = s.createCriteria(UserLanguage.class)
                .add(Restrictions.eq("userId", userId))
                .add(Restrictions.eq("baseLang", baseLang))
                .setProjection(Projections.property("learningLang"))
                .list();
        if (ids.isEmpty()) {
            return null;
        }
        return ids;
    }

    private int userId, baseLang, learningLang;

    public UserLanguage() {
    }

    public UserLanguage(int userId, int baseLang, int learningLang) {
        this.userId = userId;
        this.baseLang = baseLang;
        this.learningLang = learningLang;
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
    @Column(name = "base_language")
    public int getBaseLang() {
        return baseLang;
    }

    public void setBaseLang(int baseLang) {
        this.baseLang = baseLang;
    }

    @Id
    @Column(name = "learning_language")
    public int getLearningLang() {
        return learningLang;
    }

    public void setLearningLang(int learningLang) {
        this.learningLang = learningLang;
    }

}
