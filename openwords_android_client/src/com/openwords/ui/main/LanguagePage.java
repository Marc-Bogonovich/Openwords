package com.openwords.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.UserLearningLanguages;
import com.openwords.services.implementations.GetLanguages;
import com.openwords.services.implementations.GetLearnableLanguages;
import com.openwords.services.implementations.SetUserLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LanguagePage extends Activity {

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
        listAdapter = new ListAdapterLanguageItem(this, R.layout.list_item_language, localLanguages);
        listView.setAdapter(listAdapter);
        refreshList();

        doneButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (doneChosen) {
                    new UserLearningLanguages(DataPool.BaseLanguage, new Gson().toJson(DataPool.CurrentLearningLanguages)).save();

                    if (DataPool.UserId <= 0) {
                        MyQuickToast.showShort(LanguagePage.this, "user id is corrupt");
                        return;
                    }

                    MyDialogHelper.tryShowQuickProgressDialog(LanguagePage.this, "Saving your preference to server...");

                    new SetUserLanguages().doRequest(new RequestParamsBuilder()
                            .addParam("userId", String.valueOf(DataPool.UserId))
                            .addParam("langOneId", String.valueOf(DataPool.BaseLanguage))
                            .addParam("langTwoIds", new Gson().toJson(DataPool.CurrentLearningLanguages))
                            .getParams(),
                            new HttpResultHandler() {

                                public void hasResult(Object resultObject) {
                                    MyDialogHelper.tryDismissQuickProgressDialog();
                                    finish();
                                }

                                public void noResult(String errorMessage) {
                                    MyQuickToast.showShort(LanguagePage.this, errorMessage);
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
        MyDialogHelper.tryShowQuickProgressDialog(this, "Connection to server...");
        listAdapter.clear();
        localLanguages = Language.listAll(Language.class);
        listAdapter.addAll(localLanguages);
        listAdapter.notifyDataSetChanged();
        checkAndMergeNewLanguages();
    }

    private void showSimpleList() {
        List<String> chosenLangNames = new LinkedList<String>();
        for (Language lang : localLanguages) {
            if (DataPool.CurrentLearningLanguages.contains(lang.langId)) {
                chosenLangNames.add(lang.name);
            }
        }
        if (chosenLangNames.isEmpty()) {
            MyQuickToast.showShort(LanguagePage.this, "You must select at least one language");
            return;
        }
        topText.setText("You have chosen:");
        doneButton.setText("Ok");
        listView2 = (ListView) findViewById(R.id.act_language_list_view2);
        listView2.setDivider(null);
        listView2.setDividerHeight(0);
        listView2.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_language_simple, R.id.list_item_lang_simple_text, chosenLangNames));
        listView.setVisibility(View.GONE);
        listView2.setVisibility(View.VISIBLE);
        doneChosen = true;
    }

    private void checkAndMergeNewLanguages() {
        new GetLearnableLanguages().doRequest(new RequestParamsBuilder()
                .addParam("langOneId", String.valueOf(DataPool.BaseLanguage))
                .getParams(), new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        MyDialogHelper.tryDismissQuickProgressDialog();
                        @SuppressWarnings("unchecked")
                        List<Integer> learnableIds = (List<Integer>) resultObject;
                        if (learnableIds.isEmpty()) {
                            MyQuickToast.showShort(LanguagePage.this,
                                    "Sorry, seems we don't have learnable languages for your local language yet,\n"
                                    + "but please have a look round");
                            finish();
                            return;
                        }

                        String sqlIds = learnableIds.toString().replace("[", "(").replace("]", ")");

                        String sql = "SELECT * FROM LANGUAGE WHERE LANG_ID IN " + sqlIds;
                        List<Language> localSameLangs = Language.findWithQuery(Language.class, sql);
                        MyQuickToast.showShort(LanguagePage.this, "total local same: " + localSameLangs.size());

                        Set<Integer> newLangIds = new HashSet<Integer>(learnableIds);
                        for (Language lang : localSameLangs) {
                            if (newLangIds.contains(lang.langId)) {
                                newLangIds.remove(lang.langId);
                            }
                        }
                        MyQuickToast.showShort(LanguagePage.this, "total new: " + newLangIds.size());

                        if (!newLangIds.isEmpty()) {
                            getAndSaveLanguageInformation(newLangIds);
                        } else {
                            MyQuickToast.showShort(LanguagePage.this, "no new learnable languages");
                        }
                    }

                    public void noResult(String errorMessage) {
                        MyDialogHelper.tryDismissQuickProgressDialog();
                        MyQuickToast.showShort(LanguagePage.this, "Cannot connect to server: " + errorMessage);
                    }
                });
    }

    private void getAndSaveLanguageInformation(Collection<Integer> langIds) {
        new GetLanguages().doRequest(new RequestParamsBuilder()
                .addParam("include", new Gson().toJson(langIds))
                .getParams(), new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        @SuppressWarnings("unchecked")
                        List<Language> langs = (List<Language>) resultObject;
                        Language.saveInTx(langs);
                        listAdapter.clear();
                        localLanguages = Language.listAll(Language.class);
                        listAdapter.addAll(localLanguages);
                        listAdapter.notifyDataSetChanged();
                        MyQuickToast.showShort(LanguagePage.this, "local total: " + Language.count(Language.class));
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(LanguagePage.this, "not ok: " + errorMessage);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        //Do nothing. So force user to chose the languages.
    }
}
