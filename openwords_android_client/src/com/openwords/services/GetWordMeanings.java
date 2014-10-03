package com.openwords.services;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.openwords.model.WordMeaning;
import org.apache.http.Header;

public class GetWordMeanings {

    public static final String ServiceURL = "http://openwords.org/api-v1/getWordMeanings.php";

    public static void request(int[] wordIds, int type, int timeout, final AsyncCallback callback) {

        AsyncHttpClient http = new AsyncHttpClient();
        if (timeout > 0) {
            http.setTimeout(timeout);
        }
        RequestParams params = new RequestParams();
        params.put("words", new Gson().toJson(wordIds));
        params.put("type", type);
        http.post(ServiceURL, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                callback.callback(null, thrwbl);
            }

            @Override
            public void onSuccess(int i, Header[] headers, String string) {
                WordMeaning[] meanings = new Gson().fromJson(string, WordMeaning[].class);
                callback.callback(meanings, null);
            }
        });
    }

    private GetWordMeanings() {
    }

    public interface AsyncCallback {

        public void callback(WordMeaning[] meanings, Throwable error);
    }

}
