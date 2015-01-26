package com.openwords.services.implementations;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.openwords.interfaces.HttpResultHandler;
import com.openwords.interfaces.InterfaceGetUserLanguages;
import static com.openwords.model.DataPool.ServerAddress;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import java.util.List;

public class GetUserLanguages extends HttpServiceRequester implements HttpResultHandler, InterfaceGetUserLanguages {

    public final String ServiceURL = "http://" + ServerAddress + servicePath;

    private HttpResultHandler resultHandler;

    public void doRequest(int userId, int langOneId, HttpResultHandler hrh) {
        resultHandler = hrh;
        request(ServiceURL, new RequestParamsBuilder()
                .addParam("userId", String.valueOf(userId))
                .addParam("langOneId", String.valueOf(langOneId))
                .getParams(), 0, this);
    }

    @Override
    public void doRequest(RequestParams params, HttpResultHandler resultHandler) {
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

    public void setUserId(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setLangOneId(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Integer> getResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class Result {

        public List<Integer> result;
        public String errorMessage;
    }
}
