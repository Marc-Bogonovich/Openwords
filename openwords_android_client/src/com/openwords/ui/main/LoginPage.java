package com.openwords.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.ResultLanguage;
import com.openwords.model.ResultUserLearningLanguages;
import com.openwords.model.UserLearningLanguages;
import com.openwords.services.implementations.LoginUser;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.sound.SoundPlayer;
import com.openwords.test.ActivityTest;
import com.openwords.tts.Speak;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.util.InternetCheck;
import com.openwords.util.RandomSelectAlg;
import com.openwords.util.WordSelectionAlg;
import com.openwords.util.WordSelectionAlgNoRepeat;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.localization.LocalLanguage;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.List;

public class LoginPage extends Activity {

    private Button loginButton, registerButton;
    private CheckBox remember;
    private EditText usernameField;
    private EditText passwdField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        LogUtil.logDeubg(this, "onCreate");
        initServices();

        setContentView(R.layout.activity_login_page);

        getUI();

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

        //test
        findViewById(R.id.loginPage_test).setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, ActivityTest.class));
            }
        });
    }

    private void getUI() {
        usernameField = (EditText) findViewById(R.id.loginPage_EditText_username);
        passwdField = (EditText) findViewById(R.id.loginPage_EditText_password);
        passwdField.setTypeface(Typeface.DEFAULT);
        passwdField.setTransformationMethod(new PasswordTransformationMethod());
        loginButton = (Button) findViewById(R.id.loginPage_Button_loginSubmit);
        loginButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                MyDialogHelper.tryShowQuickProgressDialog(LoginPage.this, LocalizationManager.getTextValidatingUser() + "...");
                login(usernameField.getText().toString(), passwdField.getText().toString());
            }
        });
        registerButton = (Button) findViewById(R.id.loginPage_Button_registerGo);
        registerButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, RegisterPage.class));
            }
        });
        remember = (CheckBox) findViewById(R.id.loginPage_CheckBox_rememberMe);
    }

    private void fillUI() {
        loginButton.setText(LocalizationManager.getTextLogin());
        registerButton.setText(LocalizationManager.getTextRegister());
        remember.setText(LocalizationManager.getTextRememberMe());
        remember.setChecked(LocalSettings.isRemember());
        usernameField.setHint(LocalizationManager.getTextHintUser());
        passwdField.setHint(LocalizationManager.getTextHintPass());

        if (LocalSettings.isRemember()) {
            usernameField.setText(LocalSettings.getUsername());
            passwdField.setText(LocalSettings.getPassword());
            remember.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.logDeubg(this, "onResume");
        if (DataPool.DoRegistration) {
            DataPool.DoRegistration = false;
            login(LocalSettings.getUsername(), LocalSettings.getPassword());
        }
        fillUI();
    }

    private void login(final String username, final String password) {
        if (!InternetCheck.hasNetwork(this)) {
            if (LocalSettings.getUsername().equals(username)
                    && LocalSettings.getPassword().equals(password)) {
                DataPool.OffLine = true;
                MyQuickToast.showLong(this, "You are now in offline mode");
                goToHomePage();
                return;
            }
        }

        new LoginUser().doRequest(new RequestParamsBuilder()
                .addParam("username", username)
                .addParam("password", password)
                .getParams(),
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        LocalSettings.setRemember(remember.isChecked());
                        LocalSettings.setUsername(username);
                        LocalSettings.setPassword(password);
                        int newUserId = (Integer) resultObject;
                        if (LocalSettings.getUserId() != newUserId) {
                            MyQuickToast.showShort(LoginPage.this, "User changed");
                            UserLearningLanguages.deleteAll(UserLearningLanguages.class);
                        } else {
                            //in case cannot connect to server
                            //???UserLearningLanguages.loadUserLearningLanguagesLocal(LocalSettings.getBaseLanguageId());
                        }
                        LocalSettings.setUserId(newUserId);
                        UserLearningLanguages.loadFreshUserLearningLanguages(
                                LocalSettings.getUserId(),
                                LocalSettings.getBaseLanguageId(),
                                true,
                                new ResultUserLearningLanguages() {

                                    public void result(List<UserLearningLanguages> result) {
                                        loadLanguageDataAndGoHome();
                                    }
                                });
                    }

                    public void noResult(String errorMessage) {
                        MyDialogHelper.tryDismissQuickProgressDialog();
                        MyQuickToast.showShort(LoginPage.this, "Login fail: " + errorMessage);
                    }
                });
    }

    private void loadLanguageDataAndGoHome() {
        Language.checkAndMergeNewLanguages(this, LocalSettings.getBaseLanguageId(), new ResultLanguage() {

            public void result(String result) {
                goToHomePage();
            }
        });
    }

    private void goToHomePage() {
        MyDialogHelper.tryDismissQuickProgressDialog();
        startActivity(new Intent(LoginPage.this, HomePage.class));
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtFirstPage(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                LoginPage.super.onBackPressed();
            }
        });
    }

    private void initServices() {
        OpenwordsSharedPreferences.init(this);
        Speak.getInstance(this);
        LocalFileSystem.makeFolders();
        LocalizationManager.init(this);

        LocalLanguage lang = LocalSettings.getLocalLanguage();
        if (lang == null) {
            String current = getResources().getConfiguration().locale.getDisplayLanguage();
            for (Object[] item : LocalizationManager.LanguageNamesTypesIdsLocales) {
                String support = (String) item[3];
                if (current.equals(support)) {
                    LocalizationManager.setLocalLanguage((LocalLanguage) item[1]);
                    LocalOptionPage.supported = true;
                    break;
                }
            }
            if (!LocalOptionPage.supported) {
                LocalizationManager.setLocalLanguage(LocalLanguage.English);
            }

            startActivity(new Intent(this, LocalOptionPage.class));
        } else {
            LocalizationManager.setLocalLanguage(lang);
        }
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
        Toast.makeText(this, LocalizationManager.getTextBye(), Toast.LENGTH_SHORT).show();
    }

}
