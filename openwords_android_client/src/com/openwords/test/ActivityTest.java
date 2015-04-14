package com.openwords.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.services.implementations.GetWordAudioNames;
import com.openwords.services.implementations.ServiceAddUser;
import com.openwords.services.implementations.ServiceCheckEmail;
import com.openwords.services.implementations.ServiceCheckUsername;
import com.openwords.services.implementations.ServiceGetUserPerformanceSum;
import com.openwords.services.implementations.ServiceGetUserPerformanceSum.Result;
import com.openwords.services.implementations.ServiceGetWordConnectionsByLangOne;
import com.openwords.services.implementations.ServiceLoginUser;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.ui.common.DialogForHTTP;
import com.openwords.util.gson.MyGson;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;
import com.orm.query.Select;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;

/**
 *
 * @author hanaldo
 */
public class ActivityTest extends Activity {
    
    private ActivityTest act = ActivityTest.this;
    
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
//                List<UserWords> existList = UserWords.findByLanguage(2);
//                Toast.makeText(ActivityTest.this, new Gson().toJson(existList), Toast.LENGTH_SHORT).show();
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
                new ServiceLoginUser().doRequest("han", "123456",
                        new HttpResultHandler() {
                            
                            public void hasResult(Object resultObject) {
                                ServiceLoginUser.Result r = (ServiceLoginUser.Result) resultObject;
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
                new ServiceLoginUser().doRequest("han", "12345",
                        new HttpResultHandler() {
                            
                            public void hasResult(Object resultObject) {
                                ServiceLoginUser.Result r = (ServiceLoginUser.Result) resultObject;
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
                new ServiceCheckUsername().doRequest("han",
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
                new ServiceCheckEmail().doRequest("han@han.com",
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
                new ServiceAddUser().doRequest("han@han.com", "han", "111111",
                        new HttpResultHandler() {
                            
                            public void hasResult(Object resultObject) {
                                ServiceAddUser.Result r = (ServiceAddUser.Result) resultObject;
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
                Set<Integer> ids = new HashSet<Integer>(10);
                ids.add(39);
                ids.add(40);
                new GetWordAudioNames().doRequest(ids, new HttpResultHandler() {
                    
                    public void hasResult(Object resultObject) {
                        GetWordAudioNames.Result r = (GetWordAudioNames.Result) resultObject;
                        LogUtil.logDeubg(this, MyGson.toJson(r));
                    }
                    
                    public void noResult(String errorMessage) {
                        LogUtil.logDeubg(this, errorMessage);
                    }
                });
//                try {
//                    LocalFileSystem.makeFolders();
//                    String path = LocalFileSystem.Folders[2];
//                    LogUtil.logDeubg(this, path);
//
//                    String url = "jdbc:h2:"
//                            + path
//                            + "/hello"
//                            + ";FILE_LOCK=FS"
//                            + ";PAGE_SIZE=1024"
//                            + ";CACHE_SIZE=8192";
//                    Class.forName("org.h2.Driver");
//                    Connection conn = DriverManager.getConnection(url);
//
//                    Statement st = conn.createStatement();
//                    String sql = "SELECT * FROM TEST";
//                    ResultSet rs = st.executeQuery(sql);
//                    rs.next();
//                    LogUtil.logDeubg(this, rs.getString(1));
//                    LogUtil.logDeubg(this, rs.getString(2));
//
//                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(ActivityTest.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (SQLException ex) {
//                    Logger.getLogger(ActivityTest.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
            
        });
    }
}
