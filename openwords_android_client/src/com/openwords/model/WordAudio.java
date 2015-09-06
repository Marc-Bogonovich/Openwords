package com.openwords.model;

import com.openwords.services.implementations.GetWordAudio;
import com.openwords.services.implementations.GetWordAudioNames;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.log.LogUtil;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordAudio extends SugarRecord<WordAudio> {

    public static List<WordAudio> getAudios(Set<Integer> ids) {
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        String whereSql = "word_id in " + sqlIds;
        return Select.from(WordAudio.class).where(whereSql).list();
    }

    public static WordAudio getAudio(int wordId) {
        List<WordAudio> r = Select.from(WordAudio.class).where(Condition.prop("word_id").eq(wordId)).list();
        if (r.isEmpty()) {
            return null;
        }
        return r.get(0);
    }

    private static void deleteAudios(Collection<Integer> wordIds) {
        if (wordIds.isEmpty()) {
            return;
        }
        String sqlIds = wordIds.toString().replace("[", "(").replace("]", ")");
        String sql = "delete from word_audio where word_id in " + sqlIds;
        WordAudio.executeQuery(sql);
    }

    private static void saveOrUpdateAll(WordAudio[] audios) {
        Set<Integer> ids = new HashSet<Integer>(audios.length);
        for (WordAudio a : audios) {
            ids.add(a.wordId);
        }
        deleteAudios(ids);
        WordAudio.saveInTx(audios);
    }

    public static void downloadNewAudios(final Collection<Integer> wordIds, final int languge, final ResultWordAudio resultHandler) {
        new GetWordAudioNames().doRequest(wordIds, languge,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        GetWordAudioNames.Result r = (GetWordAudioNames.Result) resultObject;
                        if (r.result.length == 0) {
                            resultHandler.error("no audio at all");
                            return;
                        }
                        WordAudio.saveOrUpdateAll(r.result);

                        //get and save files bundle
                        File temp = new File(LocalFileSystem.Folders[0] + "temp");
                        GetWordAudio.request(wordIds, languge, 0, new GetWordAudio.AsyncCallback() {

                            public void callback(File file, Throwable error) {
                                if (file != null) {
                                    try {
                                        List<String> files = LocalFileSystem.unzipAudioPackage(file);
                                        LogUtil.logDeubg(this, "files saved\n" + files.toString());
                                        resultHandler.ok();
                                    } catch (IOException ex) {
                                        LogUtil.logWarning(this, ex);
                                        resultHandler.error(ex.toString());
                                    }
                                } else {
                                    LogUtil.logDeubg(this, error);
                                    resultHandler.error(error.toString());
                                }
                            }
                        }, temp);
                    }

                    public void noResult(String errorMessage) {
                        resultHandler.error(errorMessage);
                    }
                });

    }
    public int wordId;
    public String fileName;

    public WordAudio() {
    }
}
