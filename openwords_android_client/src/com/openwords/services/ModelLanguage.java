package com.openwords.services;

public class ModelLanguage {

    private int l2id;
    private String l2name;

    public ModelLanguage(int l2id, String l2name) {
        this.l2id = l2id;
        this.l2name = l2name;
    }

    public int getL2id() {
        return l2id;
    }

    public void setL2id(int l2id) {
        this.l2id = l2id;
    }

    public String getL2name() {
        return l2name;
    }

    public void setL2name(String l2name) {
        this.l2name = l2name;
    }

}
