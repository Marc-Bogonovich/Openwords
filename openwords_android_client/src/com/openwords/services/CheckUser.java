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
public class CheckUser {

    public static final String ServiceURL = "http://www.openwords.org/ServerPages/OpenwordsDB/validUser.php";

    public static void request(String username, String password, int timeout, final AsyncCallback callback) {

        AsyncHttpClient http = new AsyncHttpClient();
        if (timeout > 0) {
            http.setTimeout(timeout);
        }
        RequestParams params = new RequestParams();
        params.put("email", username);
        params.put("password", password);
        http.post(ServiceURL, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                callback.callback(-1, null, thrwbl);
            }

            @Override
            public void onSuccess(int i, Header[] headers, String string) {
                Result r = new Gson().fromJson(string, Result.class);
                if (r.success == 1) {
                    callback.callback(r.userid, r.message, null);
                } else {
                    callback.callback(-1, r.message, null);
                }
            }
        });
    }

    private CheckUser() {
    }

    public interface AsyncCallback {

        public void callback(int userId, String message, Throwable error);
    }

    public class Result {

        public int success, userid;
        public String message;
    }

}
