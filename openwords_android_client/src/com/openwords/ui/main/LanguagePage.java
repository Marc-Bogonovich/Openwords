package com.openwords.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.JSONParser;
import com.openwords.model.Language;
import com.openwords.model.UserInfo;
import com.openwords.services.ModelLanguage;
import com.openwords.services.implementations.GetLanguages;
import com.openwords.services.implementations.GetLearnableLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.LanguagePageTool;
import com.openwords.util.log.QuickToast;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class LanguagePage extends Activity {

    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    public static ArrayList<LanguagePageTool> langlist_global = new ArrayList<LanguagePageTool>();
    public static ListView langListView = null;
    public static String url_write_l2_choice = "http://www.openwords.org/ServerPages/OpenwordsDB/writeL2Choices.php";
    private UserInfo userinfo;

    private ListView listView;
    private ListAdapterLanguageItem listAdapter;
    private List<Language> localLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_page);
        listView = (ListView) findViewById(R.id.language_list_view);
        localLanguages = new LinkedList<Language>();
        listAdapter = new ListAdapterLanguageItem(this, R.layout.list_item_language, localLanguages);
        listView.setAdapter(listAdapter);
        //userinfo = OpenwordsSharedPreferences.getUserInfo();
        refreshList();

        final View langButton = findViewById(R.id.langbutton);
        //langButton.setOnClickListener(this);
        langButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                langButton.setEnabled(false);
                AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
                    ProgressDialog progress;

                    @Override
                    protected void onPreExecute() {
                        progress = new ProgressDialog(LanguagePage.this);
                        progress.setMessage("Synchronizing with server...");
                        progress.setCancelable(false);
                        progress.setIndeterminate(true);
                        progress.show();
                    }

                    @Override
                    protected Boolean doInBackground(Void... arg0) {
                        //We could modify this function to return a boolean value
                        return writeToServer(userinfo.getUserId());
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (progress != null) {
                            progress.dismiss();
                            if (result.equals(true)) {
                                if (DataPool.LanguageList != null) {
                                    int spinnerSize = DataPool.LanguageList.size();

                                    for (int i = 0; i < spinnerSize - 1; i++) {
                                        DataPool.LanguageList.clear();
                                    }

                                    for (int i = 0; i < langlist_global.size(); i++) {
                                        if (langlist_global.get(i).isSelected()) {
                                            //DataPool.LanguageList.add(DataPool.LanguageList.size()-1,new ModelLanguage(langlist_global.get(i).getId(),
                                            //langlist_global.get(i).getName()));
                                            DataPool.LanguageList.add(new ModelLanguage(langlist_global.get(i).getId(),
                                                    langlist_global.get(i).getName()));
                                        }
                                    }
                                    DataPool.LanguageList.add(new ModelLanguage(-999,
                                            "Add More Languages"));
                                }
                                startActivityForResult(new Intent(LanguagePage.this.getApplicationContext(), ChosenPage.class), 0);
                            } else {
                                Toast.makeText(LanguagePage.this, "You must select at least one", Toast.LENGTH_SHORT).show();
                            }

                            langButton.setEnabled(true);
                        }
                    }
                };
                task.execute((Void[]) null);
                return false;
            }

        });
    }

    private void refreshList() {
        listAdapter.clear();
        localLanguages = Language.listAll(Language.class);
        listAdapter.addAll(localLanguages);
        listAdapter.notifyDataSetChanged();
        checkAndMergeNewLanguages();
    }

    private void checkAndMergeNewLanguages() {
        new GetLearnableLanguages().doRequest(new RequestParamsBuilder()
                .addParam("langOneId", "1")
                .getParams(), new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        @SuppressWarnings("unchecked")
                        List<Integer> learnableIds = (List<Integer>) resultObject;
                        String sqlIds = learnableIds.toString().replace("[", "(").replace("]", ")");

                        String sql = "SELECT * FROM LANGUAGE WHERE LANG_ID IN " + sqlIds;
                        List<Language> localSameLangs = Language.findWithQuery(Language.class, sql);
                        QuickToast.showShort(LanguagePage.this, "total local same: " + localSameLangs.size());

                        Set<Integer> newLangIds = new HashSet<Integer>(learnableIds);
                        for (Language lang : localSameLangs) {
                            if (newLangIds.contains(lang.langId)) {
                                newLangIds.remove(lang.langId);
                            }
                        }
                        QuickToast.showShort(LanguagePage.this, "total new: " + newLangIds.size());

                        if (!newLangIds.isEmpty()) {
                            getAndSaveLanguageInformation(newLangIds);
                        } else {
                            QuickToast.showShort(LanguagePage.this, "no new learnable languages");
                        }
                    }

                    public void noResult(String errorMessage) {
                        QuickToast.showShort(LanguagePage.this, "not ok: " + errorMessage);
                    }
                });
    }

    private void getAndSaveLanguageInformation(Collection<Integer> langIds) {
        new GetLanguages().doRequest(new RequestParamsBuilder()
                .addParam("include", new Gson().toJson(langIds))
                .getParams(), new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        @SuppressWarnings("unchecked")
                        List<Language> langs = (List<Language>) resultObject;
                        Language.saveInTx(langs);
                        listAdapter.clear();
                        localLanguages = Language.listAll(Language.class);
                        listAdapter.addAll(localLanguages);
                        listAdapter.notifyDataSetChanged();
                        QuickToast.showShort(LanguagePage.this, "local total: " + Language.count(Language.class));
                    }

                    public void noResult(String errorMessage) {
                        QuickToast.showShort(LanguagePage.this, "not ok: " + errorMessage);
                    }
                });
    }

    //Writing options to server
    public boolean writeToServer(int user) {

        String l2_ids = "";
        int selected = 0;
        //-------building the selected languages parameter string
        for (int i = 0; i < langlist_global.size(); i++) {
            if (langlist_global.get(i).isSelected() == true) {
                l2_ids = l2_ids + langlist_global.get(i).getId() + "|";
                selected++;
            }
        }

        if (selected != 0) {
            l2_ids = l2_ids.substring(0, l2_ids.length() - 1);

            Log.d("L2 ids", l2_ids);
            Log.d("L2 ids", "_" + user);
            //------------------------------------

            String user_id = Integer.toString(user);
            //-------------constucting parameters------------
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("choices", l2_ids));
            params.add(new BasicNameValuePair("id", user_id));
            //-----------------------------------------------
            try {
                JSONParser jsonParse = new JSONParser();
                JSONObject jObj = jsonParse.makeHttpRequest(url_write_l2_choice, "POST", params);

                int success = jObj.getInt(TAG_SUCCESS);
                String msg = jObj.getString(TAG_MESSAGE);

                if (success == 1) {
                    Log.d("Info", "create successfully");
                    //usernameExist = true;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LanguagePage.this, "Preference created successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
