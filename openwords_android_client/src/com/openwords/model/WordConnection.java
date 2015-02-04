package com.openwords.model;

import com.orm.SugarRecord;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordConnection extends SugarRecord<WordConnection> {
    
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
