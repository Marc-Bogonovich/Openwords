package com.openwords.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.InitDatabase;
import com.openwords.model.UserInfo;
import com.openwords.services.CheckUser;
import com.openwords.services.GetLanguages;
import com.openwords.services.ModelLanguage;
import com.openwords.sound.SoundPlayer;
import com.openwords.test.ActivityTest;
import com.openwords.tts.Speak;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.util.InternetCheck;
import com.openwords.util.RandomSelectAlg;
import com.openwords.util.UIHelper;
import com.openwords.util.WordSelectionAlg;
import com.openwords.util.WordSelectionAlgNoRepeat;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.List;

public class LoginPage extends Activity implements OnClickListener {

    public static boolean DoRegistration = false;
    private static String username;
    private static String password;

    public static void setUserPass(String username, String password) {
        LoginPage.username = username;
        LoginPage.password = password;
    }
    private ProgressDialog pDialog = null;
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

        if (OpenwordsSharedPreferences.getSaveUser()) { // if user choose save username before
            username = OpenwordsSharedPreferences.getUserInfo().getUserName();
            password = OpenwordsSharedPreferences.getUserInfo().getPass();
            usernameField.setText(username);
            passwdField.setText(password);
//            UIHelper.displayText(this, R.id.loginPage_EditText_username, username);
//            UIHelper.displayText(this, R.id.loginPage_EditText_password, password);
            UIHelper.setCBChecked(this, R.id.loginPage_CheckBox_rememberMe, true);
        }

        Button loginButton = (Button) findViewById(R.id.loginPage_Button_loginSubmit);
        loginButton.setOnClickListener(this);
        Button registerButton = (Button) findViewById(R.id.loginPage_Button_registerGo);
        registerButton.setOnClickListener(this);

        if (!OpenwordsSharedPreferences.isAppStarted()) {
            Intent i = new Intent(this, WelcomePage.class);
            startActivity(i);
        }
        OpenwordsSharedPreferences.setAppStarted(true);
        //add word algorithm
        if (OpenwordsSharedPreferences.getWordSelectionAlgList().isEmpty()) {
            OpenwordsSharedPreferences.addSelectionAlg(new WordSelectionAlg());
            OpenwordsSharedPreferences.addSelectionAlg(new RandomSelectAlg());
            OpenwordsSharedPreferences.addSelectionAlg(new WordSelectionAlgNoRepeat());
        }

        findViewById(R.id.loginPage_test).setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, ActivityTest.class));
            }
        });
    }

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
        startActivity(new Intent(LoginPage.this, RegisterPage.class));
    }

    private void loginButtonClick() {
        if (InternetCheck.checkConn(LoginPage.this)) {
            pDialog = ProgressDialog.show(LoginPage.this, "",
                    "Validating user...", true);
            login(usernameField.getText().toString(), passwdField.getText().toString());
        } else {
            Toast.makeText(LoginPage.this, "Cannot get access to internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.logDeubg(this, "onResume");
        if (DoRegistration) {
            DoRegistration = false;
            login(username, password);
        }
    }

    private void login(final String username, final String password) {
        LogUtil.logDeubg(this, "username " + username);
        LogUtil.logDeubg(this, "passwd " + password);

        CheckUser.request(username, password, 0, new CheckUser.AsyncCallback() {

            public void callback(int userId, String message, Throwable error) {
                try {
                    if (userId > 0) {
                        Toast.makeText(LoginPage.this, "Login Success", Toast.LENGTH_SHORT).show();

                        //save user preference
                        Boolean saveuser = UIHelper.getCBChecked(LoginPage.this, R.id.loginPage_CheckBox_rememberMe);
                        if (saveuser) {
                            OpenwordsSharedPreferences.setSaveUser(true);
                        } else {
                            OpenwordsSharedPreferences.setSaveUser(false);
                        }
                        int lu = 0;
                        long lupd = 0;
                        if (OpenwordsSharedPreferences.getUserInfo() != null) {
                            lu = OpenwordsSharedPreferences.getUserInfo().getUserId();
                            lupd = OpenwordsSharedPreferences.getUserInfo().getLastPerfUpd();
                        }
                        OpenwordsSharedPreferences.setUserInfo(new UserInfo(lu, 0, userId, username, password, System.currentTimeMillis(), lupd));

                        /* ********************************
                         * Refreshing User's data on client (if needed)
                         * ********************************
                         * */
                        InitDatabase.checkAndRefreshPerf(LoginPage.this, 0, 0);

                        //pre-load language information
                        GetLanguages.request(Integer.toString(userId), 0, new GetLanguages.AsyncCallback() {

                            public void callback(List<ModelLanguage> languages, Throwable error) {
                                if (languages != null) {
                                    languages.add(new ModelLanguage(-999, "Add more languages"));
                                    DataPool.LanguageList.clear();
                                    DataPool.LanguageList.addAll(languages);
                                    startActivity(new Intent(LoginPage.this, HomePage.class));
                                } else {
                                    Toast.makeText(LoginPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginPage.this, "Username/Password incorrect", Toast.LENGTH_SHORT).show();
                        if (error != null) {
                            Toast.makeText(LoginPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginPage.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtFirstPage(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                //when exit, remember user's choice
                Boolean saveuser = UIHelper.getCBChecked(LoginPage.this, R.id.loginPage_CheckBox_rememberMe);
                if (saveuser) {
                    OpenwordsSharedPreferences.setSaveUser(true);
                } else {
                    OpenwordsSharedPreferences.setSaveUser(false);
                }
                LoginPage.super.onBackPressed();
            }
        });
    }

    private void initServices() {
        OpenwordsSharedPreferences.init(this);
        Speak.getInstance(this);
        LocalFileSystem.makeFolders();
    }

    private void cleanServices() {
        OpenwordsSharedPreferences.clean();
        Speak.getInstance(null).clean();
        SoundPlayer.clean();
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
