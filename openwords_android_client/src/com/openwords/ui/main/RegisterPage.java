package com.openwords.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import com.openwords.R;
import com.openwords.model.UserInfo;
import com.openwords.services.implementations.AddUser;
import com.openwords.services.implementations.CheckEmail;
import com.openwords.services.implementations.CheckUsername;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.util.ui.QuickToast;

public class RegisterPage extends Activity {

    private ProgressDialog pDialog = null;
    private EditText usernameField;
    private EditText emailField;
    private EditText passwdField;
    private EditText passwdField2;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        usernameField = (EditText) findViewById(R.id.registerPage_EditText_username);
        emailField = (EditText) findViewById(R.id.registerPage_EditText_email);
        passwdField = (EditText) findViewById(R.id.registerPage_EditText_password);
        passwdField.setTypeface(Typeface.DEFAULT);
        passwdField.setTransformationMethod(new PasswordTransformationMethod());
        passwdField2 = (EditText) findViewById(R.id.registerPage_EditText_password2);
        passwdField2.setTypeface(Typeface.DEFAULT);
        passwdField2.setTransformationMethod(new PasswordTransformationMethod());

        submitButton = (Button) findViewById(R.id.registerPage_Button_registerSubmit);
        submitButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                register();
            }
        });

        usernameField.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateUsername();
                }
            }
        });

        emailField.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateEmail();
                }
            }
        });
    }

    private void fillUI() {
        usernameField.setHint(LocalizationManager.getTextHintUser());
        emailField.setHint(LocalizationManager.getTextHintEmail());
        passwdField.setHint(LocalizationManager.getTextHintPass());
        passwdField2.setHint(LocalizationManager.getTextHintPassRe());
        submitButton.setHint(LocalizationManager.getTextReg());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillUI();
    }

    private void validateEmail() {
        new CheckEmail().doRequest(new RequestParamsBuilder()
                .addParam("email", emailField.getText().toString())
                .getParams(),
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        QuickToast.showShort(RegisterPage.this, "email is valid");
                    }

                    public void noResult(String errorMessage) {
                        QuickToast.showShort(RegisterPage.this, errorMessage);
                    }
                });

    }

    private void validateUsername() {
        new CheckUsername().doRequest(new RequestParamsBuilder()
                .addParam("username", usernameField.getText().toString())
                .getParams(),
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        QuickToast.showShort(RegisterPage.this, "username is valid");
                    }

                    public void noResult(String errorMessage) {
                        QuickToast.showShort(RegisterPage.this, errorMessage);
                    }
                });
    }

    private void register() {
        if (identicalPassword()) {
            pDialog = ProgressDialog.show(RegisterPage.this, "",
                    "Connecting server...", true);

            final String username = usernameField.getText().toString();
            final String password = passwdField.getText().toString();

            new AddUser().doRequest(new RequestParamsBuilder()
                    .addParam("email", emailField.getText().toString())
                    .addParam("username", username)
                    .addParam("password", password)
                    .getParams(),
                    new HttpResultHandler() {

                        public void hasResult(Object resultObject) {
                            pDialog.dismiss();
                            AddUser.Result r = (AddUser.Result) resultObject;
                            OpenwordsSharedPreferences.setUserInfo(new UserInfo(r.userId, username, password, System.currentTimeMillis()));
                            QuickToast.showShort(RegisterPage.this, "AddUser ok: " + r.userId);
                            finish();
                            LoginPage.setUserPass(username, password);
                            LoginPage.DoRegistration = true;
                            RegisterPage.this.startActivity(new Intent(RegisterPage.this, LanguagePage.class));
                        }

                        public void noResult(String errorMessage) {
                            pDialog.dismiss();
                            QuickToast.showShort(RegisterPage.this, "AddUser fail: " + errorMessage);
                        }
                    });
        }
    }

    private boolean identicalPassword() {
        String passwd1 = passwdField.getText().toString();
        String passwd2 = passwdField2.getText().toString();
        if (passwd1.equals(passwd2)) {
            return true;
        } else {
            QuickToast.showShort(RegisterPage.this, "passwords are inconsistent");
        }
        return false;
    }
}
