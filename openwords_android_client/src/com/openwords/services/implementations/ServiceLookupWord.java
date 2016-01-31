package com.openwords.services.implementations;

import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.Word;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.gson.MyGson;
import java.util.List;

public class ServiceLookupWord extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/lookupWord";

    private HttpResultHandler resultHandler;

    public void doRequest(int langId, String wordForm, int pageSize, boolean definition, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("lang", String.valueOf(langId))
                .addParam("wordForm", wordForm)
                .addParam("pageSize", String.valueOf(pageSize))
                .getParams(), 30 * 1000, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = MyGson.fromJson(jsonReply, Result.class);
        if (r.result == null) {
            resultHandler.noResult("no result");
        } else {
            resultHandler.hasResult(r);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public List<Word> result;
        public String errorMessage;
    }
}
