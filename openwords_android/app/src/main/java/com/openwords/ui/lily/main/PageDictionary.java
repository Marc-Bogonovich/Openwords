package com.openwords.ui.lily.main;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.LocalSettings;
import com.openwords.model.Word;
import com.openwords.services.implementations.ServiceLookupWord;
import com.openwords.services.implementations.ServiceLookupWordSameMeaning;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PageDictionary extends Activity {

    private LinearLayout root, result;
    private Button buttonSearchNative, buttonSearchLearn;
    private EditText inputNative, inputLearn;
    private ImageView arrow, back;
    private int localLang, learningLang, padding, margin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.lily_page_dictionary);
        root = (LinearLayout) findViewById(R.id.page_dict_root);
        root.setBackgroundColor(getResources().getColor(R.color.main_app_color));
        localLang = LocalSettings.getBaseLanguageId();
        learningLang = LocalSettings.getCurrentLearningLanguage();
        Resources r = getResources();
        padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());

        inputNative = (EditText) findViewById(R.id.page_dict_edit1);
        inputLearn = (EditText) findViewById(R.id.page_dict_edit2);
        buttonSearchNative = (Button) findViewById(R.id.page_dict_button1);
        buttonSearchLearn = (Button) findViewById(R.id.page_dict_button2);
        back = (ImageView) findViewById(R.id.page_dict_image1);
        arrow = (ImageView) findViewById(R.id.page_dict_image2);
        result = (LinearLayout) findViewById(R.id.page_dict_layout1);
        inputNative.setHint("Search in native language");
        inputLearn.setHint("Search in learning language");
        buttonSearchNative.setText("Search");
        buttonSearchLearn.setText("Search");

        buttonSearchNative.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                arrow.setRotation(180);
                inputLearn.setText(null);
                result.removeAllViews();
                search(localLang, learningLang, inputNative, inputLearn);
            }
        });

        buttonSearchLearn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                arrow.setRotation(0);
                inputNative.setText(null);
                result.removeAllViews();
                search(learningLang, localLang, inputLearn, inputNative);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PageDictionary.super.onBackPressed();
            }
        });

    }

    private void search(int langIn, final int langOut, EditText input, final EditText output) {
        String form = input.getText().toString();
        if (form.isEmpty()) {
            MyQuickToast.showShort(this, "Please input the word!");
            return;
        }

        MyDialogHelper.tryShowQuickProgressDialog(this, "Searching...");
        new ServiceLookupWord().doRequest(langIn, form, 10, false, new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                MyDialogHelper.tryDismissQuickProgressDialog();
                ServiceLookupWord.Result r = (ServiceLookupWord.Result) resultObject;

                List<Long> ids = new LinkedList<Long>();
                final Map<String, String> allCommons = new HashMap<String, String>(r.result.size());

                for (Word w : r.result) {
                    ids.add(w.wordId);
                    allCommons.put(w.getMeta().commonTranslation, w.word + " -> ");
                }

                new ServiceLookupWordSameMeaning().doRequest(ids, langOut, new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        ServiceLookupWordSameMeaning.Result r = (ServiceLookupWordSameMeaning.Result) resultObject;

                        String together = "";
                        for (Word w : r.result) {
                            String commonKey = w.getMeta().commonTranslation;
                            if (allCommons.containsKey(commonKey)) {
                                String old = allCommons.get(commonKey);
                                allCommons.put(commonKey, old + w.word + ", ");
                                together += w.word + ", ";
                            } else {
                                MyQuickToast.showShort(PageDictionary.this, "Got one wrong result: " + w.word + ": " + commonKey);
                            }
                        }
                        output.setHint(together);

                        for (Entry<String, String> entry : allCommons.entrySet()) {
                            TextView t = new TextView(PageDictionary.this);
                            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            t.setPadding(padding, 0, padding, 0);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            t.setLayoutParams(params);
                            params.setMargins(margin, margin, margin, margin);
                            t.setText(entry.getValue() + "\n(" + entry.getKey() + ")");
                            result.addView(t);
                        }
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(PageDictionary.this, "No result.");
                    }
                });
            }

            public void noResult(String errorMessage) {
                MyDialogHelper.tryDismissQuickProgressDialog();
                MyQuickToast.showShort(PageDictionary.this, "No such word.");
            }
        });
    }

}
