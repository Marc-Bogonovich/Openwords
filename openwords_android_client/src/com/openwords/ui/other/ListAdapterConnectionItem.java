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
        final Word[] localWords = new Word[2];
        if (currentConnection != null) {
            final CheckBox check1 = (CheckBox) view.findViewById(R.id.list_item_connection_check1);
            TextView text1 = (TextView) view.findViewById(R.id.list_item_connection_text1);
            TextView text2 = (TextView) view.findViewById(R.id.list_item_connection_text2);
            final TextView text3 = (TextView) view.findViewById(R.id.list_item_connection_text3);
            for (Word word : words) {
                if (word.wordId == currentConnection.wordOneId) {
                    localWords[0] = word;
                }
                if (word.wordId == currentConnection.wordTwoId) {
                    localWords[1] = word;
                }
            }
            if (localWords[0] == null) {
                text1.setText("no word1");
            } else {
                text1.setText(localWords[0].word);
            }
            if (localWords[1] == null) {
                text2.setText("no word2");
            } else {
                text2.setText(localWords[1].word);
                if (WordConnection.hasConnection(currentConnection.connectionId)) {
                    text2.setTextColor(context.getResources().getColor(R.color.my_orange));
                }
            }
            
            OnClickListener listenerCheck = new OnClickListener() {
                
                public void onClick(View view) {
                    if (chosen.contains(currentConnection.connectionId)) {
                        chosen.remove(currentConnection.connectionId);
                        check1.setChecked(false);
                    } else {
                        chosen.add(currentConnection.connectionId);
                        check1.setChecked(true);
                    }
                }
            };
            OnClickListener listenerShowMeaning = new OnClickListener() {
                
                public void onClick(View view) {
                    text3.setText(localWords[0].getMeta().commonTranslation);
                }
            };
            check1.setOnClickListener(listenerCheck);
            text1.setOnClickListener(listenerShowMeaning);
            text2.setOnClickListener(listenerShowMeaning);
            text3.setOnClickListener(listenerShowMeaning);
        }
        return view;
    }
}
