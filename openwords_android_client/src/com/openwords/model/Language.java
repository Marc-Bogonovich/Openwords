package com.openwords.model;

import com.orm.SugarRecord;

public class Language extends SugarRecord<Language> {

    public int langId;
    public String name, code, meta, displayName;
    public boolean chosen;

    public Language() {
    }

    public Language(int langId, String name, String code, String meta, String displayName, boolean chosen) {
        this.langId = langId;
        this.name = name;
        this.code = code;
        this.meta = meta;
        this.displayName = displayName;
        this.chosen = chosen;
    }

}
