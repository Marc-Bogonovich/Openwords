package com.openwords.ui.other;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.UserLanguage;
import com.openwords.services.implementations.ServiceSetLanguagePage;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.ui.main.LocalOptionPage;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.util.ui.CallbackCancelButton;
import com.openwords.util.ui.CallbackOkButton;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.ArrayList;
import java.util.List;

public class SettingsPage extends Activity {

    private RadioGroup portalPageRadioGroup;
    private Spinner wsaSpinner, leafCardSizeSpinner, languagePageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        BackIcons.enable(this);
        portalPageRadioGroup = (RadioGroup) findViewById(R.id.settingsPage_RadioGroup_portalPage);
        if (OpenwordsSharedPreferences.getHidePortal()) {
            portalPageRadioGroup.check(R.id.settingsPage_RadioButton_hidePortalPage);
        } else {
            portalPageRadioGroup.check(R.id.settingsPage_RadioButton_displayPortalPage);
        }

        addWSASpinner();
        addLeafCardSizeSpinner();
        makeLanguagePageSpinner();

        Button b1 = (Button) findViewById(R.id.settingsPage_button1);
        b1.setText(LocalizationManager.getTextLangOptionChange());
        b1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                LocalOptionPage.showOptionDialogs(SettingsPage.this);
            }
        });
    }

    private void makeLanguagePageSpinner() {
        if (DataPool.LmLearningLang < 0) {
            MyQuickToast.showShort(this, "You have no languages");
            return;
        }
        TextView t = (TextView) findViewById(R.id.settingsPage_TextView1);
        t.setText("Change your current language set number:");
        languagePageSpinner = (Spinner) findViewById(R.id.settingsPage_Spinner1);

        final UserLanguage languageInfo = UserLanguage.getUserLanguageInfo(LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang);
        if (languageInfo == null) {
            MyQuickToast.showShort(this, "No language is selected");
            return;
        }
        final int currentPage = languageInfo.page;
        int minPage = currentPage - 5;
        if (minPage <= 0) {
            minPage = 1;
        }
        int maxPage = currentPage + 4;

        final List<String> list = new ArrayList<String>(10);
        for (int i = minPage; i <= maxPage; i++) {
            list.add(String.valueOf(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagePageSpinner.setAdapter(dataAdapter);
        languagePageSpinner.setSelection(list.indexOf(String.valueOf(currentPage)));

        final boolean[] omitSelect = new boolean[]{true};
        languagePageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position, long id) {
                if (omitSelect[0]) {
                    omitSelect[0] = false;
                    return;
                }

                MyDialogHelper.showConfirmDialog(SettingsPage.this, "Change Language Set Number",
                        "Are you sure you want to change your current learning language set number?\n\n"
                        + "We actually recommend you learn the words one set by one set.",
                        new CallbackOkButton() {

                            public void okPressed() {
                                final int chosenPage = Integer.parseInt(list.get(position));

                                new ServiceSetLanguagePage().doRequest(LocalSettings.getUserId(), languageInfo.baseLang, languageInfo.learningLang, chosenPage,
                                        new HttpResultHandler() {

                                            public void hasResult(Object resultObject) {
                                                languageInfo.page = chosenPage;
                                                languageInfo.save();
                                                MyQuickToast.showShort(SettingsPage.this, "ok: " + chosenPage);
                                            }

                                            public void noResult(String errorMessage) {
                                                omitSelect[0] = true;
                                                languagePageSpinner.setSelection(list.indexOf(String.valueOf(currentPage)));
                                                MyQuickToast.showShort(SettingsPage.this, "Error: " + errorMessage);
                                            }
                                        });

                            }
                        },
                        new CallbackCancelButton() {

                            public void cancelPressed() {
                                omitSelect[0] = true;
                                languagePageSpinner.setSelection(list.indexOf(String.valueOf(currentPage)));
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
        wsaSpinner.setVisibility(View.GONE);
//        List<WSAinterface> algList = OpenwordsSharedPreferences.getWordSelectionAlgList();
//        List<String> list = new ArrayList<String>();
//        for (WSAinterface alg : algList) {
//            list.add(alg.toString());
//        }
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        wsaSpinner.setAdapter(dataAdapter);
//        wsaSpinner.setSelection(OpenwordsSharedPreferences.getAlgIndex());
//        wsaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                OpenwordsSharedPreferences.setAlgIndex(position);
//                Toast.makeText(parentView.getContext(),
//                        "Selected WSA : " + parentView.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//
//            }
//
//        });
    }

    private void addLeafCardSizeSpinner() {
        leafCardSizeSpinner = (Spinner) findViewById(R.id.settingsPage_Spinner_leafCardSize);
        leafCardSizeSpinner.setVisibility(View.GONE);
//        List<String> list = new ArrayList<String>();
//        for (int i = 5; i < 15; i++) {
//            list.add(Integer.toString(i));
//        }
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        leafCardSizeSpinner.setAdapter(dataAdapter);
//        leafCardSizeSpinner.setSelection(list.indexOf(Integer.toString(OpenwordsSharedPreferences.getLeafCardSize())));
//        leafCardSizeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                OpenwordsSharedPreferences.setLeafCardSize(Integer.parseInt(parentView.getItemAtPosition(position).toString()));
//                Toast.makeText(parentView.getContext(),
//                        "Selected leaf-card size: " + parentView.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//
//            }
//
//        });
    }
}
