package com.openwords.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.Performance;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.services.implementations.ServiceGetWordConnectionsByLangOne;
import com.openwords.services.implementations.ServiceGetWordConnectionsByLangOne.Result;
import com.openwords.services.implementations.ServiceSetUserPerformance;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.ui.MyQuickToast;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DialogSearchWords extends Dialog {

    private ListView listView;
    private ListAdapterConnectionItem listAdapter;
    private Button buttonClear, buttonSearch, buttonAdd, buttonPrev, buttonNext;
    private TextView pageNumber;
    private EditText input;
    private Context context;
    private Set<Integer> chosen;
    private int currentPage = 1;

    public DialogSearchWords(Context c) {
        super(c);
        context = c;
        setContentView(R.layout.dialog_search_words);
        Language lang = Language.getLanguageInfo(DataPool.LmLearningLang);
        setTitle(lang.name);
        //setTitle("Search " + lang.name + " by English");

        listView = (ListView) findViewById(R.id.dialog_search_word_list1);
        buttonClear = (Button) findViewById(R.id.dialog_search_word_button1);
        buttonSearch = (Button) findViewById(R.id.dialog_search_word_button2);
        buttonAdd = (Button) findViewById(R.id.dialog_search_word_button3);
        input = (EditText) findViewById(R.id.dialog_search_word_text1);
        pageNumber = (TextView) findViewById(R.id.dialog_search_word_text2);
        buttonPrev = (Button) findViewById(R.id.dialog_search_word_button4);
        buttonNext = (Button) findViewById(R.id.dialog_search_word_button5);

        input.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {
                currentPage = 1;
                buttonPrev.setEnabled(false);
                buttonNext.setEnabled(false);
            }

            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable edtbl) {
            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clickButton1();
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clickButton2();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clickButton3();
            }
        });
        buttonPrev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clickButton4();
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clickButton5();
            }
        });

        chosen = new HashSet<Integer>(10);
        listView.setItemsCanFocus(false);

        buttonPrev.setEnabled(false);
        buttonNext.setEnabled(false);
        buttonAdd.setEnabled(false);
    }

    private void clickButton1() {
        input.setText(null);
    }

    private void clickButton2() {
        doSearchWords(1);
    }

    private void clickButton3() {
        buttonAdd.setEnabled(false);
        List<Performance> perfs = new LinkedList<Performance>();
        for (Integer connectionId : chosen) {
            perfs.add(new Performance(connectionId, "all", "new", 1));
        }
        new ServiceSetUserPerformance().doRequest(LocalSettings.getUserId(), true, perfs, "all",
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        MyQuickToast.showShort(context, "Your words are added.");
                        buttonAdd.setEnabled(true);
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(context, "Cannot add your chosen words.");
                        buttonAdd.setEnabled(true);
                    }
                });
    }

    private void clickButton4() {
        doSearchWords(currentPage - 1);
    }

    private void clickButton5() {
        doSearchWords(currentPage + 1);
    }

    private void doSearchWords(int page) {
        String text = input.getText().toString().trim();
        if (text.isEmpty()) {
            MyQuickToast.showShort(context, "Please type something");
        } else {
            chosen.clear();
            searchWords(text, page);
        }
    }

    private void searchWords(String form, int page) {
        buttonPrev.setEnabled(false);
        buttonNext.setEnabled(false);
        buttonSearch.setEnabled(false);
        buttonAdd.setEnabled(false);
        new ServiceGetWordConnectionsByLangOne().doRequest(form, LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang, page, 6,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        Result r = (Result) resultObject;
                        currentPage = r.pageNumber;
                        int currentTotal = currentPage * r.pageSize;
                        pageNumber.setText("Page " + r.pageNumber);
                        if (currentTotal < r.total) {
                            buttonNext.setEnabled(true);
                        } else {
                            buttonNext.setEnabled(false);
                        }
                        if (currentPage > 1) {
                            buttonPrev.setEnabled(true);
                        } else {
                            buttonPrev.setEnabled(false);
                        }

                        refreshListView(r.connections, r.words);
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(context, errorMessage);
                        refreshListView(null, null);
                    }
                });

    }

    private void refreshListView(List<WordConnection> connections, List<Word> words) {
        if (connections != null && words != null) {
            listAdapter = new ListAdapterConnectionItem(context, connections, words, chosen, new ListChoiceCallback() {

                public void changed() {
                    if (chosen.isEmpty()) {
                        buttonAdd.setEnabled(false);
                    } else {
                        buttonAdd.setEnabled(true);
                    }
                }
            });
            listView.setAdapter(listAdapter);
        } else {
            MyQuickToast.showShort(context, "No result");
        }
        buttonSearch.setEnabled(true);
    }

    public interface ListChoiceCallback {

        public void changed();
    }

}
