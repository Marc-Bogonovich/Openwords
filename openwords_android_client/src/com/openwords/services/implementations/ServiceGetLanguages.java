package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.Language;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import java.util.Collection;
import java.util.List;

public class ServiceGetLanguages extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/getLanguages";

    private HttpResultHandler resultHandler;

    public void doRequest(Collection<Integer> langIds, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("include", new Gson().toJson(langIds))
                .getParams(), 30 * 1000, this);
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

        public List<Language> result;
        public String errorMessage;
    }
}
