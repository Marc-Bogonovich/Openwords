package com.openwords.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.services.implementations.ServiceGetWordConnectionsByLangOne;
import com.openwords.services.implementations.ServiceGetWordConnectionsByLangOne.Result;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DialogSearchWords extends Dialog {

    private ListView listView;
    private ListAdapterConnectionItem listAdapter;
    private Button button1, button2, button3;
    private EditText input;
    private Context context;
    private List<WordConnection> connections;
    private List<Word> words;
    private Set<Integer> chosen;
    private int total;

    public DialogSearchWords(Context c) {
        super(c);
        context = c;
        setContentView(R.layout.dialog_search_words);
        setTitle("Search Words for " + DataPool.LmLearningLang);

        listView = (ListView) findViewById(R.id.dialog_search_word_list1);
        button1 = (Button) findViewById(R.id.dialog_search_word_button1);
        button2 = (Button) findViewById(R.id.dialog_search_word_button2);
        button3 = (Button) findViewById(R.id.dialog_search_word_button3);
        input = (EditText) findViewById(R.id.dialog_search_word_text1);

        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clickButton1();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clickButton2();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clickButton3();
            }
        });

        chosen = new HashSet<Integer>(10);
        words = new LinkedList<Word>();
        connections = new LinkedList<WordConnection>();

        listAdapter = new ListAdapterConnectionItem(context, connections, words, chosen);
        listView.setAdapter(listAdapter);
        listView.setItemsCanFocus(false);
    }

    private void clickButton1() {
    }

    private void clickButton2() {
        String text = input.getText().toString().trim();
        if (text.isEmpty()) {
            MyQuickToast.showShort(context, "Please type something");
        } else {
            chosen.clear();
            searchWords(text);
        }
    }

    private void clickButton3() {
    }

    private void searchWords(String form) {
        MyDialogHelper.tryShowQuickProgressDialog(context, "Search words data...");
        new ServiceGetWordConnectionsByLangOne().doRequest(form, LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang, 1, 10,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        Result r = (Result) resultObject;
                        words.clear();
                        words.addAll(r.words);
                        total = r.total;
                        refreshListView(r.connections);
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(context, errorMessage);
                        total = 0;
                        words.clear();
                        refreshListView(null);
                    }
                });

    }

    private void refreshListView(List<WordConnection> connections) {
        MyQuickToast.showShort(context, "refreshListView");
        listAdapter.clear();
        if (connections != null) {
            this.connections = connections;
            listAdapter.addAll(this.connections);
        } else {
            MyQuickToast.showShort(context, "No result");
        }
        listAdapter.notifyDataSetChanged();

        MyDialogHelper.tryDismissQuickProgressDialog();
    }

}
