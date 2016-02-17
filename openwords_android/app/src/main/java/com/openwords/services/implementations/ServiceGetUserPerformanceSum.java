package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;

public class ServiceGetUserPerformanceSum extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/getUserPerformanceSum";

    private HttpResultHandler resultHandler;

    public void doRequest(int userId, int baseLang, int learningLang, HttpResultHandler hrh) {
        resultHandler = hrh;
        request(ServiceURL, new RequestParamsBuilder()
                .addParam("userId", String.valueOf(userId))
                .addParam("baseLang", String.valueOf(baseLang))
                .addParam("learningLang", String.valueOf(learningLang))
                .getParams(), 0, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.errorMessage != null) {
            resultHandler.noResult(r.errorMessage);
        } else {
            resultHandler.hasResult(r);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public int totalGood, total, totalVersion, totalWordsInLanguage;
        public String errorMessage;
    }
}
