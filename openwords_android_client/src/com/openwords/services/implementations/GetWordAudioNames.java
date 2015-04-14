package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import com.openwords.model.WordAudio;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.gson.MyGson;
import java.util.Collection;

public class GetWordAudioNames extends HttpServiceRequester implements HttpResultHandler {

    public static final String ServiceURL = "http://" + ServerAddress + ":8888/api-v1/getWordAudioNames.php";

    private HttpResultHandler resultHandler;

    public void doRequest(Collection<Integer> wordIds, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("words", MyGson.toJson(wordIds))
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
