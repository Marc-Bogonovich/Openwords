package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.SetInfo;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;

public class ServiceSetWordSet extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/setWordSet";

    private HttpResultHandler resultHandler;

    public void doRequest(long setId, long userId, int nlang, int llang, String name, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("setId", String.valueOf(setId))
                .addParam("userId", String.valueOf(userId))
                .addParam("nlang", String.valueOf(nlang))
                .addParam("llang", String.valueOf(llang))
                .addParam("name", name)
                .getParams(), 30 * 1000, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.setResult == null) {
            resultHandler.noResult("Cannot update set! You may has entered an identical set name.");
        } else {
            resultHandler.hasResult(r);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public SetInfo setResult;
        public String errorMessage;
    }
}
