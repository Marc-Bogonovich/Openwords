package com.openwords.database;

import com.openwords.utils.MyMessageDigest;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "words")
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Map<String, Object> getWordFormAndTranslation(Session s, int wordId) {
        String sql = "select word, ExtractValue(meta_info,  \"/word/commonTranslation\") as common_translation from words where word_id=@wordId@";
        sql = sql.replace("@wordId@", String.valueOf(wordId));
        return DatabaseHandler.Query(sql, s).get(0);
    }

    public static int countLanguageWord(Session s, int languageId) {
        int total;
        if (languageId <= 0) {
            total = ((Number) s.createCriteria(Word.class)
                    .setProjection(Projections.rowCount()
                    ).uniqueResult()).intValue();
        } else {
            total = ((Number) s.createCriteria(Word.class)
                    .add(Restrictions.eq("languageId", languageId))
                    .setProjection(Projections.rowCount()
                    ).uniqueResult()).intValue();
        }
        return total;
    }

    public static void addWord(Session s, Word w) throws Exception {
        try {
            s.save(w);
            s.beginTransaction().commit();
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Word> getWordsWithSameCommonTranslation(Session s, String translation) throws NoSuchAlgorithmException {
        return s.createCriteria(Word.class).add(Restrictions.eq("md5", MyMessageDigest.digest(translation.getBytes())))
                .list();
    }

    private int wordId, languageId;
    private String word, meta, contributor;
    private Date updatedTime;
    private byte[] md5;

    public Word() {
    }

    public Word(int languageId, String word, String meta, String contributor, byte[] md5) {
        this.languageId = languageId;
        this.word = word;
        this.meta = meta;
        this.contributor = contributor;
        this.md5 = md5;
    }

    @Id
    @GeneratedValue
    @Column(name = "word_id")
    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    @Column(name = "language_id")
    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    @Column(name = "word")
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Column(name = "meta_info")
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Column(name = "contributor_id")
    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    @Column(name = "updated_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Column(name = "translation_md5")
    public byte[] getMd5() {
        return md5;
    }

    public void setMd5(byte[] md5) {
        this.md5 = md5;
    }

}
