package com.openwords.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import java.util.List;
import org.apache.http.Header;

public class NewActivity extends Activity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        AsyncHttpClient http = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", "1");
        params.put("lang_id", "1");
        http.post("http://geographycontest.ipage.com/api-v1/getLanguages.php", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Gson g = new Gson();
                GetLanguages result = g.fromJson(responseBody, GetLanguages.class);
                StringBuilder sb = new StringBuilder(50);
                for (Language l : result.languages) {
                    sb.append(l.getTwoid()).append(":").append(l.getName()).append("\n");
                }

                Toast.makeText(NewActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String responseBody, Throwable error) {
                Toast.makeText(NewActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class GetLanguages {

        public List<Language> languages;

    }

}
