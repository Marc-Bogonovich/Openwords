package com.openwords.services;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import java.util.List;
import org.apache.http.Header;

public class GetLanguages {

    public static final String URL_DROPDOWN = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/homePageChooseLanguage.php";

    public static void request(String userId, int timeout, final AsyncCallback callback) {
        AsyncHttpClient http = new AsyncHttpClient();
        if (timeout > 0) {
            http.setTimeout(timeout);
        }
        RequestParams params = new RequestParams();
        params.put("userid", userId);
        http.post(URL_DROPDOWN, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, String string) {
                Result r = new Gson().fromJson(string, Result.class);
                callback.callback(r.language, null);
            }

            @Override
            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                callback.callback(null, thrwbl);
            }

        });
    }

    private GetLanguages() {
    }

    public interface AsyncCallback {

        public void callback(List<ModelLanguage> languages, Throwable error);
    }

    public class Result {

        public List<ModelLanguage> language;
    }
}
