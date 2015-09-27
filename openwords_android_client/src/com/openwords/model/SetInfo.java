package com.openwords.model;

import com.openwords.services.implementations.ServiceGetSets;
import com.openwords.services.implementations.ServiceGetSets.Result;
import com.openwords.services.interfaces.HttpResultHandler;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class SetInfo extends SugarRecord<SetInfo> {

    public static void getAllSets(int pageNumber, int pageSize, final ResultWordSets resultHandler) {
        new ServiceGetSets().doRequest(pageNumber, pageSize, new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                Result r = (Result) resultObject;
                resultHandler.result(r.result);
            }

            public void noResult(String errorMessage) {
                resultHandler.result(null);
            }
        });
    }

    public long setId, userId;
    public int learningLang, setSize;
    public String name;
    public long updatedTimeLong;
    @Ignore
    public boolean isPlusButton;

    public SetInfo() {
    }

}
