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
import com.openwords.util.ui.MyQuickToast;
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

        final Language lang = itemContent.get(position);
        if (lang != null) {
            final CheckBox itemObj = (CheckBox) view.findViewById(R.id.list_item_lang_1);
            TextView itemObj2 = (TextView) view.findViewById(R.id.list_item_lang_2);
            itemObj2.setText(lang.name);
            itemObj.setChecked(lang.chosen);
            itemObj.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    lang.chosen = !lang.chosen;
                    lang.save();
                    itemObj.setChecked(lang.chosen);
                    MyQuickToast.showShort(context, lang.chosen + " " + lang.langId + " " + lang.name);
                }
            });
        }
        return view;
    }
}
