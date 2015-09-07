package com.openwords.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WordAudioId implements Serializable {

    private static final long serialVersionUID = 1L;
    private long wordId;
    private int type;

    public WordAudioId() {
    }

    public WordAudioId(long wordId, int type) {
        this.wordId = wordId;
        this.type = type;
    }

    @Column(name = "word_id")
    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
