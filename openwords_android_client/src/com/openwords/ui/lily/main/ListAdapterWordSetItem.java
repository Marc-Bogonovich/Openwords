package com.openwords.ui.lily.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.util.ui.MyQuickToast;
import java.util.List;

public class ListAdapterWordSetItem extends ArrayAdapter<SetItem> {

    private final List<SetItem> itemContent;
    private final Context context;

    public ListAdapterWordSetItem(Context context, List<SetItem> objects) {
        super(context, R.layout.list_item_wordset, objects);
        this.context = context;
        itemContent = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;
        final SetItem item = itemContent.get(position);

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.list_item_wordset, null);
            viewHolder = new ViewHolder();
            viewHolder.root = (LinearLayout) view.findViewById(R.id.list_item_ws_root);
            viewHolder.w1 = (TextView) view.findViewById(R.id.list_item_ws_text1);
            viewHolder.w2 = (TextView) view.findViewById(R.id.list_item_ws_text2);
            viewHolder.a1 = (ImageView) view.findViewById(R.id.list_item_ws_image1);
            viewHolder.a2 = (ImageView) view.findViewById(R.id.list_item_ws_image2);
            viewHolder.remove = (ImageView) view.findViewById(R.id.list_item_ws_image3);

            viewHolder.w1.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    MyQuickToast.showShort(context, "hi");
                }
            });
            viewHolder.remove.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    removeClicked(item, viewHolder);
                }
            });

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (item != null) {
            viewHolder.w1.setText(item.wordOne);
            viewHolder.w2.setText(item.wordTwo);
            viewHolder.root.setBackgroundColor(Color.parseColor("#ffffff"));

            if (item.isModifying) {
                viewHolder.remove.setVisibility(View.VISIBLE);
            } else {
                viewHolder.remove.setVisibility(View.GONE);
            }
            viewHolder.remove.setSelected(false);
            if (item.isHead) {
                viewHolder.a1.setVisibility(View.VISIBLE);
                viewHolder.a2.setVisibility(View.VISIBLE);
                viewHolder.w1.setTextColor(Color.parseColor("#d2d2d2"));
                viewHolder.w2.setTextColor(Color.parseColor("#d2d2d2"));
            }
        }
        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private void removeClicked(SetItem item, ViewHolder v) {
        if (item.isRemoving) {
            v.root.setBackgroundColor(Color.parseColor("#ffffff"));
            v.remove.setSelected(false);
        } else {
            v.root.setBackgroundColor(Color.parseColor("#d2d2d2"));
            v.remove.setSelected(true);
        }
        item.isRemoving = !item.isRemoving;

    }

    private class ViewHolder {

        public TextView w1, w2;
        public ImageView a1, a2, remove;
        public LinearLayout root;
    }
}
