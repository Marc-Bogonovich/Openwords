package com.openwords.database;

import com.openwords.utils.MyMessageDigest;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
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
    public static List<WordConnection> getConnectionsByMd5(Session s, String message) throws NoSuchAlgorithmException {
        return s.createCriteria(WordConnection.class).add(Restrictions.eq("md5", MyMessageDigest.digest(message.getBytes()))).list();
    }
    private int autoId, wordOneId, wordTwoId, connectionType;
    private Date updatedTime;
    private String contributorId;
    private byte[] md5;

    public WordConnection() {
    }

    public WordConnection(int wordOneId, int wordTwoId, int connectionType, Date updatedTime, String contributorId, byte[] md5) {
        this.wordOneId = wordOneId;
        this.wordTwoId = wordTwoId;
        this.connectionType = connectionType;
        this.updatedTime = updatedTime;
        this.contributorId = contributorId;
        this.md5 = md5;
    }

    @Id
    @GeneratedValue
    @Column(name = "auto_id")
    public int getAutoId() {
        return autoId;
    }

    public void setAutoId(int autoId) {
        this.autoId = autoId;
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
    public String getContributorId() {
        return contributorId;
    }

    public void setContributorId(String contributorId) {
        this.contributorId = contributorId;
    }

    @Column(name = "form_md5")
    public byte[] getMd5() {
        return md5;
    }

    public void setMd5(byte[] md5) {
        this.md5 = md5;
    }

}
