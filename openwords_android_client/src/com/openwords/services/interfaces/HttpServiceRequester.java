package com.openwords.services.interfaces;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;

public abstract class HttpServiceRequester {

    public abstract void doRequest(RequestParams params, HttpResultHandler resultHandler);

    public void request(String serviceUrl, RequestParams params, int timeout, final HttpResultHandler callback) {
        AsyncHttpClient http = new AsyncHttpClient();
        if (timeout > 0) {
            http.setTimeout(timeout);
        }
        http.post(serviceUrl, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                callback.noResult(thrwbl.toString());
            }

            @Override
            public void onSuccess(int i, Header[] headers, String string) {
                callback.hasResult(string);
            }
        });
    }
}
