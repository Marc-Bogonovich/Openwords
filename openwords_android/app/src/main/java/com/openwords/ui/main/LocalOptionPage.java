package com.openwords.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.ResultLanguage;
import com.openwords.model.ResultUserLanguage;
import com.openwords.model.UserLanguage;
import com.openwords.util.localization.LocalLanguage;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author hanaldo
 */
public class LocalOptionPage extends Activity {

    public static boolean supported = false;

    public static void showOptionDialogs(final Activity act) {
        List<String> options = new LinkedList<String>();
        for (Object[] item : LocalizationManager.LanguageNamesTypesIdsLocales) {
            options.add((String) item[0]);
        }
        final Dialog[] d = new Dialog[2];
        d[0] = new DialogForLocalLangOptions(act, LocalizationManager.getTextLangOptionTitle(), options, new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> av, View view, final int i, long l) {
                String name = (String) LocalizationManager.LanguageNamesTypesIdsLocales[i][0];
                d[1] = new AlertDialog.Builder(act)
                        .setTitle(LocalizationManager.getTextAreYouSure())
                        .setMessage(name)
                        .setNegativeButton(LocalizationManager.getTextNo(), null)
                        .setPositiveButton(LocalizationManager.getTextYes(), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Object choice = LocalizationManager.LanguageNamesTypesIdsLocales[i][1];
                                LocalLanguage lang = (LocalLanguage) choice;
                                LocalizationManager.setLocalLanguage(lang);
                                LocalSettings.setLocalLanguage(lang);
                                d[0].cancel();
                                d[1].cancel();

                                MyQuickToast.showShort(act, "Base Language changed to " + LocalSettings.getBaseLanguageId());
                                Language.syncLanguagesData(act, new ResultLanguage() {

                                    public void result(String resultObject) {
                                        if (resultObject != null && resultObject.equals(Result_No_Language_Data)) {
                                            MyDialogHelper.tryDismissQuickProgressDialog();
                                            MyQuickToast.showShort(act, "No language data");
                                            act.finish();
                                            return;
                                        }
                                        if (resultObject != null && resultObject.equals(Result_No_Server_Response)) {
                                            MyDialogHelper.tryDismissQuickProgressDialog();
                                            MyQuickToast.showShort(act, "No server response");
                                            act.finish();
                                            return;
                                        }
                                        if (LocalSettings.getUserId() > 0) {
                                            UserLanguage.syncUserLanguage(
                                                    LocalSettings.getUserId(),
                                                    LocalSettings.getBaseLanguageId(),
                                                    new ResultUserLanguage() {

                                                        public void result(List<UserLanguage> result) {
                                                            act.finish();
                                                        }
                                                    });
                                        } else {
                                            act.finish();
                                        }
                                    }
                                });

                            }
                        }).create();
                d[1].show();
            }
        });
        d[0].show();
    }

    private TextView text1;
    private Button buttonYes, buttonNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_option);

        text1 = (TextView) findViewById(R.id.localOptionPage_text1);
        String title;
        if (supported) {
            title = LocalizationManager.getTextLangOptionGreet();
            title = title.replace("#", "<b><font color=#000>");
            title = title.replace("$", "</font></b>");
        } else {
            title = "Hi there, we do not support the native language on your phone yet, do you want to use <b><font color=#000>ENGLISH</font></b> as your native language?";
        }
        text1.setText(Html.fromHtml(title));

        buttonNo = (Button) findViewById(R.id.localOptionPage_button1);
        buttonYes = (Button) findViewById(R.id.localOptionPage_button2);
        buttonNo.setText(LocalizationManager.getTextNo());
        buttonYes.setText(LocalizationManager.getTextYes());
        buttonNo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                showOptionDialogs(LocalOptionPage.this);
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                LocalSettings.setLocalLanguage(LocalizationManager.getCurrentLang());
                LocalOptionPage.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
