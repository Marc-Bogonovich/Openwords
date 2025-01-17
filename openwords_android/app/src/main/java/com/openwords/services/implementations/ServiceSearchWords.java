package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.Word;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServiceSearchWords extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = ServiceProtocol + ServerAddress + "/searchWords";

    private HttpResultHandler resultHandler;

    public void doRequest(int pageSize, int targetLang, int searchLang, String form, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("pageSize", String.valueOf(pageSize))
                .addParam("targetLang", String.valueOf(targetLang))
                .addParam("searchLang", String.valueOf(searchLang))
                .addParam("form", form)
                .getParams(), 30 * 1000, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.searchResult == null || r.targetResult == null) {
            resultHandler.noResult("No result: " + r.errorMessage);
        } else {
            resultHandler.hasResult(r);
        }
    }

    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }

    public class Result {

        public List<Word> targetResult;
        public List<Word> searchResult;
        public Map<Long, Set<Long>> linkedTargetWords;
        public Map<Long, Set<Long>> linkedSearchWords;
        public String errorMessage;
    }
}
