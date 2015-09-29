package com.openwords.model;

import com.openwords.util.gson.MyGson;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Select;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Word extends SugarRecord<Word> {

    public static Word getWord(long wordId) {
        return Word.find(Word.class, "word_id = ?", String.valueOf(wordId)).get(0);
    }

    public static List<Word> getWords(Set<Long> ids) {
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        String whereSql = "word_id in " + sqlIds;
        return Select.from(Word.class).where(whereSql).list();
    }

    public static void saveOrUpdateAll(List<Word> ws) {
        Set<Long> ids = new HashSet<Long>(ws.size());
        for (Word c : ws) {
            ids.add(c.wordId);
            c.saveMetaToJson();
        }
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        String sql = "delete from word where word_id in " + sqlIds;
        Word.executeQuery(sql);
        Word.saveInTx(ws);
    }

    public long wordId;
    public int languageId;
    public String word, contributor;
    public long updatedTimeLong;
    @Ignore
    private WordMetaInfo wordMetaInfo;
    public String meta;

    public Word() {
    }

    public Word saveMetaToJson() {
        meta = MyGson.toJson(wordMetaInfo);
        return this;
    }

    public WordMetaInfo getMeta() {
        if (wordMetaInfo == null) {
            wordMetaInfo = (WordMetaInfo) MyGson.fromJson(meta, WordMetaInfo.class);
        }
        return wordMetaInfo;
    }
}
