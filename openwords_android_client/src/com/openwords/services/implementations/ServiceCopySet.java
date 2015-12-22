package com.openwords.services.implementations;

import com.google.gson.Gson;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.HttpServiceRequester;
import com.openwords.services.interfaces.RequestParamsBuilder;

public class ServiceCopySet extends HttpServiceRequester implements HttpResultHandler {
    
    public final String ServiceURL = ServiceProtocol + ServerAddress + "/copySet";
    
    private HttpResultHandler resultHandler;
    
    public void doRequest(long user, long targetSet, String name, HttpResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        request(ServiceURL,
                new RequestParamsBuilder()
                .addParam("user", String.valueOf(user))
                .addParam("targetSet", String.valueOf(targetSet))
                .addParam("name", name)
                .getParams(), 30 * 1000, this);
    }
    
    public void hasResult(Object resultObject) {
        String jsonReply = (String) resultObject;
        Result r = new Gson().fromJson(jsonReply, Result.class);
        if (r.errorMessage == null) {
            resultHandler.hasResult(null);
        } else {
            resultHandler.noResult(r.errorMessage);
        }
    }
    
    public void noResult(String errorMessage) {
        resultHandler.noResult(errorMessage);
    }
    
    public class Result {
        
        public String errorMessage;
    }
}
