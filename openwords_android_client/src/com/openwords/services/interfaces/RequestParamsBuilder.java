package com.openwords.services.interfaces;

import com.loopj.android.http.RequestParams;

public class RequestParamsBuilder {

    private RequestParams params;

    public RequestParamsBuilder() {
        params = new RequestParams();
    }

    public RequestParamsBuilder addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public RequestParams getParams() {
        return params;
    }

}
