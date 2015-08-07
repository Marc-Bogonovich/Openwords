package com.openwords.database;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "word_audiocall")
public class WordAudio implements Serializable {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public static List<WordAudio> getAudioByIds(Session s, Integer[] ids, int type, int language) {
        return s.createCriteria(WordAudio.class)
                .add(Restrictions.eq("id.type", type))
                .add(Restrictions.eq("languageId", language))
                .add(Restrictions.in("id.wordId", ids)).list();
    }

    private WordAudioId id;
    private int languageId;
    private String url;

    public WordAudio() {
    }

    @Id
    public WordAudioId getId() {
        return id;
    }

    public void setId(WordAudioId id) {
        this.id = id;
    }

    @Column(name = "language_id")
    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
