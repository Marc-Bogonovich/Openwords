package com.openwords.test;

import static android.app.Activity.RESULT_OK;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.model.UserWords;
import com.openwords.services.GetWordAudio;
import com.openwords.sound.SoundPlayer;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.log.LogUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author hanaldo
 */
public class DialogSoundPlay2 extends ListActivity {

    public static final UserWords[] words = new UserWords[]{
        new UserWords(5, 0, null, 0, null, 0, null, "5_zidian_man1_48.ogg"),
        new UserWords(17, 0, null, 0, null, 0, null, "17_ziyou_man1_48.ogg"),
        new UserWords(48, 0, null, 0, null, 0, null, "48_mao_man1_48.ogg")
    };

    public static boolean downloaded = false;

    public static String[] options = new String[5];

    private ArrayAdapter<String> adapter;
    private final AtomicBoolean downloading = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_2);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        makeOptions();
        setListAdapter(adapter);
    }

    private void makeOptions() {
        if (downloaded) {
            options[0] = "<Files Are Downloaded>";
            options[4] = "<Delete All Audio Files>";
        } else {
            options[0] = "<Download Those Audio Files>";
            options[4] = "";
        }

        int i = 1;
        for (UserWords w : words) {
            if (downloaded) {
                options[i] = "Play local " + w.audiocall;
            } else {
                options[i] = "Play remote " + w.audiocall;
            }
            i++;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position == 0) {
            if (downloaded || downloading.get()) {
                Toast.makeText(DialogSoundPlay2.this, "Files are already downloaded or being downloaded now!", Toast.LENGTH_SHORT).show();
                return;
            }

            downloading.set(true);
            File temp = new File(LocalFileSystem.Folders[0] + "temp");
            options[0] = "<Downloading...>";
            adapter.notifyDataSetChanged();
            GetWordAudio.request(new int[]{5, 17, 48}, RESULT_OK, new GetWordAudio.AsyncCallback() {

                public void callback(File file, Throwable error) {
                    if (file != null) {
                        try {
                            List<String> files = LocalFileSystem.unzipAudioPackage(file);
                            Toast.makeText(DialogSoundPlay2.this, "Files are saved!\n" + files.toString(), Toast.LENGTH_SHORT).show();
                            for (UserWords word : words) {
                                word.audioIsLocal = true;
                            }
                            downloaded = true;
                        } catch (IOException ex) {
                            LogUtil.logWarning(this, ex);
                            Toast.makeText(DialogSoundPlay2.this, ex.toString(), Toast.LENGTH_SHORT).show();
                            downloaded = false;
                        }
                    } else {
                        Toast.makeText(DialogSoundPlay2.this, error.toString(), Toast.LENGTH_SHORT).show();
                        downloaded = false;
                    }
                    downloading.set(false);
                    makeOptions();
                }
            }, temp);
        } else if (position == 4) {
            for (UserWords w : words) {
                w.audioIsLocal = false;
            }
            downloaded = false;
            makeOptions();
        } else {
            UserWords w = words[position - 1];
            if (w.audioIsLocal) {
                SoundPlayer.playMusic(LocalFileSystem.getAudioFullPath(w.audiocall), true);
            } else {
                SoundPlayer.playMusic("http://openwords.org/api-v1/audio/" + w.audiocall, false);
            }
        }
    }
}
