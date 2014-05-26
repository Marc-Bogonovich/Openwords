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

    public ActionBarBuilderForActivity showTitle(int textViewId, int stringResourceId) {
        TextView tv = (TextView) activity.findViewById(textViewId);
        tv.setText(stringResourceId);
        return this;
    }

    public ActionBarBuilderForActivity showFirstItem(int imageResourceId, ActionBarItemClickAction action) {
        return showItem(R.id.actionbar_item_1, R.id.actionbar_item_1_image, imageResourceId, action);
    }

    public ActionBarBuilderForActivity showSecondItem(int imageResourceId, ActionBarItemClickAction action) {
        return showItem(R.id.actionbar_item_2, R.id.actionbar_item_2_image, imageResourceId, action);
    }

    public ActionBarBuilderForActivity showThirdItem(int imageResourceId, ActionBarItemClickAction action) {
        return showItem(R.id.actionbar_item_3, R.id.actionbar_item_3_image, imageResourceId, action);
    }

    private ActionBarBuilderForActivity showItem(int layoutViewId, int imageViewId, int imageResourceId, final ActionBarItemClickAction action) {
        LinearLayout l = (LinearLayout) activity.findViewById(layoutViewId);
        ImageView iv = (ImageView) activity.findViewById(imageViewId);
        iv.setBackgroundResource(imageResourceId);
        l.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                action.clicked();
            }
        });
        l.setVisibility(View.VISIBLE);
        return this;
    }

    public interface ActionBarItemClickAction {

        public void clicked();
    }
}
