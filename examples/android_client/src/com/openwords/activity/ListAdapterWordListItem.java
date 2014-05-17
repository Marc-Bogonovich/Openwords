package com.openwords.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.database.Word;
import java.util.List;

/**
 *
 * @author hanaldo
 */
public class ListAdapterWordListItem extends ArrayAdapter<Object[]> {
    
    private final List<Object[]> itemContent;
    private final Context context;
    
    public ListAdapterWordListItem(Context context, List<Object[]> objects) {
        super(context, 1, objects);
        this.context = context;
        itemContent = objects;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Object[] item = itemContent.get(position);
        View view = convertView;
        
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.list_item_word, null);
        }
        
        if (item != null) {
            Word word = (Word) item[0];
            TextView text1 = (TextView) view.findViewById(R.id.list_item_word_text1);
            text1.setText(word.getSpelling());
        }
        return view;
    }
}
