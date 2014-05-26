package com.openwords.view;
//Follow tutorial http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.openwords.R;
import com.openwords.model.JSONParser;
import com.openwords.model.Snippet;
import com.openwords.util.UIHelper;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends Activity implements OnClickListener{
	
	public static final String LOGTAG="LoginPage";
	public static final String USERNAME="username";
	public static final String PASSWORD="password";
	public static final String SAVEUSER="pref_saveuser";
	public static final String TAG_SUCCESS="success";
	public static final String TAG_MESSAGE="message";
	private static String url_check_user = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/validUser.php";
	//private static String url_check_user = "http://geographycontest.ipage.com/OpenwordsOrg/validUser.php";
	private SharedPreferences settings;
	private ProgressDialog pDialog = null;
	private String username;
	private String password;
	private EditText usernameField;
	private EditText passwdField;
	


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("onCreat","LoginPage");
		setContentView(R.layout.activity_login_page);
		usernameField = (EditText) findViewById(R.id.loginPage_EditText_username);
		//modify textfield to password input field
		passwdField = (EditText) findViewById(R.id.loginPage_EditText_password);
		passwdField.setTypeface(Typeface.DEFAULT);
		passwdField.setTransformationMethod(new PasswordTransformationMethod());
		

		settings = PreferenceManager.getDefaultSharedPreferences(this);
		if(settings.getBoolean(SAVEUSER,false)) { // if user choose save username before
			username = settings.getString(USERNAME, "");
			password = settings.getString(PASSWORD,"");
			UIHelper.displayText(this, R.id.loginPage_EditText_username, username);
			UIHelper.displayText(this, R.id.loginPage_EditText_password, password);
			UIHelper.setCBChecked(this, R.id.loginPage_CheckBox_rememberMe, true);
		}
		
		Button loginButton = (Button) findViewById(R.id.loginPage_Button_loginSubmit);
		loginButton.setOnClickListener(this);
		Button registerButton = (Button) findViewById(R.id.loginPage_Button_registerGo);
		registerButton.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch(v.getId()) {
		 case R.id.loginPage_Button_loginSubmit:
			 loginButtonClick();
			 break;
		 case R.id.loginPage_Button_registerGo:
			 Log.d("button","register");
			 registerButtonClick();
			 break;
		 }
	}
	
	private void registerButtonClick() {
		// TODO Auto-generated method stub
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
	
	void login(){
        try{
        	username = usernameField.getText().toString();
        	password = passwdField.getText().toString();
        	Log.d("username",username);
        	Log.d("passwd",password);
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("email",username.trim()));
            params.add(new BasicNameValuePair("password",password.trim()));
            for (NameValuePair i : params) {
            	Log.d("USer", i.toString());
            }
            JSONParser jsonParse = new JSONParser();
            JSONObject jObj = jsonParse.makeHttpRequest(url_check_user, "POST", params);
            //Log.d("Res",jObj.toString());
            int success = jObj.getInt(TAG_SUCCESS);
            String msg = jObj.getString(TAG_MESSAGE);
            Log.d("RES",msg);
            runOnUiThread(new Runnable() {
                public void run() {
                    pDialog.dismiss();
                }
            });
            if (success == 1) {
            	Log.d("Info","user found");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginPage.this,"Login Success", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("RES","should go to next");
                //save user preference
    			Boolean saveuser = UIHelper.getCBChecked(this, R.id.loginPage_CheckBox_rememberMe);
    			if(saveuser) {
    				SharedPreferences.Editor editor = settings.edit();
    				editor.putString(USERNAME, username);
    				editor.putString(PASSWORD, password);
    				editor.putBoolean(SAVEUSER, saveuser);
    				editor.commit();
    			}
                LoginPage.this.startActivity(new Intent(LoginPage.this, HomePage.class));
            }else{
            	  runOnUiThread(new Runnable() {
                      public void run() {
                          Toast.makeText(LoginPage.this,"Invalid user", Toast.LENGTH_SHORT).show();
                      }
                  });            
            }
             
        }catch(Exception e){
            pDialog.dismiss();
            Log.e("Exception : ", e.getMessage());
        }
    }
}





