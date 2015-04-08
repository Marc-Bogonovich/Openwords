package com.openwords.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.openwords.R;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.other.ActionBarBuilder;
import com.openwords.ui.other.DialogSearchWords;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.ui.MyQuickToast;

public class WordsPage extends Activity implements OnClickListener {

    private ActionBarBuilder actionBar;
    private Button searchWords, searchWordSets, viewMyWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_page);

        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Words_Page);

        searchWords = (Button) findViewById(R.id.wordsPage_Button_searchWords);
        searchWords.setOnClickListener(this);
        searchWordSets = (Button) findViewById(R.id.wordsPage_Button_searchWordSets);
        searchWordSets.setOnClickListener(this);
        viewMyWords = (Button) findViewById(R.id.wordsPage_Button_viewMyWords);
        viewMyWords.setOnClickListener(this);

    }

    private void fillUI() {
        searchWords.setText(LocalizationManager.getTextWordsSearch());
        searchWordSets.setText(LocalizationManager.getTextWordsSets());
        viewMyWords.setText(LocalizationManager.getTextWordsView());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wordsPage_Button_getNextWords:
                break;
            case R.id.wordsPage_Button_searchWords:
                new DialogSearchWords(this).show();
                break;
            case R.id.wordsPage_Button_searchWordSets:
                MyQuickToast.showShort(this, "Not supported yet");
                break;
            case R.id.wordsPage_Button_viewMyWords:
                MyQuickToast.showShort(this, "Not supported yet");
                break;
            case R.id.wordsPage_ImageView_syncButton:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.checkSetting();
        fillUI();
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtMainPages(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                WordsPage.super.onBackPressed();
            }
        });
    }
}
