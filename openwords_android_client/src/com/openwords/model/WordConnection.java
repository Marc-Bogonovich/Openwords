package com.openwords.model;

import com.openwords.services.implementations.GetWordConnectionsPack;
import com.openwords.services.interfaces.HttpResultHandler;
import com.orm.SugarRecord;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordConnection extends SugarRecord<WordConnection> {

    private static void loadWordConnectionsFullPackLocally(int langOneId, int langTwoId, int pageNumber, int pageSize, ResultWordConnections resultHandler) {
        List<WordConnection> connections = getConnections(langOneId, langTwoId, pageSize, pageNumber);
        Set<Integer> ids = new HashSet<Integer>(connections.size());
        for (WordConnection wc : connections) {
            ids.add(wc.wordOneId);
            ids.add(wc.wordTwoId);
        }
        List<Word> words = Word.getWords(ids);
        if (ids.size() != words.size()) {
            resultHandler.result(null, null, null);
            return;
        }
        List<Performance> performance = Performance.loadUserPerformanceLocally(unpackConnectionIds(connections), "all");
        if (performance.size() != connections.size()) {
            resultHandler.result(null, null, null);
            return;
        }
        resultHandler.result(connections, words, performance);
    }

    public static void loadWordConnectionsFullPack(boolean tryRemote, final int userId,
            final int langOneId, final int langTwoId, final int pageNumber, final int pageSize,
            boolean doOrder, String orderBy,
            final ResultWordConnections resultHandler) {

        if (!tryRemote) {
            loadWordConnectionsFullPackLocally(langOneId, langTwoId, pageNumber, pageSize, resultHandler);
            return;
        }

        new GetWordConnectionsPack().doRequest(
                userId, langOneId, langTwoId, pageNumber, pageSize, doOrder, orderBy,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        Object[] r = (Object[]) resultObject;
                        @SuppressWarnings("unchecked")
                        List<WordConnection> connections = (List<WordConnection>) r[0];
                        @SuppressWarnings("unchecked")
                        List<Word> words = (List<Word>) r[1];
                        @SuppressWarnings("unchecked")
                        List<Performance> performance = (List<Performance>) r[2];

                        if (connections == null || words == null) {
                            resultHandler.result(null, null, null);
                            return;
                        }

                        saveOrUpdateAll(connections);
                        Word.saveOrUpdateAll(words);
                        Performance.saveOrUpdateAll(performance, "all");
                        resultHandler.result(connections, words, performance);
                    }

                    public void noResult(String errorMessage) {
                        loadWordConnectionsFullPackLocally(langOneId, langTwoId, pageNumber, pageSize, resultHandler);
                    }
                });
    }

    private static List<WordConnection> getConnections(int baseLang, int learningLang, int pageSize, int pageNumber) {
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

    public static Set<Integer> unpackConnectionIds(List<WordConnection> connections) {
        Set<Integer> l = new HashSet<Integer>(connections.size());
        for (WordConnection conn : connections) {
            l.add(conn.connectionId);
        }
        return l;
    }

    public int connectionId, wordOneId, wordOneLangId, wordTwoId, wordTwoLangId, connectionType;
    public long updatedTimeLong;
    public String contributor;

    public WordConnection() {
    }

}
