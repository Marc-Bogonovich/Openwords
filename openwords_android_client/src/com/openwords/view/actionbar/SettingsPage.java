package com.openwords.view.actionbar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.openwords.R;
import com.openwords.util.WSAinterface;
import com.openwords.util.preference.OpenwordsSharedPreferences;

public class SettingsPage extends Activity {

    private Button buttonTest;
    private Spinner wsaSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        //BackIcons.builder(this);
        ActionBarBuilder actionBar = new ActionBarBuilder(this, ActionBarBuilder.Home_Page);
        buttonTest = (Button) findViewById(R.id.settingsPage_button_test);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean hide = OpenwordsSharedPreferences.getHidePortal();
                OpenwordsSharedPreferences.setHidePortal(!hide);
                Toast.makeText(SettingsPage.this, "Hide Portal is set to " + hide, Toast.LENGTH_LONG).show();
            }
        });
        
        addWSASpinner();
    }
    
    private void addWSASpinner() {
    	 
    	wsaSpinner = (Spinner) findViewById(R.id.settingsPage_Spinner_WSA);
    	List<WSAinterface> algList = OpenwordsSharedPreferences.getWordSelectionAlgList();
    	List<String> list = new ArrayList<String>();
    	for(WSAinterface alg : algList) {
    		list.add(alg.toString());
    	}
    	
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	wsaSpinner.setAdapter(dataAdapter);
    	wsaSpinner.setSelection(OpenwordsSharedPreferences.getAlgIndex());
    	wsaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	OpenwordsSharedPreferences.setAlgIndex(position);
            	Toast.makeText(parentView.getContext(), 
            			"Select WSA : " + parentView.getItemAtPosition(position).toString(),
            			Toast.LENGTH_SHORT).show();

            }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
    	
    	});
    	
      }
}
