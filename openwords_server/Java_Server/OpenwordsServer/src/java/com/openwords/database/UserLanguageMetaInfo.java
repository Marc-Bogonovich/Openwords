package com.openwords.database;

import com.openwords.utils.MyXStream;
import org.apache.struts2.json.annotations.JSON;

public class UserLanguageMetaInfo {

    private Integer modeSetPage;
    private Long modeSetId;

    public UserLanguageMetaInfo() {
    }

    public UserLanguageMetaInfo(Integer modeSetPage, Long modeSetId) {
        this.modeSetPage = modeSetPage;
        this.modeSetId = modeSetId;
    }

    public Integer getModeSetPage() {
        return modeSetPage;
    }

    public void setModeSetPage(Integer modeSetPage) {
        this.modeSetPage = modeSetPage;
    }

    public Long getModeSetId() {
        return modeSetId;
    }

    public void setModeSetId(Long modeSetId) {
        this.modeSetId = modeSetId;
    }

    @JSON(serialize = false, deserialize = false)
    public String getXmlString() {
        return MyXStream.toXml(this);
    }
}
