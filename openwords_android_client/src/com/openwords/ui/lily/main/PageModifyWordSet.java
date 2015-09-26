package com.openwords.ui.lily.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import com.openwords.R;
import java.util.LinkedList;
import java.util.List;

public class PageModifyWordSet extends Activity {

    private ListView itemList;
    private ListAdapterWordSetItem listAdapter;
    private ImageView buttonMode;
    private boolean isModifying;
    private List<SetItem> mySet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_lily_wordset_page);
        itemList = (ListView) findViewById(R.id.act_ws_list1);
        buttonMode = (ImageView) findViewById(R.id.act_ws_image_1);

        buttonMode.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (isModifying) {
                    LinkedList<SetItem> n = new LinkedList<SetItem>();
                    n.add(new SetItem(1, "hello", "你好", false));
                    n.add(new SetItem(2, "free", "自由", false));
                    n.add(new SetItem(3, "dictionary", "字典", false));
                    n.add(new SetItem(4, "new", "新的", false));
                    refreshListView(n);
                } else {
                    LinkedList<SetItem> n = new LinkedList<SetItem>();
                    n.add(new SetItem(1, "hello", "你好", false, true));
                    n.add(new SetItem(2, "free", "自由", false, true));
                    n.add(new SetItem(3, "dictionary", "字典", false, true));
                    n.add(new SetItem(4, "new", "新的", false, true));
                    n.add(new SetItem(0, "(Native Lang)", "(Learning Lang)", true, true));
                    refreshListView(n);
                }
                isModifying = !isModifying;
            }
        });

        listAdapter = new ListAdapterWordSetItem(this, new LinkedList<SetItem>());
        itemList.setAdapter(listAdapter);
    }

    private void refreshListView(LinkedList<SetItem> items) {
        listAdapter.clear();
        listAdapter.addAll(items);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinkedList<SetItem> n = new LinkedList<SetItem>();
        n.add(new SetItem(1, "hello", "你好", false));
        n.add(new SetItem(2, "free", "自由", false));
        n.add(new SetItem(3, "dictionary", "字典", false));
        n.add(new SetItem(4, "new", "新的", false));
        refreshListView(n);
    }
}
