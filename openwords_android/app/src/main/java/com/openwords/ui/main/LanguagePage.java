package com.openwords.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.ResultLanguage;
import com.openwords.model.ResultUserLanguage;
import com.openwords.model.UserLanguage;
import com.openwords.services.implementations.ServiceSetUserLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.LinkedList;
import java.util.List;

public class LanguagePage extends Activity {

    public final static List<Integer> ChosenLangIds = new LinkedList<Integer>();
    private ListView listView;
    private ListView listView2;
    private ListAdapterLanguageItem listAdapter;
    private List<Language> localLanguages;
    private TextView topText;
    private Button doneButton;
    private boolean doneChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_page);
        topText = (TextView) findViewById(R.id.act_language_text);
        doneButton = (Button) findViewById(R.id.act_language_button);
        listView = (ListView) findViewById(R.id.act_language_list_view);
        localLanguages = new LinkedList<Language>();
        listAdapter = new ListAdapterLanguageItem(this, localLanguages);
        listView.setAdapter(listAdapter);
        refreshList();

        doneButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (doneChosen) {
                    if (LocalSettings.getUserId() <= 0) {
                        MyQuickToast.showShort(LanguagePage.this, "user id is corrupt");
                        return;
                    }

                    MyDialogHelper.tryShowQuickProgressDialog(LanguagePage.this, LocalizationManager.getBlockConnectServer() + "...");

                    new ServiceSetUserLanguages().doRequest(LocalSettings.getUserId(), LocalSettings.getBaseLanguageId(), ChosenLangIds,
                            new HttpResultHandler() {

                                public void hasResult(Object resultObject) {
                                    UserLanguage.syncUserLanguage(
                                            LocalSettings.getUserId(),
                                            LocalSettings.getBaseLanguageId(),
                                            new ResultUserLanguage() {

                                                public void result(List<UserLanguage> result) {
                                                    MyDialogHelper.tryDismissQuickProgressDialog();
                                                    finish();
                                                }
                                            });

                                }

                                public void noResult(String errorMessage) {
                                    LogUtil.logDeubg(this, errorMessage);
                                    MyDialogHelper.tryDismissQuickProgressDialog();
                                    finish();
                                }
                            });

                } else {
                    showSimpleList();
                }
            }
        });
    }

    private void refreshList() {
        MyDialogHelper.tryShowQuickProgressDialog(this, LocalizationManager.getBlockRefreshLang() + "...");
        Language.syncLanguagesData(this, new ResultLanguage() {

            public void result(String resultObject) {
                if (resultObject != null && resultObject.equals(Result_No_Language_Data)) {
                    MyDialogHelper.tryDismissQuickProgressDialog();
                    LogUtil.logDeubg(this, "No language data");
                    finish();
                    return;
                }
                if (resultObject != null && resultObject.equals(Result_No_Server_Response)) {
                    MyDialogHelper.tryDismissQuickProgressDialog();
                    LogUtil.logDeubg(this, "No server response");
                    finish();
                    return;
                }
                refreshUserLearningLanguages();
            }
        });
    }

    private void refreshListView() {
        listAdapter.clear();
        localLanguages = Language.listAll(Language.class);
        listAdapter.addAll(localLanguages);
        listAdapter.notifyDataSetChanged();
        MyDialogHelper.tryDismissQuickProgressDialog();
    }

    private void refreshUserLearningLanguages() {
        ChosenLangIds.clear();
        UserLanguage.syncUserLanguage(
                LocalSettings.getUserId(),
                LocalSettings.getBaseLanguageId(),
                new ResultUserLanguage() {

                    public void result(List<UserLanguage> result) {
                        List<Integer> chosen = UserLanguage.unpackLearningLangIds(result);
                        ChosenLangIds.addAll(chosen);
                        refreshListView();
                    }
                });
    }

    private void showSimpleList() {
        List<String> chosenLangNames = new LinkedList<String>();
        for (Language lang : localLanguages) {
            if (ChosenLangIds.contains(lang.langId)) {
                chosenLangNames.add(lang.name);
            }
        }
        if (chosenLangNames.isEmpty()) {
            MyQuickToast.showShort(LanguagePage.this, LocalizationManager.getErrorOneLang());
            return;
        }
        topText.setText("You have chosen:");
        doneButton.setText(LocalizationManager.getOk());
        listView2 = (ListView) findViewById(R.id.act_language_list_view2);
        listView2.setDivider(null);
        listView2.setDividerHeight(0);
        listView2.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_language_simple, R.id.list_item_lang_simple_text, chosenLangNames));
        listView.setVisibility(View.GONE);
        listView2.setVisibility(View.VISIBLE);
        doneChosen = true;
    }

    @Override
    public void onBackPressed() {
        //Do nothing. So force user to chose the languages.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChosenLangIds.clear();
    }
}
