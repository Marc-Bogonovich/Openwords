package com.openwords.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.openwords.R;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author hanaldo
 */
public class DialogForSettingSelection extends Dialog {

    private final Context context;
    private final List<String> items;
    private ListView listView;

    public DialogForSettingSelection(Context context) {
        super(context);
        this.context = context;
        items = new LinkedList<String>();
    }

    public DialogForSettingSelection build(OnItemClickListener onItemClickListener) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setting_selection);

        listView = (ListView) findViewById(R.id.dialog_setting_selection_list1);
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                items));
        listView.setOnItemClickListener(onItemClickListener);
        listView.setItemsCanFocus(false);
        return this;
    }

    public DialogForSettingSelection addItem(String name) {
        items.add(name);
        return this;
    }
}
