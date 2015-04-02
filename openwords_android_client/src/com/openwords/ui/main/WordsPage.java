package com.openwords.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.openwords.R;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.other.ActionBarBuilder;
import com.openwords.ui.other.NextWords;
import com.openwords.util.TimeConvertor;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.ui.MyQuickToast;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WordsPage extends Activity implements OnClickListener {

    private static String url_write_downloaded_words_to_server = "http://www.openwords.org/ServerPages/OpenwordsDB/setUserWords.php";
    private static String search_words_url = "http://www.openwords.org/ServerPages/WordsDB/wordsPageSearchWord.php";
    private static String[] searchWordsArray;
    public static AlertDialog.Builder dg;
    public static ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();
    private static JSONArray jArrMain;
    private ActionBarBuilder actionBar;
    private Button searchWords, searchWordSets, viewMyWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_page);

        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Words_Page);

        searchWords = (Button) findViewById(R.id.wordsPage_Button_searchWords);
        searchWords.setOnClickListener(this);
        searchWordSets = (Button) findViewById(R.id.wordsPage_Button_searchWordSets);
        searchWordSets.setOnClickListener(this);
        viewMyWords = (Button) findViewById(R.id.wordsPage_Button_viewMyWords);
        viewMyWords.setOnClickListener(this);

        searchWordsArray = null;
    }

    private void fillUI() {
        searchWords.setText(LocalizationManager.getTextWordsSearch());
        searchWordSets.setText(LocalizationManager.getTextWordsSets());
        viewMyWords.setText(LocalizationManager.getTextWordsView());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wordsPage_Button_getNextWords:
                WordsPage.this.startActivity(new Intent(WordsPage.this, NextWords.class));
                break;
            case R.id.wordsPage_Button_searchWords:
                searchWordsButtonClick(1, null);
                break;
            case R.id.wordsPage_Button_searchWordSets:
                MyQuickToast.showShort(this, "Not supported yet");
                break;
            case R.id.wordsPage_Button_viewMyWords:
                viewWordsButtonClick();
                break;
            case R.id.wordsPage_ImageView_syncButton:
                break;
        }
    }

    private void searchWordsButtonClick(int entry, String searchTxt) {
        mSelectedItems = new ArrayList<Integer>();
        LayoutInflater inflater = LayoutInflater.from(this);
        final View infView = inflater.inflate(R.layout.fragment_search_word, null);
        dg = new AlertDialog.Builder(this);
        dg.setTitle("Search words");
        dg.setView(infView);
        dg.setMultiChoiceItems(searchWordsArray, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                            boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it 
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                    }
                });

        final EditText ed = (EditText) infView.findViewById(R.id.editText_search);
        ed.setText(searchTxt);

        dg.setPositiveButton("Add Words", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog
                updateUserWords();
                dialog.dismiss();
            }
        });
        dg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                searchWordsArray = null;
                dialog.dismiss();
            }
        });
        dg.setNeutralButton("Search", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // who wrote this? Need modify
                searchWordsArray = new String[]{"�� cloud", "��˾ company", "��˾ blah"};
                //testmethod();
                Log.d("text", ed.getText().toString());
                dialog.dismiss();
                searchWordFromServer(ed.getText().toString());
                searchWordsButtonClick(0, ed.getText().toString());
            }
        });
        dg.create().show();

    }

    private void viewWordsButtonClick() {
//        final List<UserWords> userwords = UserWords.findByLanguage(OpenwordsSharedPreferences.getUserInfo().getLang_id());
//        CharSequence[] wordList = new String[userwords.size()];
//        mSelectedItems = new ArrayList<Integer>();
//        for (int i = 0; i < userwords.size(); i++) {
//            wordList[i] = userwords.get(i).wordLTwo + "  <-->  " + userwords.get(i).wordLOne;
//            if (userwords.get(i).fresh == 1) { //if this word is fresh
//                mSelectedItems.add(i);
//            }
//        }
        dg = new AlertDialog.Builder(this);
        dg.setTitle("My words");
//        dg.setMultiChoiceItems(wordList, null,
//                new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which,
//                            boolean isChecked) {
//                        if (isChecked) {
//                            // If the user checked the item, add it to the selected items
//                            mSelectedItems.add(which);
//                        } else if (mSelectedItems.contains(which)) {
//                            // Else, if the item is already in the array, remove it 
//                            mSelectedItems.remove(Integer.valueOf(which));
//                        }
//                    }
//                })
//                // Set the action buttons
//                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                    }
//                });

        dg.create().show();;

    }

    //
    public void searchWordFromServer(String searchText) {
        ArrayList<String> words_list = new ArrayList<String>();
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
//            params.add(new BasicNameValuePair("user", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getUserId())));
//            params.add(new BasicNameValuePair("langOne", "1"));
//            params.add(new BasicNameValuePair("langTwo", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getLang_id())));
//            params.add(new BasicNameValuePair("searchText", searchText));
            //Log.d("User", "47");

            JSONObject jObj = null;//jsonParse.makeHttpRequest(search_words_url, "POST", params);
            Log.d("Obj", jObj.toString());
            if (jObj.getInt("success") == 1) {
                JSONArray jArr = jObj.getJSONArray("data");
                String abc = Integer.toString(jArr.length());
                Log.d("Array", abc);
                jArrMain = jArr;
                for (int i = 0; i < jArr.length(); i++) {  // **line 2**
                    JSONObject childJSONObject = jArr.getJSONObject(i);

                    words_list.add(childJSONObject.getString("wordl1_text") + "  <-->  " + childJSONObject.getString("wordl2_text"));
                    //Log.d("Loop", childJSONObject.getString("wordl1_text"));
                    Log.d("Loop", words_list.get(i));
                }
            } else {
                words_list.add(new String("No Words found"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        searchWordsArray = new String[words_list.size()];
        words_list.toArray(searchWordsArray);
    }

    //update user words
    public void updateUserWords() {
        String conIds = "";
        for (int i = 0; i < mSelectedItems.size(); i++) {
            try {
                JSONObject c = jArrMain.getJSONObject(mSelectedItems.get(i));
//                UserWords uwRec = new UserWords(c.getInt("connection_id"),
//                        c.getInt("wordl1"), c.getString("wordl1_text"), c.getInt("wordl2"),
//                        c.getString("wordl2_text"), c.getInt("l2id"), c.getString("l2name"), c.getString("audio"));
//                uwRec.save();
//
//                //writing word transcriptions.
//                WordTranscription.insertMerge(c.getInt("wordl2"), c.getString("trans"));

                //concatenating connection ids
                if (conIds.length() == 0) {
                    conIds = conIds + c.getInt("connection_id");
                } else {
                    conIds = conIds + "|" + c.getInt("connection_id");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        this.updateWordsOnServer(conIds, TimeConvertor.getUnixTime());

        searchWordsArray = null;
    }

    public void updateWordsOnServer(String conIds, long dTime) {
        try {
//            int user = OpenwordsSharedPreferences.getUserInfo().getUserId();
//            int langTwo = OpenwordsSharedPreferences.getUserInfo().getLang_id();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("conid", conIds));
            params.add(new BasicNameValuePair("dtime", Long.toString(dTime)));
//            params.add(new BasicNameValuePair("user", Integer.toString(user)));
//            params.add(new BasicNameValuePair("lTwo", Integer.toString(langTwo)));

            JSONObject jObj = null;//jsonParse.makeHttpRequest(url_write_downloaded_words_to_server, "POST", params);

            if (jObj.getInt("success") == 1) {
                Log.d("Message From Server", jObj.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.checkSetting();
        fillUI();
    }

//        @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setTitle("Really?")
//                .setMessage("Are you sure you want to log out?")
//                .setNegativeButton("No", null)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        WordsPage.super.onBackPressed();
//                    }
//                }).create().show();
//    }
    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtMainPages(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                WordsPage.super.onBackPressed();
            }
        });
    }
}
