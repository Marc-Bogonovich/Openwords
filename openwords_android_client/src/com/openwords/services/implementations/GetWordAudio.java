package com.openwords.services.implementations;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static com.openwords.model.DataPool.ServerAddress;
import java.io.File;
import java.util.Collection;
import org.apache.http.Header;

public class GetWordAudio {

    public static final String ServiceURL = "http://" + ServerAddress + ":8888/api-v1/getWordAudio.php";

    public static void request(Collection<Integer> wordIds, int timeout, final AsyncCallback callback, File file) {

        AsyncHttpClient http = new AsyncHttpClient();
        if (timeout > 0) {
            http.setTimeout(timeout);
        }
        RequestParams params = new RequestParams();
        params.put("words", new Gson().toJson(wordIds));
        http.post(ServiceURL, params, new FileAsyncHttpResponseHandler(file) {

            @Override
            public void onFailure(int i, Header[] headers, Throwable thrwbl, File file) {
                callback.callback(null, thrwbl);
            }

            @Override
            public void onSuccess(int i, Header[] headers, File file) {
                callback.callback(file, null);
            }
        });
    }

    private GetWordAudio() {
    }

    public interface AsyncCallback {

        public void callback(File file, Throwable error);
    }

}
