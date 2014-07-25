package com.openwords.services;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;

/**
 *
 * @author hanaldo
 */
public class SetUserWords {

    public static final String URL_SET_USER_WORDS = "http://www.openwords.org/ServerPages/OpenwordsDB/setUserWords.php";

    public static void request(String connectionIds, String dTime, String userId, String langTwoId, int timeout, final AsyncCallback callback) {

        AsyncHttpClient http = new AsyncHttpClient();
        if (timeout > 0) {
            http.setTimeout(timeout);
        }
        RequestParams params = new RequestParams();
        params.put("conid", connectionIds);
        params.put("dtime", dTime);
        params.put("user", userId);
        params.put("lTwo", langTwoId);
        http.post(URL_SET_USER_WORDS, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                callback.callback(null, thrwbl);
            }

            @Override
            public void onSuccess(int i, Header[] headers, String string) {
                Result r = new Gson().fromJson(string, Result.class);
                if (r.success == 1) {
                    callback.callback(r.message, null);
                } else {
                    callback.callback(null, new Exception("SetUserWords Failed"));
                }
            }
        });
    }

    private SetUserWords() {
    }

    public interface AsyncCallback {

        public void callback(String message, Throwable error);
    }

    public class Result {

        public int success;
        public String message;
    }

}
