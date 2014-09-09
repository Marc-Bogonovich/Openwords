package com.openwords.services;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.openwords.model.WordAudio;
import org.apache.http.Header;

public class GetWordAudioNames {

    public static final String URL_GET_WORD_AUDIO_NAMES = "http://openwords.org/api-v1/getWordAudioNames.php";

    public static void request(int[] wordIds, int timeout, final AsyncCallback callback) {

        AsyncHttpClient http = new AsyncHttpClient();
        if (timeout > 0) {
            http.setTimeout(timeout);
        }
        RequestParams params = new RequestParams();
        params.put("words", new Gson().toJson(wordIds));
        http.post(URL_GET_WORD_AUDIO_NAMES, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                callback.callback(null, thrwbl);
            }

            @Override
            public void onSuccess(int i, Header[] headers, String string) {
                WordAudio[] names = new Gson().fromJson(string, WordAudio[].class);
                callback.callback(names, null);
            }
        });
    }

    private GetWordAudioNames() {
    }

    public interface AsyncCallback {

        public void callback(WordAudio[] names, Throwable error);
    }

}
