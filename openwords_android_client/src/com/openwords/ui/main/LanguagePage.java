package com.openwords.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.UserLearningLanguages;
import com.openwords.services.implementations.SetUserLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.gson.MyGson;
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
                    if (DataPool.getLocalSettings().getUserId() <= 0) {
                        MyQuickToast.showShort(LanguagePage.this, "user id is corrupt");
                        return;
                    }
                    UserLearningLanguages.saveUserLearningLanguagesToLocal(DataPool.getLocalSettings().getBaseLanguageId(),
                            ChosenLangIds);

                    MyDialogHelper.tryShowQuickProgressDialog(LanguagePage.this, "Saving your preference to server...");

                    new SetUserLanguages().doRequest(new RequestParamsBuilder()
                            .addParam("userId", String.valueOf(DataPool.getLocalSettings().getUserId()))
                            .addParam("langOneId", String.valueOf(DataPool.getLocalSettings().getBaseLanguageId()))
                            .addParam("langTwoIds", MyGson.toJson(ChosenLangIds))
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
        listAdapter.clear();
        localLanguages = Language.listAll(Language.class);
        List<Integer> chosen = UserLearningLanguages.loadUserLearningLanguagesFromLocal(DataPool.getLocalSettings().getBaseLanguageId());
        ChosenLangIds.clear();
        ChosenLangIds.addAll(chosen);
        listAdapter.addAll(localLanguages);
        listAdapter.notifyDataSetChanged();

        Language.checkAndMergeNewLanguages(this, DataPool.getLocalSettings().getBaseLanguageId(), new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                listAdapter.clear();
                localLanguages = Language.listAll(Language.class);
                listAdapter.addAll(localLanguages);
                listAdapter.notifyDataSetChanged();
            }

            public void noResult(String errorMessage) {
                MyQuickToast.showShort(LanguagePage.this, "checkAndMergeNewLanguages no result");
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

    @Override
    public void onBackPressed() {
        //Do nothing. So force user to chose the languages.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChosenLangIds.clear();
        MyQuickToast.showShort(this, "ChosenLangIds cleared");
    }
}
