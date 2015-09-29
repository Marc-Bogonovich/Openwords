package com.openwords.ui.lily.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.SetItem;
import com.openwords.model.Word;
import com.openwords.services.implementations.ServiceSearchWords;
import com.openwords.services.implementations.ServiceSearchWords.Result;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.ui.MyQuickToast;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class PageSetContent extends Activity {

    private ListView itemList;
    private ListAdapterWordSetItem listAdapter;
    private ImageView buttonMode, buttonBack;
    private EditText setTitleInput;
    private TextView setTitle;
    private boolean isModifying, contentHasJustChanged;
    private CopyOnWriteArrayList<SetItem> setItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_lily_wordset_page);
        itemList = (ListView) findViewById(R.id.act_ws_list1);
        setTitle = (TextView) findViewById(R.id.act_ws_text1);
        setTitleInput = (EditText) findViewById(R.id.act_ws_edit1);
        buttonMode = (ImageView) findViewById(R.id.act_ws_image_1);
        buttonBack = (ImageView) findViewById(R.id.act_ws_image_2);

        if (DataPool.currentSet == null) {
            isModifying = true;
        } else {
            isModifying = false;
            setTitle.setText(DataPool.currentSet.name);
        }

        buttonMode.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (isModifying) {
                    for (SetItem item : setItems) {
                        item.isModifying = false;
                        if (item.isHead || item.isNew) {
                            setItems.remove(item);
                        }
                    }
                    listAdapter.notifyDataSetChanged();
                    String name = setTitleInput.getText().toString();
                    setTitle.setText(name);
                    setTitle.setVisibility(View.VISIBLE);
                    setTitleInput.setVisibility(View.GONE);
                    if (contentHasJustChanged) {
                        MyQuickToast.showShort(PageSetContent.this, "Saving...");
                    }
                } else {
                    for (SetItem item : setItems) {
                        item.isModifying = true;
                    }
                    setItems.add(new SetItem(0, "(Native Lang)", "(Learning Lang)", true, true));
                    listAdapter.notifyDataSetChanged();
                    setTitleInput.setText(DataPool.currentSet.name);
                    setTitle.setVisibility(View.GONE);
                    setTitleInput.setVisibility(View.VISIBLE);
                    contentHasJustChanged = false;
                }
                isModifying = !isModifying;
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PageSetContent.super.onBackPressed();
            }
        });

        setItems = new CopyOnWriteArrayList<SetItem>();
        listAdapter = new ListAdapterWordSetItem(this, setItems);
        itemList.setAdapter(listAdapter);
        setTitleInput.setHint("Input Set Name");
        setTitleInput.setVisibility(View.GONE);
    }

    private void refreshListView(List<SetItem> items) {
        listAdapter.clear();
        listAdapter.addAll(items);
        listAdapter.notifyDataSetChanged();
    }

    public void search() {
        for (SetItem item : setItems) {
            if (item.isNew) {
                setItems.remove(item);
            }
        }
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m != null) {
            m.hideSoftInputFromWindow(itemList.getWindowToken(), 0);
        }
        new ServiceSearchWords().doRequest(10, 206, 98, "典", new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                Result r = (Result) resultObject;
                Map<Long, String> wordForms = new HashMap<Long, String>(r.searchResult.size() + r.targetResult.size());
                for (Word w : r.targetResult) {
                    wordForms.put(w.wordId, w.word);
                }
                for (Word w : r.searchResult) {
                    wordForms.put(w.wordId, w.word);
                }
                for (Entry<Long, Set<Long>> e : r.linkedSearchWords.entrySet()) {
                    if (r.linkedTargetWords.containsKey(e.getKey())) {
                        String searchWord = "";
                        int i = 1;
                        for (Long searchWordId : e.getValue()) {
                            if (i == 2) {
                                searchWord = searchWord.replace(",", "");
                                searchWord += " (";
                            }
                            searchWord += wordForms.get(searchWordId) + ",";
                            i++;
                        }
                        if (i > 2) {
                            searchWord += ")";
                        }

                        for (Long targetWordId : r.linkedTargetWords.get(e.getKey())) {
                            setItems.add(new SetItem(0, searchWord,
                                    wordForms.get(targetWordId),
                                    false, true, true));
                        }
                    }
                }
                listAdapter.notifyDataSetChanged();
                if (r.errorMessage != null) {
                    MyQuickToast.showShort(PageSetContent.this, r.errorMessage);
                }
            }

            public void noResult(String errorMessage) {
                MyQuickToast.showShort(PageSetContent.this, errorMessage);
            }
        });
    }

    public void addSetItemFromSearch(SetItem item) {
        contentHasJustChanged = true;
        setItems.remove(item);
        for (int i = 0; i < setItems.size(); i++) {
            if (setItems.get(i).isHead) {
                item.isNew = false;
                setItems.add(i, item);
                listAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView(DataPool.currentSetItems);
    }
}
