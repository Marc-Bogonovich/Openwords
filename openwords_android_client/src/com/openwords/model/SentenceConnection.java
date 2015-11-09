package com.openwords.model;

import com.openwords.services.implementations.ServiceGetSentencePack;
import com.openwords.services.interfaces.HttpResultHandler;
import com.orm.SugarRecord;
import java.util.List;

public class SentenceConnection extends SugarRecord<SentenceConnection> {

    public static void loadSentencePack(int langTwo, final ResultSentencePack resultHandler) {
        new ServiceGetSentencePack().doRequest(langTwo, new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                ServiceGetSentencePack.Result r = (ServiceGetSentencePack.Result) resultObject;
                refreshAll(r.connections);
                Sentence.refreshAll(r.sentences);
                SentenceItem.refreshAll(r.items);

                resultHandler.result(r.connections);
            }

            public void noResult(String errorMessage) {
                resultHandler.error(errorMessage);
            }
        });
    }

    public static void refreshAll(List<SentenceConnection> sc) {
        for (SentenceConnection c : sc) {
            SentenceConnection.deleteAll(SentenceConnection.class,
                    "uni_id = ? and sentence_id = ?",
                    String.valueOf(c.uniId), String.valueOf(c.sentenceId));
        }
        SentenceConnection.saveInTx(sc);
    }

    public long uniId, sentenceId;
    public int langTwo;
    public String type;

    public SentenceConnection() {
    }

    public SentenceConnection(long uniId, long sentenceId, int langTwo, String type) {
        this.uniId = uniId;
        this.sentenceId = sentenceId;
        this.langTwo = langTwo;
        this.type = type;
    }

}
