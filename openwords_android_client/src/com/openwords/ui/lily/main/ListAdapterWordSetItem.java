package com.openwords.ui.lily.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.SetItem;
import com.openwords.util.ui.MyDialogHelper;
import java.util.List;

public class ListAdapterWordSetItem extends ArrayAdapter<SetItem> {

    private final List<SetItem> itemContent;
    private final PageSetContent context;

    public ListAdapterWordSetItem(PageSetContent context, List<SetItem> objects) {
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item_wordset, null);
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
        viewHolder.root = (LinearLayout) view.findViewById(R.id.list_item_ws_root);
        viewHolder.w1 = (TextView) view.findViewById(R.id.list_item_ws_text1);
        viewHolder.w2 = (TextView) view.findViewById(R.id.list_item_ws_text2);
        viewHolder.a1 = (ImageView) view.findViewById(R.id.list_item_ws_image1);
        viewHolder.a2 = (ImageView) view.findViewById(R.id.list_item_ws_image2);
        viewHolder.remove = (ImageView) view.findViewById(R.id.list_item_ws_image3);
        viewHolder.searchNative = (EditText) view.findViewById(R.id.list_item_ws_edit1);
        viewHolder.search = (ImageView) view.findViewById(R.id.list_item_ws_image4);
        view.setTag(viewHolder);
    }

    private void fillLayoutContent(final ViewHolder viewHolder, final SetItem item) {
        viewHolder.w1.setText(item.wordOne);
        viewHolder.w2.setText(item.wordTwo);
        viewHolder.w1.setPaintFlags(viewHolder.w1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        viewHolder.w2.setPaintFlags(viewHolder.w2.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        viewHolder.w1.setVisibility(View.VISIBLE);
        viewHolder.w2.setVisibility(View.VISIBLE);
        viewHolder.root.setBackgroundColor(Color.parseColor("#ffffff"));
        viewHolder.remove.setVisibility(View.GONE);
        viewHolder.a1.setVisibility(View.GONE);
        viewHolder.a2.setVisibility(View.GONE);
        viewHolder.searchNative.setVisibility(View.GONE);
        viewHolder.searchNative.setText(null);
        viewHolder.search.setVisibility(View.GONE);
        viewHolder.w1.setTextColor(Color.parseColor("#000000"));
        viewHolder.w2.setTextColor(Color.parseColor("#000000"));

        View.OnClickListener clickItemOne = new View.OnClickListener() {

            public void onClick(View view) {
                String trans = item.wordOneCommon;
                MyDialogHelper.showMessageDialog(context, null, trans, null);
            }
        };
        View.OnClickListener clickItemTwo = new View.OnClickListener() {

            public void onClick(View view) {
                String trans = item.wordTwoCommon;
                MyDialogHelper.showMessageDialog(context, null, trans, null);
            }
        };

        if (item.isModifying) {
            if (item.isHead) {
                viewHolder.a1.setVisibility(View.VISIBLE);
                viewHolder.a2.setVisibility(View.VISIBLE);
                viewHolder.w1.setTextColor(Color.parseColor("#d2d2d2"));
                viewHolder.w2.setTextColor(Color.parseColor("#d2d2d2"));
                viewHolder.remove.setVisibility(View.INVISIBLE);
                View.OnClickListener clickNative = new View.OnClickListener() {

                    public void onClick(View view) {
                        enterSearch(viewHolder, "Search Word", true);
                    }
                };
                viewHolder.a1.setOnClickListener(clickNative);
                viewHolder.w1.setOnClickListener(clickNative);
                View.OnClickListener clickLearning = new View.OnClickListener() {

                    public void onClick(View view) {
                        enterSearch(viewHolder, "搜索词", false);
                    }
                };
                viewHolder.a2.setOnClickListener(clickLearning);
                viewHolder.w2.setOnClickListener(clickLearning);
            } else if (item.isNew) {
                //viewHolder.a1.setOnClickListener(clickItemOne);
                //viewHolder.a2.setOnClickListener(clickItemTwo);
                viewHolder.w1.setOnClickListener(clickItemOne);
                viewHolder.w2.setOnClickListener(clickItemTwo);
                viewHolder.remove.setImageResource(R.drawable.ic_set_add);
                viewHolder.remove.setVisibility(View.VISIBLE);
                viewHolder.remove.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        context.addSetItemFromSearch(item);
                    }
                });
            } else if (item.isRemoving) {
                //viewHolder.a1.setOnClickListener(clickItemOne);
                //viewHolder.a2.setOnClickListener(clickItemTwo);
                viewHolder.w1.setOnClickListener(clickItemOne);
                viewHolder.w2.setOnClickListener(clickItemTwo);
                viewHolder.root.setBackgroundColor(Color.parseColor("#d2d2d2"));
                viewHolder.remove.setImageResource(R.drawable.ic_set_restore);
                viewHolder.remove.setVisibility(View.VISIBLE);
                viewHolder.remove.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        removeClicked(item, viewHolder);
                    }
                });
            } else {
                //viewHolder.a1.setOnClickListener(clickItemOne);
                //viewHolder.a2.setOnClickListener(clickItemTwo);
                viewHolder.w1.setOnClickListener(clickItemOne);
                viewHolder.w2.setOnClickListener(clickItemTwo);
                viewHolder.root.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.remove.setImageResource(R.drawable.ic_set_remove);
                viewHolder.remove.setVisibility(View.VISIBLE);
                viewHolder.remove.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        removeClicked(item, viewHolder);
                    }
                });
            }
        } else {
            //viewHolder.a1.setOnClickListener(clickItemOne);
            //viewHolder.a2.setOnClickListener(clickItemTwo);
            viewHolder.w1.setOnClickListener(clickItemOne);
            viewHolder.w2.setOnClickListener(clickItemTwo);
        }
    }

    private void removeClicked(SetItem item, ViewHolder v) {
        if (item.isRemoving) {
            v.root.setBackgroundColor(Color.parseColor("#ffffff"));
            v.remove.setImageResource(R.drawable.ic_set_remove);
            v.w1.setPaintFlags(v.w1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            v.w2.setPaintFlags(v.w2.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        } else {
            v.root.setBackgroundColor(Color.parseColor("#d2d2d2"));
            v.remove.setImageResource(R.drawable.ic_set_restore);
            v.w1.setPaintFlags(v.w1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            v.w2.setPaintFlags(v.w2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            context.reportContentHasJustChanged();
        }
        item.isRemoving = !item.isRemoving;
    }

    private void enterSearch(final ViewHolder v, String hint, final boolean searchNative) {
        v.a1.setVisibility(View.GONE);
        v.w1.setVisibility(View.GONE);
        v.a2.setVisibility(View.GONE);
        v.w2.setVisibility(View.GONE);
        v.searchNative.setVisibility(View.VISIBLE);
        v.searchNative.setText(null);
        v.searchNative.setHint(hint);
        v.remove.setImageResource(R.drawable.ic_set_remove);
        v.remove.setVisibility(View.VISIBLE);
        v.remove.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                bringBackAddButtons(v);
            }
        });
        v.search.setVisibility(View.VISIBLE);
        v.search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                context.search(searchNative, v.searchNative.getText().toString().trim());
            }
        });
        v.searchNative.requestFocus();
        InputMethodManager m = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m != null) {
            m.showSoftInput(v.searchNative, InputMethodManager.SHOW_FORCED);
        }
    }

    private void bringBackAddButtons(ViewHolder v) {
        v.a1.setVisibility(View.VISIBLE);
        v.w1.setVisibility(View.VISIBLE);
        v.a2.setVisibility(View.VISIBLE);
        v.w2.setVisibility(View.VISIBLE);
        v.searchNative.setVisibility(View.GONE);
        v.remove.setVisibility(View.INVISIBLE);
        v.search.setVisibility(View.GONE);
    }

    private class ViewHolder {

        public TextView w1, w2;
        public ImageView a1, a2, remove, search;
        public LinearLayout root;
        public EditText searchNative;
    }
}
