package com.openwords.database;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "languages")
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public static List<Language> getLearnableLanguages(Session s) {
        return s.createCriteria(Language.class)
                .add(Restrictions.gt("totalConnections", 0))
                .list();
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getNewLanguageIds(Session s, Integer[] old) {
        Criteria c = s.createCriteria(Language.class);
        c.setProjection(Projections.property("langId"));
        if (old.length != 0) {
            c.add(Restrictions.not(Restrictions.in("langId", old)));
        }
        return c.list();
    }

    @SuppressWarnings("unchecked")
    public static List<Language> getAllLanguages(Session s) {
        return s.createCriteria(Language.class).list();
    }

    @SuppressWarnings("unchecked")
    public static List<Language> getLanguages(Session s, Integer[] langIds) {
        return s.createCriteria(Language.class)
                .add(Restrictions.in("langId", langIds))
                .list();
    }

    @SuppressWarnings("unchecked")
    public static List<Language> getLanguages(Session s, List<Integer> langIds) {
        return s.createCriteria(Language.class)
                .add(Restrictions.in("langId", langIds))
                .list();
    }

    private int langId, totalWords, totalConnections, totalSounds;
    private String name, code, meta, displayName;

    public Language() {
    }

    public Language(String name, String code, String meta, String displayName) {
        this.name = name;
        this.code = code;
        this.meta = meta;
        this.displayName = displayName;
    }

    @Id
    @GeneratedValue
    @Column(name = "language_id")
    public int getLangId() {
        return langId;
    }

    public void setLangId(int langId) {
        this.langId = langId;
    }

    @Column(name = "language")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "meta_info")
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Column(name = "display_name")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Column(name = "total_words")
    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    @Column(name = "total_connections")
    public int getTotalConnections() {
        return totalConnections;
    }

    public void setTotalConnections(int totalConnections) {
        this.totalConnections = totalConnections;
    }

    @Column(name = "total_sounds")
    public int getTotalSounds() {
        return totalSounds;
    }

    public void setTotalSounds(int totalSounds) {
        this.totalSounds = totalSounds;
    }
}
