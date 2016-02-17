package com.openwords.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.Performance;
import com.openwords.model.ResultWordConnections;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.util.ui.MyQuickToast;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DialogLocalWords extends Dialog {

    private ListView listView;

    public DialogLocalWords(final Context c) {
        super(c);
        setContentView(R.layout.dialog_local_words);
        Language lang = Language.getLanguageInfo(DataPool.LmLearningLang);
        if (lang == null) {
            MyQuickToast.showShort(c, "You have no languages");
            cancel();
            return;
        }
        //temp solution
        String langName = lang.name.split("]")[0];
        langName = langName.split("#")[0];
        langName = langName.replace("[", "");
        setTitle("Your local " + langName + " words");

        listView = (ListView) findViewById(R.id.dialog_local_words_list1);
        listView.setItemsCanFocus(false);

        WordConnection.loadWordConnectionsFullPack(
                false, LocalSettings.getUserId(),
                LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang,
                1, 10000, false, null,
                new ResultWordConnections() {

                    public void result(List<WordConnection> connections, List<Word> words, List<Performance> performance) {
                        if (connections == null) {
                            MyQuickToast.showShort(c, "You have no local words");
                            DialogLocalWords.this.cancel();
                        } else {
                            Map<Long, String> wordForms = new HashMap<Long, String>(words.size());
                            for (Word word : words) {
                                wordForms.put(word.wordId, word.word);
                            }

                            List<String> items = new LinkedList<String>();
                            for (WordConnection wc : connections) {
                                String word1 = wordForms.get(wc.wordOneId);
                                String word2 = wordForms.get(wc.wordTwoId);
                                items.add(word1 + "          " + word2);
                            }
                            listView.setAdapter(new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, items));
                        }
                    }
                });
    }
}
