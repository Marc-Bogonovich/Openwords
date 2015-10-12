package com.openwords.ui.lily.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.SetInfo;
import java.util.List;

public class ListAdapterWordSets extends ArrayAdapter<SetInfo> {

    private final Context context;
    private final List<SetInfo> items;
    private final long localUserId;

    public ListAdapterWordSets(Context context, List<SetInfo> objects, long localUserId) {
        super(context, R.layout.list_item_deck_info, objects);
        this.context = context;
        items = objects;
        this.localUserId = localUserId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        SetInfo info = items.get(position);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_deck_info, null);
            viewHolder = new ViewHolder();
            inflateLayout(view, viewHolder, info);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (info != null) {
            fillLayoutContent(viewHolder, info);
        }
        return view;
    }

    private void inflateLayout(final View view, final ViewHolder viewHolder, final SetInfo info) {
        viewHolder.deckName = (TextView) view.findViewById(R.id.list_item_deck_info_1);
        viewHolder.deckHolder = (LinearLayout) view.findViewById(R.id.list_item_deck_info_deck);
        if (info != null) {
            if (info.isPlusButton) {
                viewHolder.deckCircle = new ViewDeckCircle(context, context.getResources().getColor(R.color.blue), 255,
                        true, context.getResources().getColor(R.color.white), 255);
                viewHolder.deckName.setTextColor(context.getResources().getColor(R.color.blue));
            } else {
                viewHolder.deckCircle = new ViewDeckCircle(context, DataPool.Color_Main, 200,
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

    private void fillLayoutContent(final ViewHolder viewHolder, final SetInfo info) {
        viewHolder.deckName.setText(info.name);
        if (info.userId == localUserId && info.setId != 0) {
            viewHolder.deckCircle.updateColor(Color.parseColor("#477368"));
        }
//            if (!deckInfo.isPlusButton) {
//                viewHolder.deckCircle.setText(deckInfo.name);
//            }

    }

    @Override
    public boolean isEnabled(int position) {
        SetInfo info = items.get(position);
        return info != null;
    }

    private class ViewHolder {

        public TextView deckName;
        public LinearLayout deckHolder;
        public ViewDeckCircle deckCircle;
    }
}
