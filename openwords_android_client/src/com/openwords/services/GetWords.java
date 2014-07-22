package com.openwords.services;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.openwords.util.log.LogUtil;
import java.util.List;
import org.apache.http.Header;

/**
 *
 * @author hanaldo
 */
public class GetWords {

    public static final String URL_NEXTWORDS = "http://www.openwords.org/ServerPages/WordsDB/wordsPageGetWordList.php";

    /**
     * Get the words and connections between two languages?
     *
     * @param userId
     * @param langOne
     * @param langTwo
     * @param timeout The timeout for HTTP request in milliseconds, less or
     * equal to 0 will make it using the default value
     * @param callback
     */
    public static void request(String userId, String langOne, String langTwo, int timeout, final AsyncCallback callback) {
        LogUtil.logDeubg(GetWords.class, "user:" + userId + ", langOne:" + langOne + ", langTwo:" + langTwo);
        AsyncHttpClient http = new AsyncHttpClient();
        if (timeout > 0) {
            http.setTimeout(timeout);
        }
        RequestParams params = new RequestParams();
        params.put("user", userId);
        params.put("langOne", langOne);
        params.put("langTwo", langTwo);
        http.post(URL_NEXTWORDS, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, String string) {
                LogUtil.logDeubg(GetWords.class, "Result: " + string);
                Result r = new Gson().fromJson(string, Result.class);
                if (r.success == 1) {
                    callback.callback(r.data, null);
                } else {
                    onFailure(i, headers, string, new Exception(string));
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                callback.callback(null, thrwbl);
            }

        });
    }

    private GetWords() {
    }

    public interface AsyncCallback {

        public void callback(List<ModelWordConnection> data, Throwable error);
    }

    public class Result {

        public int success;
        public List<ModelWordConnection> data;

    }
}
