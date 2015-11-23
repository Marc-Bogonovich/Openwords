package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.SetInfo;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import java.util.List;

public class ServiceGetSets extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/getSets";

    private HttpResultHandler resultHandler;

    public void doRequest(int pageNumber, int pageSize, int langOne, int langTwo, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("pageNumber", String.valueOf(pageNumber))
                .addParam("pageSize", String.valueOf(pageSize))
                .addParam("langOne", String.valueOf(langOne))
                .addParam("langTwo", String.valueOf(langTwo))
                .getParams(), 30 * 1000, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.result == null) {
            resultHandler.noResult("no result");
        } else {
            resultHandler.hasResult(r);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public List<SetInfo> result;
        public String errorMessage;
    }
}
