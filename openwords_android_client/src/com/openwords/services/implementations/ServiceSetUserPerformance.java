package com.openwords.services.implementations;

import static com.openwords.model.DataPool.ServerAddress;
import com.openwords.model.Performance;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.gson.MyGson;
import java.util.Collection;

public class ServiceSetUserPerformance extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = "http://" + ServerAddress + "/setUserPerformance";

    private HttpResultHandler resultHandler;

    public void doRequest(int userId, Collection<Performance> performance, String learningType, HttpResultHandler hrh) {
        resultHandler = hrh;
        Object[] perfs = new Object[performance.size() * 3];
        int index = 0;
        for (Performance perf : performance) {
            perfs[index * 3] = perf.wordConnectionId;
            perfs[index * 3 + 1] = perf.performance;
            perfs[index * 3 + 2] = perf.version;
            index += 1;
        }

        request(ServiceURL, new RequestParamsBuilder()
                .addParam("userId", String.valueOf(userId))
                .addParam("performance", MyGson.toJson(perfs))
                .getParams(), 0, this);
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = (Result) MyGson.fromJson(jsonReply, Result.class);
        if (r.result) {
            resultHandler.hasResult(null);
        } else {
            resultHandler.noResult("setUserPerformance failed");
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
