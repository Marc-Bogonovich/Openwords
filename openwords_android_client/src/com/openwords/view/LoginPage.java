package com.openwords.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.model.JSONParser;
import com.openwords.model.Plate;
import com.openwords.model.PlateTestType;
import com.openwords.model.UserInfo;
import com.openwords.test.ActivitySelfEval;
import com.openwords.tts.Speak;
import com.openwords.util.UIHelper;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class LoginPage extends Activity implements OnClickListener {

    public static final String LOGTAG = "LoginPage";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USERID = "userid";
    public static final String SAVEUSER = "pref_saveuser";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_USERID = "userid";
    private static final String url_check_user = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/validUser.php";
    //private static String url_check_user = "http://geographycontest.ipage.com/OpenwordsOrg/validUser.php";
    private SharedPreferences settings;
    private ProgressDialog pDialog = null;
    private String username;
    private String password;
    private EditText usernameField;
    private EditText passwdField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        LogUtil.logDeubg(this, "onCreate");
        initServices();

        setContentView(R.layout.activity_login_page);
        usernameField = (EditText) findViewById(R.id.loginPage_EditText_username);
        //modify textfield to password input field
        passwdField = (EditText) findViewById(R.id.loginPage_EditText_password);
        passwdField.setTypeface(Typeface.DEFAULT);
        passwdField.setTransformationMethod(new PasswordTransformationMethod());

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        if (settings.getBoolean(SAVEUSER, false)) { // if user choose save username before
            username = settings.getString(USERNAME, "");
            password = settings.getString(PASSWORD, "");
            UIHelper.displayText(this, R.id.loginPage_EditText_username, username);
            UIHelper.displayText(this, R.id.loginPage_EditText_password, password);
            UIHelper.setCBChecked(this, R.id.loginPage_CheckBox_rememberMe, true);
        }

        Button loginButton = (Button) findViewById(R.id.loginPage_Button_loginSubmit);
        loginButton.setOnClickListener(this);
        Button registerButton = (Button) findViewById(R.id.loginPage_Button_registerGo);
        registerButton.setOnClickListener(this);

        if (!OpenwordsSharedPreferences.isAppStarted()) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        OpenwordsSharedPreferences.setAppStarted(true);

        UserInfo user = OpenwordsSharedPreferences.getUserInfo();
        if (user == null) {
            Toast.makeText(LoginPage.this, "no user", Toast.LENGTH_SHORT).show();
        } else {
            usernameField.setText(user.getUserName());
            passwdField.setText(user.getPass());
        }

        Button test = (Button) findViewById(R.id.loginPage_test);
        test.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                List<Plate> plate = Plate.listAll(Plate.class);
                ActivitySelfEval.setProblemPool(plate);
                startActivity(new Intent(LoginPage.this, ActivitySelfEval.class));
            }
        });

        //TEST!!! Please delete soon
        List<Plate> ps = Plate.listAll(Plate.class);
        Toast.makeText(LoginPage.this, "This is a test: I have " + ps.size() + " records in my plate!", Toast.LENGTH_SHORT).show();
        if (ps.isEmpty()) {
            long plateId = System.currentTimeMillis();
            new Plate(-1,
                    Plate.Performance_Null,
                    PlateTestType.Test_Type_Review,
                    "",
                    "",
                    "人",
                    "ren",
                    "",
                    "person",
                    "bird",
                    plateId,
                    LoginPage.this).save();
            new Plate(-1,
                    Plate.Performance_Null,
                    PlateTestType.Test_Type_Self_Evaluate,
                    "",
                    "",
                    "猫",
                    "mao",
                    "",
                    "cat",
                    "tiger",
                    plateId,
                    LoginPage.this).save();
            new Plate(-1,
                    Plate.Performance_Null,
                    PlateTestType.Test_Type_Self_Evaluate,
                    "",
                    "",
                    "时间",
                    "shi jian",
                    "",
                    "time",
                    "clock",
                    plateId,
                    LoginPage.this).save();
            new Plate(-1,
                    Plate.Performance_Null,
                    PlateTestType.Test_Type_Self_Evaluate,
                    "",
                    "",
                    "地球",
                    "di qiu",
                    "",
                    "earth",
                    "football",
                    plateId,
                    LoginPage.this).save();
            Toast.makeText(LoginPage.this, "We just made 4 problems for you", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_page, menu);
//        return true;
//    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginPage_Button_loginSubmit:
                loginButtonClick();
                break;
            case R.id.loginPage_Button_registerGo:
                LogUtil.logDeubg(this, "button register");
                registerButtonClick();
                break;
        }
    }

    private void registerButtonClick() {
        LoginPage.this.startActivity(new Intent(LoginPage.this, RegisterPage.class));
    }

    private void loginButtonClick() {
        pDialog = ProgressDialog.show(LoginPage.this, "",
                "Validating user...", true);
        new Thread(new Runnable() {
            public void run() {
                login();
            }
        }).start();
    }

    void login() {
        try {
            username = usernameField.getText().toString();
            password = passwdField.getText().toString();
            Log.d("Login", "username " + username);
            Log.d("Login", "passwd " + password);
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("email", username.trim()));
            params.add(new BasicNameValuePair("password", password.trim()));
            for (NameValuePair i : params) {
                LogUtil.logDeubg(this, "params: " + i.toString());
            }
            JSONParser jsonParse = new JSONParser();
            JSONObject jObj = jsonParse.makeHttpRequest(url_check_user, "POST", params);
            //Log.d("Res",jObj.toString());
            int success = jObj.getInt(TAG_SUCCESS);
            String msg = jObj.getString(TAG_MESSAGE);
            int userid = jObj.getInt(TAG_USERID);
            LogUtil.logDeubg(this, msg);
            runOnUiThread(new Runnable() {
                public void run() {
                    pDialog.dismiss();
                }
            });
            if (success == 1) {
                LogUtil.logDeubg(this, "user found");

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginPage.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }
                });
                LogUtil.logDeubg(this, "should go to next");
                //save user preference
//                Boolean saveuser = UIHelper.getCBChecked(this, R.id.loginPage_CheckBox_rememberMe);
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putInt(USERID, userid);
//                if (saveuser) {
//                    editor.putString(USERNAME, username);
//                    editor.putString(PASSWORD, password);
//                    editor.putBoolean(SAVEUSER, saveuser);
//                    editor.commit();
//                }
                OpenwordsSharedPreferences.setUserInfo(new UserInfo(userid, username, password, System.currentTimeMillis()));
                LoginPage.this.startActivity(new Intent(LoginPage.this, HomePage.class));
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginPage.this, "Invalid user", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            pDialog.dismiss();
            LogUtil.logWarning(this, e);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginPage.super.onBackPressed();
                    }
                }).create().show();
    }

    private void initServices() {
        OpenwordsSharedPreferences.init(this);
        Speak.getInstance(this);
    }

    private void cleanServices() {
        OpenwordsSharedPreferences.clean();
        Speak.getInstance(null).clean();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.logDeubg(this, "onDestroy");
        OpenwordsSharedPreferences.setAppStarted(false);

        cleanServices();
        Toast.makeText(this, "Bye Bye", Toast.LENGTH_SHORT).show();
    }
}
