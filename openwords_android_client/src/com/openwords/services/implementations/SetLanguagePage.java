package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;

public class SetLanguagePage extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = "http://" + ServerAddress + "/setLanguagePage";

    private HttpResultHandler resultHandler;

    public void doRequest(int userId, int baseLang, int learningLang, int nextPage, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("userId", String.valueOf(userId))
                .addParam("langOneId", String.valueOf(baseLang))
                .addParam("langTwoId", String.valueOf(learningLang))
                .addParam("nextPage", String.valueOf(nextPage))
                .getParams(), 0, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.result) {
            resultHandler.hasResult(null);
        } else {
            resultHandler.noResult("setLanguagePage failed: " + r.errorMessage);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public boolean result;
        public String errorMessage;
    }
}
