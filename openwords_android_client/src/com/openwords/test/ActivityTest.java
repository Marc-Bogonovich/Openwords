package com.openwords.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.openwords.R;

/**
 *
 * @author hanaldo
 */
public class ActivityTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.act_test_test1).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            }
        });
    }
}
