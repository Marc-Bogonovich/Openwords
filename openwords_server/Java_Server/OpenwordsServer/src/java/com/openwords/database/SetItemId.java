package com.openwords.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SetItemId implements Serializable {

    private static final long serialVersionUID = 1L;

    private long setId, wordConnectionId;

    public SetItemId() {
    }

    public SetItemId(long wordConnectionId) {
        this.wordConnectionId = wordConnectionId;
    }

    public SetItemId(long setId, long wordConnectionId) {
        this.setId = setId;
        this.wordConnectionId = wordConnectionId;
    }

    @Column(name = "set_id")
    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    @Column(name = "word_connection_id")
    public long getWordConnectionId() {
        return wordConnectionId;
    }

    public void setWordConnectionId(long wordConnectionId) {
        this.wordConnectionId = wordConnectionId;
    }

}
