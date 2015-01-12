package com.openwords.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.Language;
import java.util.List;

public class ListAdapterLanguageItem extends ArrayAdapter<Language> {

    private List<Language> itemContent;
    private Context context;
    private int listItemResourceId;

    public ListAdapterLanguageItem(Context context, int listItemResourceId, List<Language> objects) {
        super(context, listItemResourceId, objects);
        this.context = context;
        this.listItemResourceId = listItemResourceId;
        itemContent = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(listItemResourceId, null);
        }

        Language lang = itemContent.get(position);
        if (lang != null) {
            TextView itemObj = (TextView) view.findViewById(R.id.code);
            CheckBox itemObj2 = (CheckBox) view.findViewById(R.id.checkBox1);
            itemObj.setText(lang.name);
            itemObj2.setChecked(lang.chosen);
        }
        return view;
    }
}
