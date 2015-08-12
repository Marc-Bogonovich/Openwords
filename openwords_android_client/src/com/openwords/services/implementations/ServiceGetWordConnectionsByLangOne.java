package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import java.util.List;

public class ServiceGetWordConnectionsByLangOne extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/getConnectionsByLangOne";

    private HttpResultHandler resultHandler;

    public void doRequest(String form, int langOneId, int langTwoId, int pageNumber, int pageSize, HttpResultHandler hrh) {
        resultHandler = hrh;
        request(ServiceURL, new RequestParamsBuilder()
                .addParam("form", String.valueOf(form))
                .addParam("langOneId", String.valueOf(langOneId))
                .addParam("langTwoId", String.valueOf(langTwoId))
                .addParam("pageNumber", String.valueOf(pageNumber))
                .addParam("pageSize", String.valueOf(pageSize))
                .getParams(), 30000, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.errorMessage != null) {
            resultHandler.noResult("no result: " + r.errorMessage);
        } else {
            resultHandler.hasResult(r);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public List<Word> words;
        public List<WordConnection> connections;
        public int total, pageNumber, pageSize;
        public String errorMessage;
    }
}
