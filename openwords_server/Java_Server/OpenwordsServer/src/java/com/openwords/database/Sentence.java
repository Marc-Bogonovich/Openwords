package com.openwords.database;

import com.openwords.utils.MyXStream;
import com.openwords.utils.UtilLog;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "sentences")
public class Sentence implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void createSentence(Session s, Sentence sen) {
        sen.setUpdatedTime(new Date());
        s.save(sen);
        s.beginTransaction().commit();
    }

    public static void updateSentence(Session s, Sentence sen) {
        Sentence old = (Sentence) s.get(SetInfo.class, sen.getSentenceId());
        if (old.getUserId() != sen.getUserId()) {
            UtilLog.logWarn(Sentence.class, "user not own this sentence");
            return;
        }
        old.setMeta(sen.getMeta());
        old.setText(sen.getText());
        old.setUpdatedTime(new Date());
        s.beginTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public static List<Sentence> getSentences(Session s, Collection<Long> ids) {
        return s.createCriteria(Sentence.class)
                .add(Restrictions.in("sentenceId", ids)).list();
    }

    private long sentenceId, userId;
    private int languageId;
    private String text, meta;
    private Date updatedTime;
    private SentenceMetaInfo metaInfo;

    public Sentence() {
    }

    public Sentence(long sentenceId, int languageId, String text, String meta) {
        this.sentenceId = sentenceId;
        this.languageId = languageId;
        this.text = text;
        this.meta = meta;
    }

    @Id
    @Column(name = "sentence_id")
    public long getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(long sentenceId) {
        this.sentenceId = sentenceId;
    }

    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "language_id")
    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "meta_info")
    @JSON(serialize = false, deserialize = false)
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Column(name = "updated_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @JSON(serialize = false, deserialize = false)
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Transient
    public long getUpdatedTimeLong() {
        return updatedTime.getTime();
    }

    @Transient
    public SentenceMetaInfo getMetaInfo() {
        if (metaInfo == null && meta != null) {
            metaInfo = (SentenceMetaInfo) MyXStream.fromXml(meta);
        }
        return metaInfo;
    }
}
