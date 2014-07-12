package com.openwords.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.openwords.R;
import com.openwords.model.JSONParser;
import com.openwords.model.UserInfo;
import com.openwords.util.UIHelper;
import com.openwords.util.preference.OpenwordsSharedPreferences;

import android.os.Bundle;
import android.provider.Contacts.Settings;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterPage extends Activity implements OnClickListener {
	public static final String TAG_SUCCESS="success";
	public static final String TAG_MESSAGE="message";
	public static final String TAG_USERID="userid";
	private static String url_user_exist = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/userExist.php";
	private static String url_user_register = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/userRegister.php";
	private String username = null;
	private String password = null;
	private ProgressDialog pDialog = null;
	private boolean usernameExist = true;
	private EditText usernameField;
	private EditText passwdField;
	private EditText passwdField2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_page);
		
		usernameField = (EditText) findViewById(R.id.registerPage_EditText_username);
		//modify textfield to password input field
		passwdField = (EditText) findViewById(R.id.registerPage_EditText_password);
		passwdField.setTypeface(Typeface.DEFAULT);
		passwdField.setTransformationMethod(new PasswordTransformationMethod());
		passwdField2 = (EditText) findViewById(R.id.registerPage_EditText_password2);
		passwdField2.setTypeface(Typeface.DEFAULT);
		passwdField2.setTransformationMethod(new PasswordTransformationMethod());
		
		
		Button submitButton = (Button) findViewById(R.id.registerPage_Button_registerSubmit);
		submitButton.setOnClickListener(this);
		
		usernameField.setOnFocusChangeListener(new OnFocusChangeListener() {          
	        public void onFocusChange(View v, boolean hasFocus) {
	            if(!hasFocus) {
	            	Log.d("Focus","Lost focus");
	            	validUsername();
	            }
	        }
	    });
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_page, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		 case R.id.registerPage_Button_registerSubmit:
			 languagePageButtonClick();
			 break;
		}
	}

	private void languagePageButtonClick() {
		// TODO Auto-generated method stub
		registerUsername();
	}
	
	private void validUsername() {
		username = usernameField.getText().toString();
        new Thread(new Runnable() {
            public void run() {
                valid();                          
            }
          }).start(); 
	}
	
	private void registerUsername() {
	    username = usernameField.getText().toString();
	    password = passwdField.getText().toString();
   
 		if(!usernameExist) {
	        if(identicalPassword()) {
	    		pDialog = ProgressDialog.show(RegisterPage.this, "", 
	                    "Writing to database...", true);
	        	Log.d("Info","Go to register");
				 new Thread(new Runnable() {
		                public void run() {
		                    register();                          
		                }
		              }).start();    
	        }
		}
	}
	
	void valid(){
        try{
        	Log.e("Info","In valid");
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("email",username.trim()));

            JSONParser jsonParse = new JSONParser();
            boolean flag = true;
            int success = 0;
            try {
                JSONObject jObj = jsonParse.makeHttpRequest(url_user_exist, "POST", params);
                //Log.d("Res",jObj.toString());
                success = jObj.getInt(TAG_SUCCESS);
                String msg = jObj.getString(TAG_MESSAGE);
                Log.d("RES",msg);
            } catch (Exception e) {
            	flag = false;
            	e.printStackTrace();
            }
            
            if(flag==false) {

                runOnUiThread(new Runnable() {
                    public void run() {
                    	Toast toast = Toast.makeText(RegisterPage.this, "Server error", Toast.LENGTH_SHORT);
                    	toast.setGravity(Gravity.TOP , 0, 0);
                    	toast.show();
                    }
                });
                return;
            }

//            runOnUiThread(new Runnable() {
//                public void run() {
//                    pDialog.dismiss();
//                }
//            });
            if (success == 1) {
            	Log.d("Info","user doesn't exist");
            	usernameExist = false;
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(RegisterPage.this,"Username OK", Toast.LENGTH_SHORT);
                    	toast.setGravity(Gravity.TOP , 0, 0);
                    	toast.show();
                    }
                });

            }else{
            	usernameExist = true;
            	  runOnUiThread(new Runnable() {
                      public void run() {
                    	  Toast toast = Toast.makeText(RegisterPage.this,"Username exists", Toast.LENGTH_SHORT);
                          toast.setGravity(Gravity.TOP , 0, 0);
                          toast.show();
                      }
                  });            
            }
             
        }catch(Exception e){
            pDialog.dismiss();
            Log.e("Exception : ", e.getMessage());
        }
    }

	private boolean identicalPassword() {
		String passwd1 = passwdField.getText().toString();
		String passwd2 = passwdField2.getText().toString();
		if(passwd1.equals(passwd2)) {
			Log.d("Info","passwd are same");
			return true;
		} else {
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(RegisterPage.this,"Password inconsist", Toast.LENGTH_SHORT).show();
				}
			});
		}
		Log.d("Info","passwd are not same");
		return false;
	}
	
	
	private void register() {
		 try{
	            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
	            params.add(new BasicNameValuePair("email",username.trim()));
	            params.add(new BasicNameValuePair("password",password.trim()));
	            Log.d("username",username);
	            Log.d("password",password);
	            JSONParser jsonParse = new JSONParser();
	            JSONObject jObj = jsonParse.makeHttpRequest(url_user_register, "POST", params);
	            Log.d("Res",jObj.toString());
	            int success = jObj.getInt(TAG_SUCCESS);
	            String msg = jObj.getString(TAG_MESSAGE);
	            int userid = jObj.getInt(TAG_USERID);
	            Log.d("USERID",Integer.toString(userid));
	            runOnUiThread(new Runnable() {
	                public void run() {
	                    pDialog.dismiss();
	                }
	            });
	            if (success == 1) {
	            	Log.d("Info","create successfully");
	            	OpenwordsSharedPreferences.setUserInfo(new UserInfo(userid, username, password, System.currentTimeMillis()));
	            	usernameExist = true;
	                runOnUiThread(new Runnable() {
	                    public void run() {
	                        Toast.makeText(RegisterPage.this,"User create successfully", Toast.LENGTH_SHORT).show();
	                    }
	                });
					
					RegisterPage.this.startActivity(new Intent(RegisterPage.this, LanguagePage.class));
	            }else{
	            	usernameExist = false;
	            	  runOnUiThread(new Runnable() {
	                      public void run() {
	                          Toast.makeText(RegisterPage.this,"User create fail", Toast.LENGTH_SHORT).show();
	                      }
	                  });            
	            }
	             
	        }catch(Exception e){
	            pDialog.dismiss();
	            Log.e("Exception : ", e.getMessage());
	        }
	}
}
