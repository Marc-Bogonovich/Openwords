package com.openwords.model;

import com.openwords.services.implementations.ServiceGetSets;
import com.openwords.services.interfaces.HttpResultHandler;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class SetInfo extends SugarRecord<SetInfo> {

    public static void getAllSets(int pageNumber, int pageSize, final ResultWordSets resultHandler) {
        new ServiceGetSets().doRequest(pageNumber, pageSize, new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                ServiceGetSets.Result r = (ServiceGetSets.Result) resultObject;
                resultHandler.result(r.result);
            }

            public void noResult(String errorMessage) {
                resultHandler.result(null);
            }
        });
    }

    public long setId, userId;
    public int nativeLang, learningLang, setSize;
    public String name;
    public long updatedTimeLong;
    @Ignore
    public boolean isPlusButton;

    public SetInfo() {
    }

    public SetInfo(String name, boolean isPlusButton) {
        this.name = name;
        this.isPlusButton = isPlusButton;
    }

}
