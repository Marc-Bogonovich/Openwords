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

    public static void changeLanguagePage(Session s, UserLanguageId id, int nextPage) throws Exception {
        UserLanguage ul = (UserLanguage) s.get(UserLanguage.class, id);

        if (ul == null) {
            throw new Exception("no such user language");
        } else {
            if (nextPage < 1) {
                throw new Exception("next page is too small");
            }
//            if (ul.getPage() >= nextPage) {
//                throw new Exception("next page is not larger than current page");
//            }
            ul.setPage(nextPage);
            s.beginTransaction().commit();
        }
    }

    public static void setUserLearningLanguages(Session s, long userId, int baseLang, int[] learningLangs) {
        @SuppressWarnings("unchecked")
        List<UserLanguage> all = s.createCriteria(UserLanguage.class)
                .add(Restrictions.eq("id.userId", userId))
                .add(Restrictions.eq("id.baseLang", baseLang)).list();

        for (int uselang : learningLangs) {
            boolean exist = false;
            for (UserLanguage oldlang : all) {
                if (uselang == oldlang.getId().getLearningLang()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                s.save(new UserLanguage(userId, baseLang, uselang, 1, ""));
            }
        }
        s.beginTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public static List<UserLanguage> getUserLearningLanguages(Session s, long userId, int baseLang) {
        List<UserLanguage> ids = s.createCriteria(UserLanguage.class)
                .add(Restrictions.eq("id.userId", userId))
                .add(Restrictions.eq("id.baseLang", baseLang))
                .list();
        return ids;
    }

    private UserLanguageId id;

    private int page;
    private String meta;
    private UserLanguageMetaInfo metaInfo;

    public UserLanguage() {
    }

    public UserLanguage(long userId, int baseLang, int learningLang) {
        this.id = new UserLanguageId(userId, baseLang, learningLang);
    }

    public UserLanguage(long userId, int baseLang, int learningLang, int page, String meta) {
        this.id = new UserLanguageId(userId, baseLang, learningLang);
        this.page = page;
        this.meta = meta;
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

    @Transient
    public int getBaseLang() {
        return id.getBaseLang();
    }

    @Transient
    public int getLearningLang() {
        return id.getLearningLang();
    }

//    @Transient
//    public UserLanguageMetaInfo getMetaInfo() {
//        if (metaInfo == null) {
//            metaInfo = (UserLanguageMetaInfo) MyXStream.fromXml(meta);
//        }
//        return metaInfo;
//    }
}
