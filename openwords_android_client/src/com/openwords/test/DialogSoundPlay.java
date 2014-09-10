package com.openwords.test;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.sound.SoundPlayer;

/**
 *
 * @author hanaldo
 */
public class DialogSoundPlay extends ListActivity {

    private final String[] names = new String[]{
        "earth_128.mp3",
        "earth_128.ogg",
        "earth_128.m4a",
        "earth_64.mp3",
        "earth_64.ogg",
        "earth_64.m4a"};

    private final String[] size = new String[]{
        "24KB",
        "21KB",
        "19KB",
        "12KB",
        "14KB",
        "9KB"};

    private final String[] info = new String[]{
        "128kbps mp3/mp3",
        "128kbps vorbis/ogg",
        "128kbps aac/m4a",
        "64kbps mp3/mp3",
        "64kbps vorbis/ogg",
        "64kbps aac/m4a"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_2);

        String[] options = new String[]{
            "say \"地球\" in " + info[0] + " (" + size[0] + ")",
            "say \"地球\" in " + info[1] + " (" + size[1] + ")",
            "say \"地球\" in " + info[2] + " (" + size[2] + ")",
            "say \"地球\" in " + info[3] + " (" + size[3] + ")",
            "say \"地球\" in " + info[4] + " (" + size[4] + ")",
            "say \"地球\" in " + info[5] + " (" + size[5] + ")",
            "stream \"地球\" in " + info[0] + " (" + size[0] + ")",
            "stream \"地球\" in " + info[1] + " (" + size[1] + ")",
            "stream \"地球\" in " + info[2] + " (" + size[2] + ")",
            "stream \"地球\" in " + info[3] + " (" + size[3] + ")",
            "stream \"地球\" in " + info[4] + " (" + size[4] + ")",
            "stream \"地球\" in " + info[5] + " (" + size[5] + ")"};

        ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        setListAdapter(a);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (position < 6) {
            SoundPlayer.playMusic(Environment.getExternalStorageDirectory().getPath() + "/OpenwordsSound/" + names[position], true);
            Toast.makeText(this, info[position] + " " + size[position], Toast.LENGTH_SHORT).show();
        } else {
            SoundPlayer.playMusic("http://www.openwords.com/api-v1/audio/" + names[position - 6], false);
            Toast.makeText(this, info[position - 6] + " " + size[position - 6], Toast.LENGTH_SHORT).show();
        }
    }
}
