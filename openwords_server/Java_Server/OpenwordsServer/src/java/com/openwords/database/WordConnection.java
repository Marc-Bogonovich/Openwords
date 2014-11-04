package com.openwords.database;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "word_connections")
public class WordConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void addConnection(Session s, WordConnection c) throws Exception {
        try {
            s.save(c);
            s.beginTransaction().commit();
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<WordConnection> getConnectionByIds(Session s, int wordOneId, int wordTwoId) {
        return s.createCriteria(WordConnection.class)
                .add(Restrictions.eq("wordOneId", wordOneId))
                .add(Restrictions.eq("wordTwoId", wordTwoId))
                .list();
    }

    private int connectionId, wordOneId, wordOneLangId, wordTwoId, wordTwoLangId, connectionType;
    private Date updatedTime;
    private String contributor;

    public WordConnection() {
    }

    public WordConnection(int wordOneId, int wordOneLangId, int wordTwoId, int wordTwoLangId, int connectionType, String contributor) {
        this.wordOneId = wordOneId;
        this.wordOneLangId = wordOneLangId;
        this.wordTwoId = wordTwoId;
        this.wordTwoLangId = wordTwoLangId;
        this.connectionType = connectionType;
        this.contributor = contributor;
    }

    @Id
    @GeneratedValue
    @Column(name = "connection_id")
    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    @Column(name = "word1_id")
    public int getWordOneId() {
        return wordOneId;
    }

    public void setWordOneId(int wordOneId) {
        this.wordOneId = wordOneId;
    }

    @Column(name = "word2_id")
    public int getWordTwoId() {
        return wordTwoId;
    }

    public void setWordTwoId(int wordTwoId) {
        this.wordTwoId = wordTwoId;
    }

    @Column(name = "connection_type")
    public int getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }

    @Column(name = "updated_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Column(name = "contributor_id")
    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    @Column(name = "word1_language")
    public int getWordOneLangId() {
        return wordOneLangId;
    }

    public void setWordOneLangId(int wordOneLangId) {
        this.wordOneLangId = wordOneLangId;
    }

    @Column(name = "word2_language")
    public int getWordTwoLangId() {
        return wordTwoLangId;
    }

    public void setWordTwoLangId(int wordTwoLangId) {
        this.wordTwoLangId = wordTwoLangId;
    }

}
