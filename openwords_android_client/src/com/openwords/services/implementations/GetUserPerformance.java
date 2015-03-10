package com.openwords.services.implementations;

import com.loopj.android.http.RequestParams;
import static com.openwords.model.DataPool.ServerAddress;
import com.openwords.model.Performance;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.gson.MyGson;
import java.util.List;

public class GetUserPerformance extends HttpServiceRequester implements HttpResultHandler {

    public final String ServiceURL = "http://" + ServerAddress + "/getUserPerformance";

    private HttpResultHandler resultHandler;

    public void doRequest(int userId, List<Integer> connections, HttpResultHandler hrh) {
        resultHandler = hrh;
        request(ServiceURL, new RequestParamsBuilder()
                .addParam("userId", String.valueOf(userId))
                .addParam("connections", MyGson.toJson(connections))
                .getParams(), 0, this);
    }

    @Override
    public void doRequest(RequestParams params, HttpResultHandler resultHandler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = (Result) MyGson.fromJson(jsonReply, Result.class);
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

        public List<Performance> result;
        public String errorMessage;
    }
}
