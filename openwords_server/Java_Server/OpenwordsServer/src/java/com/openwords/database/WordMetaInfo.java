package com.openwords.database;

import com.openwords.utils.MyXStream;
import org.apache.struts2.json.annotations.JSON;

public class WordMetaInfo {

    private Integer commRank, popRank;
    private String commonTranslation, nativeTranslation, nativeForm;

    public WordMetaInfo() {
    }

    public WordMetaInfo(String commonTranslation, String nativeForm) {
        this.commonTranslation = commonTranslation;
        this.nativeForm = nativeForm;
    }

    public Integer getCommRank() {
        return commRank;
    }

    public void setCommRank(Integer commRank) {
        this.commRank = commRank;
    }

    public Integer getPopRank() {
        return popRank;
    }

    public void setPopRank(Integer popRank) {
        this.popRank = popRank;
    }

    public String getCommonTranslation() {
        return commonTranslation;
    }

    public void setCommonTranslation(String commonTranslation) {
        this.commonTranslation = commonTranslation;
    }

    public String getNativeTranslation() {
        return nativeTranslation;
    }

    public void setNativeTranslation(String nativeTranslation) {
        this.nativeTranslation = nativeTranslation;
    }

    public String getNativeForm() {
        return nativeForm;
    }

    public void setNativeForm(String nativeForm) {
        this.nativeForm = nativeForm;
    }

    @JSON(serialize = false, deserialize = false)
    public String getXmlString() {
        return MyXStream.toXml(this);
    }
}
