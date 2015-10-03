package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.SetItem;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.gson.MyGson;
import java.util.List;

public class ServiceUpdateSetItems extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/updateSetItems";

    private HttpResultHandler resultHandler;

    public void doRequest(long setId, long userId, List<Long> wordOneIds, List<Long> wordTwoIds, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("setId", String.valueOf(setId))
                .addParam("userId", String.valueOf(userId))
                .addParam("wordOneIds", MyGson.toJson(wordOneIds))
                .addParam("wordTwoIds", MyGson.toJson(wordTwoIds))
                .getParams(), 30 * 1000, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.itemsResult == null) {
            resultHandler.noResult("no result");
        } else {
            resultHandler.hasResult(r);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public List<SetItem> itemsResult;
        public String errorMessage;
    }
}
