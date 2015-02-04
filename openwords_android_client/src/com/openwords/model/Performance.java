package com.openwords.model;

import com.orm.SugarRecord;

public class Performance extends SugarRecord<Performance> {

    public int wordConnectionId;
    public String learningType, performance;
    public long version;

    public Performance() {
    }

}
