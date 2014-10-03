package com.openwords.sound;

import android.content.Context;
import android.widget.Toast;
import com.google.gson.Gson;
import com.openwords.model.WordAudio;
import com.openwords.services.GetWordAudio;
import com.openwords.services.GetWordAudioNames;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.log.LogUtil;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class WordAudioManager {

    public static int[] checkExistingAudios(int[] wordIds) {
        //to-do
        return null;
    }

    public static void addAudioFiles(final int[] wordIds, final Context context, final AsyncCallback callback) {
        LogUtil.logDeubg(WordAudioManager.class, "Download audios for: " + new Gson().toJson(wordIds));
        GetWordAudioNames.request(wordIds, 0, new GetWordAudioNames.AsyncCallback() {

            public void callback(WordAudio[] names, Throwable error) {
                if (error == null) {
                    //save to database
                    for (WordAudio audio : names) {//to-do, need to check duplicates
                        audio.save();
                    }

                    if (wordIds.length == names.length) {
                        Toast.makeText(context, "We have all audio files!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "We do no have all audio files!", Toast.LENGTH_SHORT).show();
                    }

                    //get and save files bundle
                    File temp = new File(LocalFileSystem.Folders[0] + "temp");
                    GetWordAudio.request(wordIds, 0, new GetWordAudio.AsyncCallback() {

                        public void callback(File file, Throwable error) {
                            if (file != null) {
                                try {
                                    List<String> files = LocalFileSystem.unzipAudioPackage(file);
                                    //Toast.makeText(context, "Files are saved!\n" + files.toString(), Toast.LENGTH_SHORT).show();
                                } catch (IOException ex) {
                                    LogUtil.logWarning(this, ex);
                                    Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (callback != null) {
                                callback.doneAddAudioFiles();
                            }
                        }
                    }, temp);

                } else {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String hasAudio(int wordId) {
        List<WordAudio> r = Select.from(WordAudio.class).where(Condition.prop("word_id").eq(wordId)).list();
        if (!r.isEmpty()) {
            return r.get(r.size() - 1).getFileName();
        }
        return null;
    }

    private WordAudioManager() {
    }

    public interface AsyncCallback {

        public void doneAddAudioFiles();
    }
}
