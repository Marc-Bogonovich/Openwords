package com.openwords.services.implementations;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.openwords.interfaces.HttpResultHandler;
import com.openwords.interfaces.InterfaceGetLearnableLanguages;
import static com.openwords.model.DataPool.ServerAddress;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;
import java.util.List;

public class GetLearnableLanguages extends HttpServiceRequester implements HttpResultHandler, InterfaceGetLearnableLanguages {

    public final String ServiceURL = "http://" + ServerAddress + servicePath;

    private HttpResultHandler resultHandler;

    public void doRequest(int langOneId, HttpResultHandler hrh) {
        resultHandler = hrh;
        request(ServiceURL, new RequestParamsBuilder()
                .addParam("langOneId", String.valueOf(langOneId))
                .getParams(), 0, this);
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

    public void setLangOneId(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Integer> getResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doRequest(RequestParams params, HttpResultHandler resultHandler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class Result {

        public List<Integer> result;
        public String errorMessage;
    }
}
