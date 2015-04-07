package com.openwords.ui.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import java.util.List;
import java.util.Set;

public class ListAdapterConnectionItem extends ArrayAdapter<WordConnection> {

    private List<WordConnection> connections;
    private List<Word> words;
    private Context context;
    private Set<Integer> chosen;

    public ListAdapterConnectionItem(Context context, List<WordConnection> connections, List<Word> words, Set<Integer> chosen) {
        super(context, R.layout.list_item_connection, connections);
        this.context = context;
        this.connections = connections;
        this.words = words;
        this.chosen = chosen;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.list_item_connection, null);
        }

        final WordConnection currentConnection = connections.get(position);
        Word word1 = null, word2 = null;
        if (currentConnection != null) {
            final CheckBox check1 = (CheckBox) view.findViewById(R.id.list_item_connection_check1);
            TextView text1 = (TextView) view.findViewById(R.id.list_item_connection_text1);
            TextView text2 = (TextView) view.findViewById(R.id.list_item_connection_text2);
            for (Word word : words) {
                if (word.wordId == currentConnection.wordOneId) {
                    word1 = word;
                }
                if (word.wordId == currentConnection.wordTwoId) {
                    word2 = word;
                }
            }
            if (word1 == null) {
                text1.setText("no word1");
            } else {
                text1.setText(word1.word);
            }
            if (word2 == null) {
                text2.setText("no word2");
            } else {
                text2.setText(word2.word);
            }

            //check1.setChecked(WordConnection.hasConnection(currentConnection.connectionId));
            check1.setOnClickListener(new OnClickListener() {

                public void onClick(View view) {
                    if (chosen.contains(currentConnection.connectionId)) {
                        chosen.remove(currentConnection.connectionId);
                        check1.setChecked(false);
                    } else {
                        chosen.add(currentConnection.connectionId);
                        check1.setChecked(true);
                    }
                }
            });
        }
        return view;
    }
}
