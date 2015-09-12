package com.openwords.database;

import com.openwords.utils.MyXStream;
import org.apache.struts2.json.annotations.JSON;

public class SetMetaInfo {

    private String purpose;

    public SetMetaInfo() {
    }

    public SetMetaInfo(String purpose) {
        this.purpose = purpose;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @JSON(serialize = false, deserialize = false)
    public String getXmlString() {
        return MyXStream.toXml(this);
    }
}
