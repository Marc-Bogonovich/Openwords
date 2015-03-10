package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.gson.MyGson;
import java.util.List;

public class SetUserLanguages extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = "http://" + ServerAddress + "/setUserLanguages";

    private HttpResultHandler resultHandler;

    public void doRequest(int userId, int baseLang, List<Integer> chosenLangIds, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("userId", String.valueOf(userId))
                .addParam("langOneId", String.valueOf(baseLang))
                .addParam("langTwoIds", MyGson.toJson(chosenLangIds))
                .getParams(), 0, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.result) {
            resultHandler.hasResult(null);
        } else {
            resultHandler.noResult("setUserLanguages failed");
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
