package com.openwords.view;
import com.example.openwords.R;
import com.openwords.learningModule.*;

import android.os.Bundle;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class HomePage extends ActionBarActivity implements OnClickListener {
	
	private Spinner begin, language;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		addItemsOnBegin();
		addItemsOnLanguage();
		Button testPageGo = (Button) findViewById(R.id.homePage_Button_testPageGo);
		testPageGo.setOnClickListener(this);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home_page, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.homePage_item_home:
	            Log.d("Icon click","Home icon clicks");
	            return true;
	        case R.id.homePage_item_words:
	        	Log.d("Icon click","Word icon clicks");
	        	wordsItemClick();
	            return true;
	        case R.id.homePage_item_stats:
	        	Log.d("Icon click","Stats icon clicks");
	        	statsItemClick();
	            return true;
	        case R.id.homePage_item_profile:
	        	Log.d("Icon click","Profile icon clicks");
	        	profileItemClick();
	            return true;
	        case R.id.homePage_item_settings:
	        	Log.d("Icon click","Setting icon clicks");
	        	settingsItemClick();
	            return true;
	        case R.id.homePage_item_tutorial:
	        	Log.d("Icon click","Tutorial icon clicks");
	        	tutorialItemClick();
	            return true;
	        case R.id.homePage_item_logout:
	        	Log.d("Icon click","Logout icon clicks");
	        	logoutItemClick();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void addItemsOnBegin() {
		begin = (Spinner) findViewById(R.id.homePage_Spinner_begin);
		ArrayAdapter<CharSequence> beginAdapter = ArrayAdapter.createFromResource(this, R.array.homePage_spinner_begin_array, android.R.layout.simple_spinner_item);
		beginAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		begin.setAdapter(beginAdapter);
	}
	
	public void addItemsOnLanguage() {
		language = (Spinner) findViewById(R.id.homePage_Spinner_chooseLanguage);
		ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this, R.array.homePage_Spinner_language_array, android.R.layout.simple_spinner_item);
		languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		language.setAdapter(languageAdapter);
	}
	
	public void wordsItemClick() {
		HomePage.this.startActivity(new Intent(HomePage.this, WordsPage.class));
	}
	public void statsItemClick() {
		HomePage.this.startActivity(new Intent(HomePage.this, StatsPage.class));
	}
	public void profileItemClick() {
		HomePage.this.startActivity(new Intent(HomePage.this, ProfilePage.class));
	}
	public void settingsItemClick() {
		HomePage.this.startActivity(new Intent(HomePage.this, SettingsPage.class));
	}
	public void tutorialItemClick() {
		HomePage.this.startActivity(new Intent(HomePage.this, TutorialPage.class));
	}
	public void logoutItemClick() {
		
	}
	

	public void testPageButtonClick() {
		String taskPage = begin.getSelectedItem().toString();
		Log.d("Task",taskPage);
		Class targetClass = HomePage.class;
		if(taskPage.equals("Review")) {
			targetClass = Review.class;
		} else if(taskPage.equals("Self evaluation")) {
			targetClass = SelfEvaluate.class;
		} else if(taskPage.equals("Type evaluation")) {
			targetClass = TypeEvaluate.class;
		} else if(taskPage.equals("Hearing")) {
			targetClass = Hearing.class;
		}
		
		HomePage.this.startActivity(new Intent(HomePage.this, targetClass));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		 case R.id.homePage_Button_testPageGo:
			 testPageButtonClick();
			 break;
		 }
	}

	

}
