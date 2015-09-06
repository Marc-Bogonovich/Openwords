package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.WordAudio;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.gson.MyGson;
import java.util.Collection;

public class GetWordAudioNames extends HttpServiceRequester implements HttpResultHandler {

    public static final String ServiceURL = ServiceProtocol + ServerAddress + "/getWordAudioInfo";

    private HttpResultHandler resultHandler;

    public void doRequest(Collection<Integer> wordIds, int language, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("wordIds", MyGson.toJson(wordIds))
                .addParam("type", String.valueOf(1))
                .addParam("language", String.valueOf(language))
                .getParams(), 0, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.result != null) {
            resultHandler.hasResult(r);
        } else {
            resultHandler.noResult("no result");
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public WordAudio[] result;
    }

}
