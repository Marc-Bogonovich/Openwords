package com.openwords.services.implementations;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static com.openwords.model.DataPool.ServerAddress;
import static com.openwords.model.DataPool.ServiceProtocol;
import com.openwords.model.LocalSettings;
import java.io.File;
import java.util.Collection;
import org.apache.http.Header;

public class GetWordAudio {

    public static final String ServiceURL = ServiceProtocol + ServerAddress + "/getWordAudioPack";

    public static void request(Collection<Integer> wordIds, int language, int timeout, final AsyncCallback callback, File file) {

        AsyncHttpClient http = new AsyncHttpClient();
        if (timeout > 0) {
            http.setTimeout(timeout);
        }
        RequestParams params = new RequestParams();
        params.put("wordIds", new Gson().toJson(wordIds));
        params.put("userId", String.valueOf(LocalSettings.getUserId()));
        params.put("type", String.valueOf(1));
        params.put("language", String.valueOf(language));
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
