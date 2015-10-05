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
import com.openwords.model.LocalSettings;
import com.openwords.util.ui.MyQuickToast;
import java.util.List;

public class ListAdapterLanguageItem extends ArrayAdapter<Language> {

    private final List<Language> itemContent;
    private final Context context;

    public ListAdapterLanguageItem(Context context, List<Language> objects) {
        super(context, R.layout.list_item_language, objects);
        this.context = context;
        itemContent = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.list_item_language, null);
        }

        final Language lang = itemContent.get(position);
        if (lang != null) {
            final CheckBox itemObj = (CheckBox) view.findViewById(R.id.list_item_lang_1);
            TextView itemObj2 = (TextView) view.findViewById(R.id.list_item_lang_2);
            itemObj2.setText(lang.name);
            itemObj.setChecked(LanguagePage.ChosenLangIds.contains(lang.langId));
            itemObj.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    if (LanguagePage.ChosenLangIds.contains(lang.langId)) {
                        LanguagePage.ChosenLangIds.remove(Integer.valueOf(lang.langId));
                        itemObj.setChecked(false);
                    } else {
                        LanguagePage.ChosenLangIds.add(lang.langId);
                        itemObj.setChecked(true);
                    }

                    if (lang.langId == LocalSettings.getBaseLanguageId()) {
                        MyQuickToast.showShort(context, "You cannot learn your native language with your native language.");
                        itemObj.setChecked(false);
                        LanguagePage.ChosenLangIds.remove(Integer.valueOf(lang.langId));
                        return;
                    }
                    if (LanguagePage.ChosenLangIds.size() > 1) {
                        MyQuickToast.showShort(context, "Sorry, we only support learning ONE language currently.");
                        itemObj.setChecked(false);
                        LanguagePage.ChosenLangIds.remove(Integer.valueOf(lang.langId));
                        return;
                    }
                    //MyQuickToast.showShort(context, itemObj.isChecked() + " " + lang.langId + " " + lang.name);
                }
            });
        }
        return view;
    }
}
