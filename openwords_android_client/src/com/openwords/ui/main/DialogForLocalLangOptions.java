package com.openwords.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.openwords.R;
import java.util.List;

/**
 *
 * @author hanaldo
 */
public class DialogForLocalLangOptions extends Dialog {

    private ListView listView;

    public DialogForLocalLangOptions(Context context, String title, List<String> options, OnItemClickListener onItemClickListener) {
        super(context);
        setContentView(R.layout.dialog_local_lang_options);
        TextView ti = (TextView) findViewById(android.R.id.title);
        ti.setSingleLine(false);
        setTitle(title);

        listView = (ListView) findViewById(R.id.dialog_localLangOptions_list1);
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                options));
        listView.setOnItemClickListener(onItemClickListener);
        listView.setItemsCanFocus(false);
    }
}
