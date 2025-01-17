package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.Performance;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import java.util.List;

public class ServiceGetWordConnectionsPack extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/getWordConnectionsPack";

    private HttpResultHandler resultHandler;

    public void doRequest(int userId, int langOneId, int langTwoId, int pageNumber, int pageSize, boolean doOrder, String orderBy, HttpResultHandler hrh) {
        resultHandler = hrh;
        request(ServiceURL, new RequestParamsBuilder()
                .addParam("userId", String.valueOf(userId))
                .addParam("langOneId", String.valueOf(langOneId))
                .addParam("langTwoId", String.valueOf(langTwoId))
                .addParam("pageNumber", String.valueOf(pageNumber))
                .addParam("pageSize", String.valueOf(pageSize))
                .addParam("doOrder", String.valueOf(doOrder))
                .addParam("orderBy", orderBy)
                .getParams(), 0, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.connections == null) {
            resultHandler.noResult("no result");
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
        public List<Performance> performance;
        public String errorMessage;
    }
}
