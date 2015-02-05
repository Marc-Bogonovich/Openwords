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

    public static void setUserLearningLanguages(Session s, int userId, int baseLang, int[] learningLangs) {
        @SuppressWarnings("unchecked")
        List<UserLanguage> all = s.createCriteria(UserLanguage.class)
                .add(Restrictions.eq("userId", userId))
                .add(Restrictions.eq("baseLang", baseLang)).list();

        for (UserLanguage lang : all) {
            lang.setUse(false);
        }

        for (int uselang : learningLangs) {
            boolean exist = false;
            for (UserLanguage oldlang : all) {
                if (uselang == oldlang.getLearningLang()) {
                    oldlang.setUse(true);
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                s.save(new UserLanguage(userId, baseLang, uselang, 0, "", true));
            }
        }
        s.beginTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public static List<UserLanguage> getUserLearningLanguages(Session s, int userId, int baseLang) {
        List<UserLanguage> ids = s.createCriteria(UserLanguage.class)
                .add(Restrictions.eq("userId", userId))
                .add(Restrictions.eq("baseLang", baseLang))
                .add(Restrictions.eq("use", true))
                .list();
        return ids;
    }

    private int userId, baseLang, learningLang, version;
    private String meta;
    private boolean use;

    public UserLanguage() {
    }

    public UserLanguage(int userId, int baseLang, int learningLang) {
        this.userId = userId;
        this.baseLang = baseLang;
        this.learningLang = learningLang;
    }

    public UserLanguage(int userId, int baseLang, int learningLang, int version, String meta, boolean use) {
        this.userId = userId;
        this.baseLang = baseLang;
        this.learningLang = learningLang;
        this.version = version;
        this.meta = meta;
        this.use = use;
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

    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Column(name = "meta_info")
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Column(name = "under_use")
    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

}
