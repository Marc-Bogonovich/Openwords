package com.openwords.ui.lily.main;

import com.orm.dsl.Ignore;
import java.io.Serializable;

public class SetInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public long setId, userId;
    public int learningLang;
    public String name, meta;
    public long updatedTimeLong;
    @Ignore
    public boolean isPlusButton;

    public SetInfo() {
    }

}
