package com.openwords.model;

import com.openwords.util.gson.MyGson;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Word extends SugarRecord<Word> {

    public static void saveOrUpdateAll(List<Word> ws) {
        Set<Integer> ids = new HashSet<Integer>(ws.size());
        for (Word c : ws) {
            ids.add(c.wordId);
            c.saveMetaToJson();
        }
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        String sql = "delete from word where word_id in " + sqlIds;
        Word.executeQuery(sql);
        Word.saveInTx(ws);
    }

    public int wordId, languageId;
    public String word, contributor;
    public long updatedTimeLong;
    @Ignore
    public WordMetaInfo wordMetaInfo;
    public String meta;

    public Word() {
    }

    public Word saveMetaToJson() {
        meta = MyGson.toJson(wordMetaInfo);
        return this;
    }
}
