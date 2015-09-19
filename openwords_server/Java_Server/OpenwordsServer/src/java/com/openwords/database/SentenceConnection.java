package com.openwords.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sentence_connections")
public class SentenceConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    private long uniId, sentenceId;
    private String type;

    public SentenceConnection() {
    }

    public SentenceConnection(long uniId, long sentenceId, String type) {
        this.uniId = uniId;
        this.sentenceId = sentenceId;
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
}
