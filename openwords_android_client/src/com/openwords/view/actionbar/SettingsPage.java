package com.openwords.view.actionbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.ui.main.LocalOptionPage;
import com.openwords.util.WSAinterface;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class SettingsPage extends Activity {

    private RadioGroup portalPageRadioGroup;
    private Spinner wsaSpinner, leafCardSizeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        //BackIcons.builder(this);
        BackIcons.builder(this);
        portalPageRadioGroup = (RadioGroup) findViewById(R.id.settingsPage_RadioGroup_portalPage);
        if (OpenwordsSharedPreferences.getHidePortal()) {
            portalPageRadioGroup.check(R.id.settingsPage_RadioButton_hidePortalPage);
        } else {
            portalPageRadioGroup.check(R.id.settingsPage_RadioButton_displayPortalPage);
        }

        addWSASpinner();
        addLeafCardSizeSpinner();

        Button b1 = (Button) findViewById(R.id.settingsPage_button1);
        b1.setText(LocalizationManager.getTextLangOptionChange());
        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                LocalOptionPage.showOptionDialogs(SettingsPage.this);
            }
        });
    }

    //the name of this function is defined in XML radio buttons
    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.settingsPage_RadioButton_displayPortalPage:
                OpenwordsSharedPreferences.setHidePortal(false);
                Toast.makeText(SettingsPage.this, "Display portal page icon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settingsPage_RadioButton_hidePortalPage:
                OpenwordsSharedPreferences.setHidePortal(true);
                Toast.makeText(SettingsPage.this, "Hide portal page icon", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void addWSASpinner() {

        wsaSpinner = (Spinner) findViewById(R.id.settingsPage_Spinner_WSA);
        List<WSAinterface> algList = OpenwordsSharedPreferences.getWordSelectionAlgList();
        List<String> list = new ArrayList<String>();
        for (WSAinterface alg : algList) {
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
                        "Selected WSA : " + parentView.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

    }

    private void addLeafCardSizeSpinner() {

        leafCardSizeSpinner = (Spinner) findViewById(R.id.settingsPage_Spinner_leafCardSize);
        List<String> list = new ArrayList<String>();
        for (int i = 5; i < 15; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leafCardSizeSpinner.setAdapter(dataAdapter);
        leafCardSizeSpinner.setSelection(list.indexOf(Integer.toString(OpenwordsSharedPreferences.getLeafCardSize())));
        leafCardSizeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                OpenwordsSharedPreferences.setLeafCardSize(Integer.parseInt(parentView.getItemAtPosition(position).toString()));
                Toast.makeText(parentView.getContext(),
                        "Selected leaf-card size: " + parentView.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

    }
}
