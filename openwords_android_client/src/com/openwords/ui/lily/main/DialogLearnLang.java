package com.openwords.ui.lily.main;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.openwords.R;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.UserLanguage;
import com.openwords.util.ui.CallbackOkButton;
import com.openwords.util.ui.MyDialogHelper;
import java.util.LinkedList;
import java.util.List;

public class DialogLearnLang extends Dialog {

    private final List<String> items;

    public DialogLearnLang(final Context context, final LanguagePicked callback) {
        super(context);
        items = new LinkedList<String>();
        setContentView(R.layout.dialog_learn_lang);
        setTitle("Pick the language to learn");

        final List<UserLanguage> langs = UserLanguage.loadUserLanguageLocally(LocalSettings.getBaseLanguageId());
        for (UserLanguage lang : langs) {
            items.add(Language.getLanguageInfo(lang.learningLang).name);
        }

        ListView listView = (ListView) findViewById(R.id.dialog_learn_lang_list1);
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                MyDialogHelper.showConfirmDialog(context, "Learning Language", "Set " + items.get(position) + "?",
                        new CallbackOkButton() {

                            public void okPressed() {
                                for (int i = 0; i < langs.size(); i++) {
                                    UserLanguage lang = langs.get(i);
                                    lang.use = false;
                                    if (i == position) {
                                        lang.use = true;
                                        LocalSettings.setCurrentLearningLanguage(lang.learningLang);
                                    }
                                    lang.save();
                                }
                                DialogLearnLang.this.dismiss();
                                if (callback != null) {
                                    callback.done();
                                }
                            }
                        }, null);
            }
        });
        listView.setItemsCanFocus(false);

    }

    public interface LanguagePicked {

        public void done();
    }
}
