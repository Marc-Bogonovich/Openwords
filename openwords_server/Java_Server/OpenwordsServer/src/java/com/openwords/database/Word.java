package com.openwords.database;

import com.openwords.utils.MyMessageDigest;
import com.openwords.utils.MyXStream;
import com.openwords.utils.UtilLog;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "words")
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    public static List<Word> getConnectionsByEnglish(Session s, String word, String langInCode, String langOutCode) {
        Language langIn = (Language) s.createCriteria(Language.class).add(Restrictions.eq("code", langInCode)).list().get(0);
        Language langOut = (Language) s.createCriteria(Language.class).add(Restrictions.eq("code", langOutCode)).list().get(0);

        List<Integer> inputWordIds = s.createCriteria(Word.class)
                .add(Restrictions.eq("word", word))
                .add(Restrictions.eq("languageId", langIn.getLangId()))
                .setProjection(Projections.property("wordId"))
                .list();
        if (inputWordIds.isEmpty()) {
            return null;
        }
        UtilLog.logInfo(Word.class, "get word ids: " + word + " " + inputWordIds.toString());

        List<Integer> resultIds;
        if (langIn.getLangId() == 1) {//from English
            resultIds = s.createCriteria(WordConnection.class)
                    .add(Restrictions.in("wordOneId", inputWordIds))
                    .add(Restrictions.eq("wordOneLangId", 1))
                    .add(Restrictions.eq("wordTwoLangId", langOut.getLangId()))
                    .setProjection(Projections.property("wordTwoId"))
                    .list();
            UtilLog.logInfo(Word.class, "from English: " + resultIds.toString());
        } else if (langOut.getLangId() == 1) {//to English
            resultIds = s.createCriteria(WordConnection.class)
                    .add(Restrictions.in("wordTwoId", inputWordIds))
                    .add(Restrictions.eq("wordTwoLangId", langIn.getLangId()))
                    .add(Restrictions.eq("wordOneLangId", 1))
                    .setProjection(Projections.property("wordOneId"))
                    .list();
            UtilLog.logInfo(Word.class, "to English: " + resultIds.toString());
        } else {//no English
            List<Integer> engWordIds = s.createCriteria(WordConnection.class)
                    .add(Restrictions.in("wordTwoId", inputWordIds))
                    .add(Restrictions.eq("wordTwoLangId", langIn.getLangId()))
                    .add(Restrictions.eq("wordOneLangId", 1))
                    .setProjection(Projections.property("wordOneId"))
                    .list();
            resultIds = s.createCriteria(WordConnection.class)
                    .add(Restrictions.in("wordOneId", engWordIds))
                    .add(Restrictions.eq("wordOneLangId", 1))
                    .add(Restrictions.eq("wordTwoLangId", langOut.getLangId()))
                    .setProjection(Projections.property("wordTwoId"))
                    .list();
        }

        List<Word> wordsOut = s.createCriteria(Word.class)
                .add(Restrictions.in("wordId", resultIds))
                .list();
        return wordsOut;
    }

    public static List<Word> getSimilarWords(Session s, String form, int pageNumber, int pageSize) {
        int firstRecord = (pageNumber - 1) * pageSize;
        return s.createCriteria(Word.class)
                .add(Restrictions.like("word", form, MatchMode.ANYWHERE))
                .setFirstResult(firstRecord)
                .setMaxResults(pageSize)
                .list();
    }

    public static void increaseRank(Session s, Word word, String rankName) {
        WordMetaInfo meta = word.getWordMetaInfo();
        if (meta.getPopRank() == null) {
            meta.setPopRank(1);
        } else {
            meta.setPopRank(meta.getPopRank() + 1);
        }
        word.setMeta(meta.getXmlString());
        word.setUpdatedTime(new Date());
        s.beginTransaction().commit();
    }

    public static void increaseRank(Session s, List<Integer> wordIds, String rankName) {
        @SuppressWarnings("unchecked")
        List<Word> words = s.createCriteria(Word.class)
                .add(Restrictions.in("wordId", wordIds))
                .list();
        for (Word word : words) {
            WordMetaInfo meta = word.getWordMetaInfo();
            if (meta.getPopRank() == null) {
                meta.setPopRank(1);
            } else {
                meta.setPopRank(meta.getPopRank() + 1);
            }
            word.setMeta(meta.getXmlString());
            word.setUpdatedTime(new Date());
        }
        s.beginTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getWordIds(Session s, String word) {
        return s.createCriteria(Word.class)
                .add(Restrictions.eq("word", word))
                .setProjection(Projections.property("wordId")).list();
    }

    public static Word getWord(Session s, int wordId) {
        return (Word) s.get(Word.class, wordId);
    }

    @SuppressWarnings("unchecked")
    public static List<Word> getWords(Session s, Collection<Integer> ids) {
        return s.createCriteria(Word.class).add(Restrictions.in("wordId", ids)).list();
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
    public static List<Word> checkSameWord(Session s, String translation, int languageId, String word) throws NoSuchAlgorithmException {
        return s.createCriteria(Word.class)
                .add(Restrictions.eq("word", word))
                .add(Restrictions.eq("languageId", languageId))
                .add(Restrictions.eq("md5", MyMessageDigest.digest(translation.getBytes())))
                .list();
    }

    private int wordId, languageId;
    private String word, meta, contributor;
    private Date updatedTime;
    private byte[] md5;
    private WordMetaInfo wordMetaInfo;

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
    @JSON(serialize = false, deserialize = false)
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
    @JSON(serialize = false, deserialize = false)
    public byte[] getMd5() {
        return md5;
    }

    public void setMd5(byte[] md5) {
        this.md5 = md5;
    }

    @Transient
    public WordMetaInfo getWordMetaInfo() {
        if (wordMetaInfo == null) {
            wordMetaInfo = (WordMetaInfo) MyXStream.fromXml("word", meta);
        }
        return wordMetaInfo;
    }
}
