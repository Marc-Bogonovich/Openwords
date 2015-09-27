package com.openwords.ui.lily.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import java.util.List;

public class ListAdapterDeckGrid extends ArrayAdapter<DeckInfo> {

    private final Context context;
    private final List<DeckInfo> items;

    public ListAdapterDeckGrid(Context context, List<DeckInfo> objects) {
        super(context, R.layout.list_item_deck_info, objects);
        this.context = context;
        items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        DeckInfo deckInfo = items.get(position);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_deck_info, null);
            viewHolder = new ViewHolder();
            inflateLayout(view, viewHolder, deckInfo);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (deckInfo != null) {
            fillLayoutContent(viewHolder, deckInfo);
        }
        return view;
    }

    private void inflateLayout(final View view, final ViewHolder viewHolder, final DeckInfo deckInfo) {
        viewHolder.deckName = (TextView) view.findViewById(R.id.list_item_deck_info_1);
        viewHolder.deckHolder = (LinearLayout) view.findViewById(R.id.list_item_deck_info_deck);
        if (deckInfo != null) {
            if (deckInfo.isPlusButton) {
                viewHolder.deckCircle = new ViewDeckCircle(context, context.getResources().getColor(R.color.blue), 255,
                        true, context.getResources().getColor(R.color.white), 255);
                viewHolder.deckName.setTextColor(context.getResources().getColor(R.color.main_app_color));
            } else {
                viewHolder.deckCircle = new ViewDeckCircle(context, context.getResources().getColor(R.color.main_app_color), 200,
                        false, -1, -1);
            }
            viewHolder.deckHolder.addView(viewHolder.deckCircle);
            viewHolder.deckHolder.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    viewHolder.deckCircle.updateDimension(v.getWidth(), v.getHeight());
                }
            });
        }
        view.setTag(viewHolder);
    }

    private void fillLayoutContent(final ViewHolder viewHolder, final DeckInfo deckInfo) {
        viewHolder.deckName.setText(deckInfo.name);
//            if (!deckInfo.isPlusButton) {
//                viewHolder.deckCircle.setText(deckInfo.name);
//            }

    }

    @Override
    public boolean isEnabled(int position) {
        DeckInfo deckInfo = items.get(position);
        return deckInfo != null;
    }

    private class ViewHolder {

        public TextView deckName;
        public LinearLayout deckHolder;
        public ViewDeckCircle deckCircle;
    }
}
