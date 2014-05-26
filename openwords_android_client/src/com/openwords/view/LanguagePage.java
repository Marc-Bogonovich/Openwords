package com.openwords.view;

import com.openwords.R;

import android.os.Bundle;
import android.provider.Contacts.Settings;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LanguagePage extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language_page);
		
		Button submitButton = (Button) findViewById(R.id.languagePage_Button_chosenPage);
		submitButton.setOnClickListener(this);
		
		//List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        //params.add(new BasicNameValuePair("email",username.trim()));

        //JSONParser jsonParse = new JSONParser();
        //JSONObject jObj = jsonParse.makeHttpRequest(url_user_exist, "POST", params);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_page, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch(v.getId()) {
		 case R.id.languagePage_Button_chosenPage:
			 chosenPageButtonClick();
			 break;
		 }
	}

	private void chosenPageButtonClick() {
		// TODO Auto-generated method stub
		 LanguagePage.this.startActivity(new Intent(LanguagePage.this, ChosenPage.class));
	}
}