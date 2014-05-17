package com.openwords.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.openwords.R;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.tts.Speak;
import java.util.List;
import org.apache.http.Header;

/**
 *
 * @author hanaldo
 */
public class MainActivity extends Activity {

    private static final String className = MainActivity.class.getName();
    private Button saveWord, loadFromRemote, loadFromLocal;
    private EditText edit1, edit2, edit3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Speak.getInstance(this);
        DatabaseHandler.getInstance(this);

        setContentView(R.layout.activity_main);

        saveWord = (Button) findViewById(R.id.act_main_button1);
        loadFromRemote = (Button) findViewById(R.id.act_main_button2);
        loadFromLocal = (Button) findViewById(R.id.act_main_button3);
        edit1 = (EditText) findViewById(R.id.act_main_edit1);
        edit2 = (EditText) findViewById(R.id.act_main_edit2);
        edit3 = (EditText) findViewById(R.id.act_main_edit3);
        
        //hey, I am making some changes!

        loadFromRemote.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                loadWords("remote");
            }
        });

        loadFromLocal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                loadWords("local");
            }
        });

        saveWord.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String id = edit1.getText().toString();
                if (id.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please give a language ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                String spelling = edit2.getText().toString();
                if (spelling.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please give the word spelling", Toast.LENGTH_SHORT).show();
                    return;
                }

                int idValue;
                try {
                    idValue = Integer.parseInt(id);
                } catch (Exception e) {
                    Log.w(className, e);
                    Toast.makeText(MainActivity.this, "The language ID is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                edit1.setText(null);
                edit2.setText(null);

                final AsyncHttpClient http = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("languageId", String.valueOf(idValue));
                params.put("spelling", spelling);
                http.post("http://156.56.95.84:8080/OpenwordsExample/addWord", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                        Toast.makeText(MainActivity.this, "Word is saved on server", Toast.LENGTH_SHORT).show();
                        Speak.getInstance(null).speak("Word is saved on server");
                    }

                    @Override
                    public void onFailure(String responseBody, Throwable error) {
                        Toast.makeText(MainActivity.this, "Saving word failed", Toast.LENGTH_SHORT).show();
                        Speak.getInstance(null).speak("Saving word failed");
                    }
                });

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Speak.getInstance(null).clean();
        DatabaseHandler.getInstance(null).clean();
        Toast.makeText(this, "bye bye", Toast.LENGTH_SHORT).show();
    }

    private void loadWords(String mode) {
        String id = edit3.getText().toString();
        if (id.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please give a language ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int idValue;
        try {
            idValue = Integer.parseInt(id);
        } catch (Exception e) {
            Log.w(className, e);
            Toast.makeText(MainActivity.this, "The language ID is not valid", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(MainActivity.this, WordListActivity.class);
        i.putExtra("mode", mode);
        i.putExtra("gid", idValue);
        startActivity(i);
    }

    private class GetResult {

        public List<Word> words;
    }
}
