package com.openwords.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sentence extends SugarRecord<Sentence> {

    public static void refreshAll(List<Sentence> ss) {
        Set<Long> ids = new HashSet<Long>(ss.size());
        for (Sentence c : ss) {
            ids.add(c.sentenceId);
            c.saveMeta();
        }
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        Sentence.deleteAll(Sentence.class, "sentence_id in " + sqlIds);
        Sentence.saveInTx(ss);
    }

    public static Sentence getSentence(long sentenceId) {
        return Sentence.find(Sentence.class, "sentence_id = ?", String.valueOf(sentenceId)).get(0);
    }

    public long sentenceId, userId;
    public int languageId;
    public String text, meta;
    public Date updatedTime;
    @Ignore
    private SentenceMetaInfo metaInfo;
    public boolean hasSpace;

    public Sentence() {
    }

    public Sentence(long sentenceId, int languageId, String text, String meta) {
        this.sentenceId = sentenceId;
        this.languageId = languageId;
        this.text = text;
        this.meta = meta;
    }

    public void saveMeta() {
        if (metaInfo != null) {
            hasSpace = metaInfo.hasSpace;
        }
    }
}
