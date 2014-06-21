package com.openwords.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.openwords.R;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.tts.Speak;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;

/**
 *
 * @author hanaldo
 */
public class WordListActivity extends ListActivity {

    private static final String className = WordListActivity.class.getName();
    private static Handler refreshListHanlder;
    private ArrayList<Object[]> items;
    private ListAdapterWordListItem listAdapter;
    private TextView text1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_word_list);

        items = new ArrayList<Object[]>(10);

        listAdapter = new ListAdapterWordListItem(this, items);
        setListAdapter(listAdapter);

        refreshListHanlder = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    refreshWordList();
                }
            }
        };

        text1 = (TextView) findViewById(R.id.act_wordlist_text1);

        int languageId = getIntent().getExtras().getInt("gid");
        String mode = getIntent().getExtras().getString("mode");
        if (mode.equals("remote")) {
            loadWordsFromServer(languageId);
        } else if (mode.equals("local")) {
            loadWordsFromLocal(languageId);
        }
    }

    private void loadWordsFromServer(final int languageId) {
        final AsyncHttpClient http = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("languageId", String.valueOf(languageId));
        http.post("http://156.56.95.84:8080/OpenwordsExample/getWordsList", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                GetResult r = new Gson().fromJson(responseBody, GetResult.class);

                Word.deleteAllWords(DatabaseHandler.getInstance(null).getWritableDatabase());
                items = new ArrayList<Object[]>(r.words.size());
                for (Word w : r.words) {
                    items.add(new Object[]{w});
                    Word.addNewWord(DatabaseHandler.getInstance(null).getWritableDatabase(), w);
                }

                WordListActivity.refreshListHanlder.sendEmptyMessage(0);
                text1.setText("Language " + languageId + ", from remote server");
                Toast.makeText(WordListActivity.this, "Words loaded from remote server and saved locally", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String responseBody, Throwable error) {
                Toast.makeText(WordListActivity.this, "Failed to connect server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadWordsFromLocal(int languageId) {
        List<Word> words = Word.getAllWordsByLanguage(DatabaseHandler.getInstance(null).getReadableDatabase(), languageId);

        items = new ArrayList<Object[]>(words.size());
        for (Word w : words) {
            items.add(new Object[]{w});
            Word.addNewWord(DatabaseHandler.getInstance(null).getWritableDatabase(), w);
        }

        WordListActivity.refreshListHanlder.sendEmptyMessage(0);
        text1.setText("Language " + languageId + ", from local database");
        Toast.makeText(WordListActivity.this, "Words loaded from local database", Toast.LENGTH_LONG).show();
    }

    private void refreshWordList() {
        listAdapter.clear();
        listAdapter.addAll(items);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Word word = (Word) items.get(position)[0];
        Speak.getInstance(null).speak(word.getSpelling());
    }

    private class GetResult {

        public List<Word> words;
    }
}
