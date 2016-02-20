package com.openwords.ui.lily.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.openwords.R;

import java.util.List;

public class ListAdapterLessonItem extends ArrayAdapter<String[]> {

    private final List<String[]> items;
    private final Context context;

    public ListAdapterLessonItem(Context context, List<String[]> objects) {
        super(context, R.layout.list_item_lesson, objects);
        this.context = context;
        items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;
        final String[] item = items.get(position);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_lesson, null);
            viewHolder = new ViewHolder();
            inflateLayout(view, viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (item != null) {
            fillLayoutContent(viewHolder, item);
        }
        return view;
    }

    private void inflateLayout(final View view, final ViewHolder viewHolder) {
        viewHolder.title = (TextView) view.findViewById(R.id.list_item_lesson_text1);
        viewHolder.content = (TextView) view.findViewById(R.id.list_item_lesson_text2);
        view.setTag(viewHolder);
    }

    private void fillLayoutContent(ViewHolder viewHolder, String[] item) {
        viewHolder.title.setText(item[0]);
        viewHolder.content.setText(item[1]);
    }

    private class ViewHolder {

        public TextView title, content;
    }
}
