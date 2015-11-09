package com.openwords.database;

import com.openwords.utils.MyXStream;
import org.apache.struts2.json.annotations.JSON;

public class SentenceMetaInfo {

    private boolean hasSpace;

    public SentenceMetaInfo() {
    }

    public SentenceMetaInfo(boolean hasSpace) {
        this.hasSpace = hasSpace;
    }

    public boolean isHasSpace() {
        return hasSpace;
    }

    public void setHasSpace(boolean hasSpace) {
        this.hasSpace = hasSpace;
    }

    @JSON(serialize = false, deserialize = false)
    public String getXmlString() {
        return MyXStream.toXml(this);
    }
}
