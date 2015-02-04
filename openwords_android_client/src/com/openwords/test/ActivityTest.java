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
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.UserLearningLanguages;
import com.openwords.model.UserWords;
import com.openwords.services.implementations.AddUser;
import com.openwords.services.implementations.CheckEmail;
import com.openwords.services.implementations.CheckUsername;
import com.openwords.services.implementations.LoginUser;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.services.interfaces.SimpleResultHandler;
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

        findViewById(R.id.act_test_test7).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //WordAudioManager.addAudioFiles(new int[]{1, 5938, 1, 139228, 139228, 5938, 36293, 1, 176, 53}, ActivityTest.this);
            }
        });

        findViewById(R.id.act_test_test8).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new LoginUser().doRequest(new RequestParamsBuilder().addParam("username", "han")
                        .addParam("password", "123456")
                        .getParams(),
                        new HttpResultHandler() {

                            public void hasResult(Object resultObject) {
                                LoginUser.Result r = (LoginUser.Result) resultObject;
                                Toast.makeText(ActivityTest.this, "Login Success: " + r.userId, Toast.LENGTH_SHORT).show();
                            }

                            public void noResult(String errorMessage) {
                                Toast.makeText(ActivityTest.this, "Login Fail: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.act_test_test9).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new LoginUser().doRequest(new RequestParamsBuilder().addParam("username", "han")
                        .addParam("password", "12345")
                        .getParams(),
                        new HttpResultHandler() {

                            public void hasResult(Object resultObject) {
                                LoginUser.Result r = (LoginUser.Result) resultObject;
                                Toast.makeText(ActivityTest.this, "Login Success: " + r.userId, Toast.LENGTH_SHORT).show();
                            }

                            public void noResult(String errorMessage) {
                                Toast.makeText(ActivityTest.this, "Login Fail: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.act_test_test10).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new CheckUsername().doRequest(new RequestParamsBuilder().addParam("username", "han")
                        .getParams(),
                        new HttpResultHandler() {

                            public void hasResult(Object resultObject) {
                                Toast.makeText(ActivityTest.this, "Username ok", Toast.LENGTH_SHORT).show();
                            }

                            public void noResult(String errorMessage) {
                                Toast.makeText(ActivityTest.this, "Username not ok: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.act_test_test11).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new CheckEmail().doRequest(new RequestParamsBuilder().addParam("email", "han@han.com")
                        .getParams(),
                        new HttpResultHandler() {

                            public void hasResult(Object resultObject) {
                                Toast.makeText(ActivityTest.this, "Email ok", Toast.LENGTH_SHORT).show();
                            }

                            public void noResult(String errorMessage) {
                                Toast.makeText(ActivityTest.this, "Email not ok: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.act_test_test12).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new AddUser().doRequest(new RequestParamsBuilder().addParam("email", "han@han.com")
                        .addParam("username", "han")
                        .addParam("password", "111111")
                        .getParams(),
                        new HttpResultHandler() {

                            public void hasResult(Object resultObject) {
                                AddUser.Result r = (AddUser.Result) resultObject;
                                Toast.makeText(ActivityTest.this, "AddUser ok: " + r.userId, Toast.LENGTH_SHORT).show();
                            }

                            public void noResult(String errorMessage) {
                                Toast.makeText(ActivityTest.this, "AddUser fail: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.act_test_test99).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Language.checkAndMergeNewLanguages(ActivityTest.this, LocalSettings.getBaseLanguageId(), new SimpleResultHandler() {

                    public void hasResult(Object resultObject) {
                        List<Integer> local = UserLearningLanguages.loadUserLearningLanguagesFromLocal(1);
                        LogUtil.logDeubg(this, "local 1: " + local);
                        local = UserLearningLanguages.loadUserLearningLanguagesFromLocal(98);
                        LogUtil.logDeubg(this, "local 98: " + local);

                        UserLearningLanguages.loadUserLearningLanguages(1, 1, new SimpleResultHandler() {

                            public void hasResult(Object resultObject) {
                                List<Integer> ids = (List<Integer>) resultObject;
                                LogUtil.logDeubg(this, "loadUserLearningLanguages got ids: " + ids);
                            }
                        });
                    }
                });

            }
        });
    }
}
