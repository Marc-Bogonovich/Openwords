package com.openwords.database;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "user_languages")
public class UserLanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void advanceLanguagePage(Session s, UserLanguageId id, int nextPage) throws Exception {
        UserLanguage ul = (UserLanguage) s.get(UserLanguage.class, id);

        if (ul == null) {
            throw new Exception("no such user language");
        } else {
            if (ul.getPage() >= nextPage) {
                throw new Exception("next page is not larger than current page");
            }
            if (!ul.isUse()) {
                throw new Exception("cannot advance user language that is not in use");
            }
            ul.setPage(nextPage);
            s.beginTransaction().commit();
        }
    }

    public static void setUserLearningLanguages(Session s, int userId, int baseLang, int[] learningLangs) {
        @SuppressWarnings("unchecked")
        List<UserLanguage> all = s.createCriteria(UserLanguage.class)
                .add(Restrictions.eq("id.userId", userId))
                .add(Restrictions.eq("id.baseLang", baseLang)).list();

        for (UserLanguage lang : all) {
            lang.setUse(false);
        }

        for (int uselang : learningLangs) {
            boolean exist = false;
            for (UserLanguage oldlang : all) {
                if (uselang == oldlang.getId().getLearningLang()) {
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
                .add(Restrictions.eq("id.userId", userId))
                .add(Restrictions.eq("id.baseLang", baseLang))
                .add(Restrictions.eq("use", true))
                .list();
        return ids;
    }

    private UserLanguageId id;

    private int page;
    private String meta;
    private boolean use;

    public UserLanguage() {
    }

    public UserLanguage(int userId, int baseLang, int learningLang) {
        this.id = new UserLanguageId(userId, baseLang, learningLang);
    }

    public UserLanguage(int userId, int baseLang, int learningLang, int page, String meta, boolean use) {
        this.id = new UserLanguageId(userId, baseLang, learningLang);
        this.page = page;
        this.meta = meta;
        this.use = use;
    }

    @Id
    @JSON(serialize = false, deserialize = false)
    public UserLanguageId getId() {
        return id;
    }

    public void setId(UserLanguageId id) {
        this.id = id;
    }

    @Column(name = "page")
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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

    @Transient
    public int getBaseLang() {
        return id.getBaseLang();
    }

    @Transient
    public int getLearningLang() {
        return id.getLearningLang();
    }
}
