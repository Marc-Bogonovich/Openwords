package com.openwords.ui.common;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;

/**
 *
 * @author hanaldo
 */
public class ActionBarBuilderForActivity {

    public static final Integer[][] ButtonResources = new Integer[][]{
        new Integer[]{R.id.actionbar_item_1, R.id.actionbar_item_1_image},
        new Integer[]{R.id.actionbar_item_2, R.id.actionbar_item_2_image},
        new Integer[]{R.id.actionbar_item_3, R.id.actionbar_item_3_image},
        new Integer[]{R.id.actionbar_item_4, R.id.actionbar_item_4_image},
        new Integer[]{R.id.actionbar_item_5, R.id.actionbar_item_5_image}
    };
    private final Activity activity;

    public ActionBarBuilderForActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     *
     * @param textViewId
     * @param stringResourceId
     * @param action Can be null if you don't want to click the title
     * @return
     */
    public ActionBarBuilderForActivity showTitle(int textViewId, int stringResourceId, final ActionBarItemClickAction action) {
        TextView tv = (TextView) activity.findViewById(textViewId);
        tv.setText(stringResourceId);
        tv.setVisibility(View.VISIBLE);
        if (action != null) {
            tv.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    action.clicked();
                }
            });
        }
        return this;
    }

    /**
     *
     * @param index the index of item in ButtonResources
     * @param imageResourceId
     * @param action Can be null if this item is not clickable
     * @return
     */
    public ActionBarBuilderForActivity showButtonItem(int index, int imageResourceId, ActionBarItemClickAction action) {
        return showItem(ButtonResources[index][0], ButtonResources[index][1], imageResourceId, action);
    }

    public ActionBarBuilderForActivity highlightButtonItem(int index) {
        highlightItem(ButtonResources[index][0]);
        return this;
    }

    public ActionBarBuilderForActivity showBackButton(final ActionBarItemClickAction action) {
        ImageView iv = (ImageView) activity.findViewById(R.id.actionbar_back_button);
        iv.setVisibility(View.VISIBLE);
        iv.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                action.clicked();
            }
        });

        return this;
    }

    private ActionBarBuilderForActivity showItem(int layoutViewId, int imageViewId, int imageResourceId, final ActionBarItemClickAction action) {
        LinearLayout l = (LinearLayout) activity.findViewById(layoutViewId);
        ImageView iv = (ImageView) activity.findViewById(imageViewId);
        iv.setBackgroundResource(imageResourceId);
        if (action != null) {
            l.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    action.clicked();
                }
            });
        }
        l.setVisibility(View.VISIBLE);
        return this;
    }

    private void highlightItem(int viewId) {
        LinearLayout v = (LinearLayout) activity.findViewById(viewId);
        v.setBackgroundColor(activity.getResources().getColor(R.color.my_gray));
    }

    public void hideItem(int index) {
        activity.findViewById(ButtonResources[index][0]).setVisibility(View.GONE);
    }

    public void reshowItem(int index) {
        activity.findViewById(ButtonResources[index][0]).setVisibility(View.VISIBLE);
    }

    public interface ActionBarItemClickAction {

        public void clicked();
    }
}
