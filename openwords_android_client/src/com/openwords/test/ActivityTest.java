package com.openwords.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.loopj.android.http.RequestParams;
import com.openwords.R;
import com.openwords.ui.common.DialogForHTTP;
import com.openwords.util.log.LogUtil;
import org.apache.http.Header;

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

                DialogForHTTP d = new DialogForHTTP(ActivityTest.this,
                        "Downloading Language Pack",
                        "Retriving something, can be shown in percentage",
                        "http://156.56.92.178:8888/getWordsList.php",
                        5000,
                        new RequestParams("languageId", "1"),
                        new DialogForHTTP.OnSuccess() {

                            public void onSuccess(int i, Header[] headers, String string) {
                                Toast.makeText(ActivityTest.this, "onSuccess: " + string, Toast.LENGTH_SHORT).show();
                                LogUtil.logDeubg(this, "onSuccess: " + string);
                            }
                        },
                        new DialogForHTTP.OnFailure() {

                            public void onFailure(int i, Header[] headers, String string, Throwable thrwbl) {
                                Toast.makeText(ActivityTest.this, "onFailure: " + thrwbl.toString(), Toast.LENGTH_SHORT).show();
                                LogUtil.logDeubg(this, "onFailure: " + thrwbl.toString());
                            }
                        });
                d.start();
            }
        });
    }
}
