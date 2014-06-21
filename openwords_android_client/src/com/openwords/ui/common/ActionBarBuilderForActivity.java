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
     * @param imageResourceId
     * @param action Can be null if this item is not clickable
     * @return
     */
    public ActionBarBuilderForActivity showFirstItem(int imageResourceId, ActionBarItemClickAction action) {
        return showItem(R.id.actionbar_item_1, R.id.actionbar_item_1_image, imageResourceId, action);
    }

    /**
     *
     * @param imageResourceId
     * @param action Can be null if this item is not clickable
     * @return
     */
    public ActionBarBuilderForActivity showSecondItem(int imageResourceId, ActionBarItemClickAction action) {
        return showItem(R.id.actionbar_item_2, R.id.actionbar_item_2_image, imageResourceId, action);
    }

    /**
     *
     * @param imageResourceId
     * @param action Can be null if this item is not clickable
     * @return
     */
    public ActionBarBuilderForActivity showThirdItem(int imageResourceId, ActionBarItemClickAction action) {
        return showItem(R.id.actionbar_item_3, R.id.actionbar_item_3_image, imageResourceId, action);
    }

    /**
     *
     * @param imageResourceId
     * @param action Can be null if this item is not clickable
     * @return
     */
    public ActionBarBuilderForActivity showFourthItem(int imageResourceId, ActionBarItemClickAction action) {
        return showItem(R.id.actionbar_item_4, R.id.actionbar_item_4_image, imageResourceId, action);
    }

    /**
     *
     * @param imageResourceId
     * @param action Can be null if this item is not clickable
     * @return
     */
    public ActionBarBuilderForActivity showFifthItem(int imageResourceId, ActionBarItemClickAction action) {
        return showItem(R.id.actionbar_item_5, R.id.actionbar_item_5_image, imageResourceId, action);
    }

    public ActionBarBuilderForActivity highlightFirstItem() {
        highlightItem(R.id.actionbar_item_1);
        return this;
    }

    public ActionBarBuilderForActivity highlightSecondItem() {
        highlightItem(R.id.actionbar_item_2);
        return this;
    }

    public ActionBarBuilderForActivity highlightThirdItem() {
        highlightItem(R.id.actionbar_item_3);
        return this;
    }

    public ActionBarBuilderForActivity highlightFourthItem() {
        highlightItem(R.id.actionbar_item_4);
        return this;
    }

    public ActionBarBuilderForActivity highlightFifthItem() {
        highlightItem(R.id.actionbar_item_5);
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

    public interface ActionBarItemClickAction {

        public void clicked();
    }
}
