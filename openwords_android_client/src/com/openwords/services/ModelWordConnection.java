package com.openwords.services;

public class ModelWordConnection {

    private int connection_id, wordl1, wordl2, l2id;
    private String wordl1_text, wordl2_text, l2name, audio, trans;

    public ModelWordConnection(int connection_id, int wordl1, int wordl2, int l2id, String wordl1_text, String wordl2_text, String l2name, String audio, String trans) {
        this.connection_id = connection_id;
        this.wordl1 = wordl1;
        this.wordl2 = wordl2;
        this.l2id = l2id;
        this.wordl1_text = wordl1_text;
        this.wordl2_text = wordl2_text;
        this.l2name = l2name;
        this.audio = audio;
        this.trans = trans;
    }

    public int getConnectionId() {
        return connection_id;
    }

    public void setConnectionId(int connection_id) {
        this.connection_id = connection_id;
    }

    public int getWordl1() {
        return wordl1;
    }

    public void setWordl1(int wordl1) {
        this.wordl1 = wordl1;
    }

    public int getWordl2() {
        return wordl2;
    }

    public void setWordl2(int wordl2) {
        this.wordl2 = wordl2;
    }

    public int getL2id() {
        return l2id;
    }

    public void setL2id(int l2id) {
        this.l2id = l2id;
    }

    public String getWordl1Text() {
        return wordl1_text;
    }

    public void setWordl1Text(String wordl1_text) {
        this.wordl1_text = wordl1_text;
    }

    public String getWordl2Text() {
        return wordl2_text;
    }

    public void setWordl2Text(String wordl2_text) {
        this.wordl2_text = wordl2_text;
    }

    public String getL2name() {
        return l2name;
    }

    public void setL2name(String l2name) {
        this.l2name = l2name;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

}
