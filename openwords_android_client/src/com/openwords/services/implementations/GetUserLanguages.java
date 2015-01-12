package com.openwords.services.implementations;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import java.util.List;

public class GetUserLanguages extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = "http://192.168.1.109:8080/OpenwordsServer/getUserLanguages";

    private HttpResultHandler resultHandler;

    @Override
    public void doRequest(RequestParams params, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL, params, 0, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.result == null) {
            resultHandler.noResult("no result");
        } else {
            resultHandler.hasResult(r.result);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public List<Integer> result;
        public String errorMessage;
    }
}
