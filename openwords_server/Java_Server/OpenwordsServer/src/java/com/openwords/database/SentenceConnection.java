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
@Table(name = "sentence_connections")
public class SentenceConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    public static int count(Session s, int langTwo) {
        return ((Number) s.createCriteria(SentenceConnection.class)
                .add(Restrictions.eq("langTwo", langTwo))
                .setProjection(Projections.rowCount())
                .uniqueResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    public static List<SentenceConnection> getConnectionPage(Session s, int pageNumber, int pageSize, int langTwo) {
        int firstRecord = (pageNumber - 1) * pageSize;
        return s.createCriteria(SentenceConnection.class)
                .add(Restrictions.eq("langTwo", langTwo))
                .setFirstResult(firstRecord)
                .setMaxResults(pageSize)
                .list();
    }

    private long uniId, sentenceId;
    private int langTwo;
    private String type;

    public SentenceConnection() {
    }

    public SentenceConnection(long uniId, long sentenceId, int langTwo, String type) {
        this.uniId = uniId;
        this.sentenceId = sentenceId;
        this.langTwo = langTwo;
        this.type = type;
    }

    @Id
    @Column(name = "uni_sentence_id")
    public long getUniId() {
        return uniId;
    }

    public void setUniId(long uniId) {
        this.uniId = uniId;
    }

    @Id
    @Column(name = "sentence_id")
    public long getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(long sentenceId) {
        this.sentenceId = sentenceId;
    }

    @Column(name = "connection_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Id
    @Column(name = "lang_two")
    public int getLangTwo() {
        return langTwo;
    }

    public void setLangTwo(int langTwo) {
        this.langTwo = langTwo;
    }

}
