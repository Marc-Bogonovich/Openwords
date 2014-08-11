package com.openwords.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.openwords.R;
import com.openwords.model.UserWords;
import com.openwords.ui.common.DialogForHTTP;
import com.openwords.util.log.LogUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;

/**
 *
 * @author hanaldo
 */
public class ActivityTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.act_test_test1).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                DialogForHTTP d = new DialogForHTTP(ActivityTest.this,
                        "Downloading Language Pack",
                        "Retriving something, can be shown in percentage",
                        "http://156.56.92.178:8888/getWordsList.php",
                        5000,
                        new RequestParams("languageId", "1"),
                        new DialogForHTTP.OnSuccess() {

                            public void onSuccess(int i, Header[] headers, String string) {
                                Toast.makeText(ActivityTest.this, "onSuccess: " + string, Toast.LENGTH_SHORT).show();
                                LogUtil.logDeubg(this, "onSuccess: " + string);
                            }
                        },
                        new DialogForHTTP.OnFailure() {

                            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                                Toast.makeText(ActivityTest.this, "onFailure: " + thrwbl.toString(), Toast.LENGTH_SHORT).show();
                                LogUtil.logDeubg(this, "onFailure: " + thrwbl.toString());
                            }
                        });
                d.start();
            }
        });

        findViewById(R.id.act_test_test2).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                List<UserWords> existList = UserWords.findByLanguage(2);
                Toast.makeText(ActivityTest.this, new Gson().toJson(existList), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.act_test_test3).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                AsyncHttpClient http = new AsyncHttpClient();
                http.get("http://www.google.com", new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, String string) {
                        LogUtil.logDeubg(ActivityTest.this, string);
                        Toast.makeText(ActivityTest.this, string.substring(0, 3), Toast.LENGTH_SHORT).show();
                        final String t = string;
                        AsyncHttpClient http2 = new AsyncHttpClient();
                        http2.get("http://www.google.com", new TextHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, String string) {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(ActivityTest.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                LogUtil.logDeubg(ActivityTest.this, "------------- new --------");
                                Toast.makeText(ActivityTest.this, "again", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {

                            }

                        });
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {

                    }

                });
            }
        });

        findViewById(R.id.act_test_test4).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(ActivityTest.this, DialogSoundPlay.class));

            }
        });

        findViewById(R.id.act_test_test5).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(ActivityTest.this, DialogSoundPlay2.class));
            }
        });

        findViewById(R.id.act_test_test6).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(ActivityTest.this, ActivityTestAutofix.class));
            }
        });
    }
}
