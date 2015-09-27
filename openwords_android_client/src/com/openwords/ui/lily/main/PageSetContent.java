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
import java.util.LinkedList;
import java.util.List;

public class PageSetContent extends Activity {

    private ListView itemList;
    private ListAdapterWordSetItem listAdapter;
    private ImageView buttonMode, buttonBack;
    private EditText setTitleInput;
    private TextView setTitle;
    private boolean isModifying;
    private List<SetItem> setItems;

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
                        if (item.isHead) {
                            setItems.remove(item);
                        }
                    }
                    listAdapter.notifyDataSetChanged();
                    String name = setTitleInput.getText().toString();
                    setTitle.setText(name);
                    setTitle.setVisibility(View.VISIBLE);
                    setTitleInput.setVisibility(View.GONE);
                } else {
                    for (SetItem item : setItems) {
                        item.isModifying = true;
                    }
                    setItems.add(new SetItem(0, "(Native Lang)", "(Learning Lang)", true, true));
                    listAdapter.notifyDataSetChanged();
                    setTitleInput.setText(DataPool.currentSet.name);
                    setTitle.setVisibility(View.GONE);
                    setTitleInput.setVisibility(View.VISIBLE);
                }
                isModifying = !isModifying;
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PageSetContent.super.onBackPressed();
            }
        });

        setItems = new LinkedList<SetItem>();
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
        setItems.add(new SetItem(4, "test", "测试", false, true, true));
        listAdapter.notifyDataSetChanged();
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m != null) {
            m.hideSoftInputFromWindow(itemList.getWindowToken(), 0);
        }
    }

    public void addSetItemFromSearch(SetItem item) {
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
