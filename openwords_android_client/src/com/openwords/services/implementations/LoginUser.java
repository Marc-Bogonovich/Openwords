package com.openwords.services.implementations;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;

public class LoginUser extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = "http://192.168.1.109:8080/OpenwordsServer/loginUser";

    private HttpResultHandler resultHandler;

    @Override
    public void doRequest(RequestParams params, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL, params, 0, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.result) {
            resultHandler.hasResult(r.userId);
        } else {
            resultHandler.noResult(r.errorMessage);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public boolean result;
        public int userId;
        public String errorMessage;
    }
}
