package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.Sentence;
import com.openwords.model.SentenceConnection;
import com.openwords.model.SentenceItem;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import java.util.List;

public class ServiceGetSentencePack extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/getSentencePack";

    private HttpResultHandler resultHandler;

    public void doRequest(int langOne, int langTwo, int pageSize, HttpResultHandler hrh) {
        resultHandler = hrh;
        request(ServiceURL, new RequestParamsBuilder()
                .addParam("langOne", String.valueOf(langOne))
                .addParam("langTwo", String.valueOf(langTwo))
                .addParam("pageSize", String.valueOf(pageSize))
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

        public List<Sentence> sentences;
        public List<SentenceItem> items;
        public List<SentenceConnection> connections;
        public String errorMessage;
    }
}
