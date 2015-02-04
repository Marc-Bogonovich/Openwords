package com.openwords.model;

import com.orm.SugarRecord;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordConnection extends SugarRecord<WordConnection> {

    public static List<WordConnection> getConnections(int baseLang, int learningLang, int pageSize, int pageNumber) {
        int firstRecord = (pageNumber - 1) * pageSize;
        String sql = "select * from word_connection "
                + "where word_one_lang_id=@baseLang@ "
                + "and word_two_lang_id=@learningLang@ "
                + "order by connection_id asc "
                + "limit @firstRecord@,@pageSize@";

        sql = sql.replace("@baseLang@", String.valueOf(baseLang))
                .replace("@learningLang@", String.valueOf(learningLang))
                .replace("@firstRecord@", String.valueOf(firstRecord))
                .replace("@pageSize@", String.valueOf(pageSize));

        return WordConnection.findWithQuery(WordConnection.class, sql);
    }

    public static void saveOrUpdateAll(List<WordConnection> cs) {
        Set<Integer> ids = new HashSet<Integer>(cs.size());
        for (WordConnection c : cs) {
            ids.add(c.connectionId);
        }
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        String sql = "delete from word_connection where connection_id in " + sqlIds;
        WordConnection.executeQuery(sql);
        WordConnection.saveInTx(cs);
    }

    public int connectionId, wordOneId, wordOneLangId, wordTwoId, wordTwoLangId, connectionType;
    public long updatedTimeLong;
    public String contributor;

    public WordConnection() {
    }

}
