package com.openwords.ui.main;

import android.app.Activity;
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
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.UserLanguage;
import com.openwords.services.implementations.ServiceAddUser;
import com.openwords.services.implementations.ServiceCheckEmail;
import com.openwords.services.implementations.ServiceCheckUsername;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;

public class RegisterPage extends Activity {

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
        new ServiceCheckEmail().doRequest(emailField.getText().toString(),
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        MyQuickToast.showShort(RegisterPage.this, "email is valid");
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(RegisterPage.this, errorMessage);
                    }
                });

    }

    private void validateUsername() {
        new ServiceCheckUsername().doRequest(usernameField.getText().toString(),
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        MyQuickToast.showShort(RegisterPage.this, "username is valid");
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(RegisterPage.this, errorMessage);
                    }
                });
    }

    private void register() {
        if (identicalPassword()) {
            MyDialogHelper.tryShowQuickProgressDialog(this, "Connecting to server...");

            final String username = usernameField.getText().toString();
            final String password = passwdField.getText().toString();

            new ServiceAddUser().doRequest(emailField.getText().toString(), username, password,
                    new HttpResultHandler() {

                        public void hasResult(Object resultObject) {
                            MyDialogHelper.tryDismissQuickProgressDialog();
                            Language.deleteAll(Language.class);
                            UserLanguage.deleteAll(UserLanguage.class);

                            ServiceAddUser.Result r = (ServiceAddUser.Result) resultObject;
                            MyQuickToast.showShort(RegisterPage.this, "AddUser ok: " + r.userId);
                            finish();
                            LocalSettings.setUsername(username);
                            LocalSettings.setPassword(password);
                            DataPool.DoRegistration = true;
                            LocalSettings.setUserId(r.userId);
                            RegisterPage.this.startActivity(new Intent(RegisterPage.this, LanguagePage.class));
                        }

                        public void noResult(String errorMessage) {
                            MyDialogHelper.tryDismissQuickProgressDialog();
                            MyQuickToast.showShort(RegisterPage.this, "AddUser fail: " + errorMessage);
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
            MyQuickToast.showShort(RegisterPage.this, "passwords are inconsistent");
        }
        return false;
    }
}
