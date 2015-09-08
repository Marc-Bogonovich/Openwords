package com.openwords.database;

import com.openwords.utils.UtilLog;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "word_connections")
public class WordConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    public static List<WordConnection> getSimilarWordsByLangOne(Session s, int langOne, int langTwo, String form, int pageNumber, int pageSize, int[] callbackTotal) {
        int firstRecord = (pageNumber - 1) * pageSize;
        String sql = "SELECT c.* "
                + "FROM word_connections as c, words as w "
                + "WHERE c.word1_language=@langOne@ "
                + "and c.word2_language=@langTwo@ "
                + "and c.word1_id=w.word_id "
                + "and w.word like \"%@form@%\" "
                + "limit @firstRecord@,@pageSize@";

        sql = sql.replace("@langOne@", String.valueOf(langOne))
                .replace("@langTwo@", String.valueOf(langTwo))
                .replace("@form@", form)
                .replace("@firstRecord@", String.valueOf(firstRecord))
                .replace("@pageSize@", String.valueOf(pageSize));

        @SuppressWarnings("unchecked")
        List<WordConnection> connections = s.createSQLQuery(sql).addEntity(WordConnection.class).list();

        sql = "SELECT count(c.connection_id) "
                + "FROM word_connections as c, words as w "
                + "WHERE c.word1_language=@langOne@ "
                + "and c.word2_language=@langTwo@ "
                + "and c.word1_id=w.word_id "
                + "and w.word like \"%@form@%\"";

        sql = sql.replace("@langOne@", String.valueOf(langOne))
                .replace("@langTwo@", String.valueOf(langTwo))
                .replace("@form@", form);

        Number total = (Number) s.createSQLQuery(sql).uniqueResult();
        callbackTotal[0] = total.intValue();

        return connections;
    }

    @SuppressWarnings("unchecked")
    public static List<WordConnection> getConnections(Session s, Collection<Long> connectionIds) {
        return s.createCriteria(WordConnection.class)
                .add(Restrictions.in("connectionId", connectionIds)).list();
    }

    public static List<Map<String, Object>> getWordConnectionPack(Session s, int langTwo) {
        String sql = "SELECT l2_info.connection_id, l2_info.word1_language, w.word_id as word1_id, w.word, ExtractValue(w.meta_info,\"/word/commonTranslation\") as common_translation,\n"
                + "l2_info.word2_language, l2_info.word2_id, l2_info.word as word2, ExtractValue(l2_info.meta_info,\"/word/commonTranslation\") as common_translation2,\n"
                + "l2_info.updated_time as connection_time\n"
                + "FROM\n"
                + "(SELECT c.connection_id, c.word1_id, c.word1_language, c.word2_id, w.word, w.meta_info, c.word2_language, c.updated_time\n"
                + "FROM word_connections as c, words as w\n"
                + "WHERE c.word1_language=1 and c.word2_language=@langTwo@ and c.word2_id=w.word_id) l2_info,\n"
                + "words as w\n"
                + "where w.word_id=l2_info.word1_id";
        sql = sql.replace("@langTwo@", String.valueOf(langTwo));
        List<Map<String, Object>> r = DatabaseHandler.Query(sql, s);
        return r;
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> getLearnableLanguageIds(Session s, int langOne) {
        return s.createCriteria(WordConnection.class)
                .add(Restrictions.eq("wordOneLangId", langOne))
                .setProjection(Projections.distinct(Projections.property("wordTwoLangId")))
                .list();
    }

    public static List<WordConnection> getConnectionsPageWithOrder(Session s, int langOneId, int langTwoId, int pageNumber, int pageSize, String orderItem, boolean includeWords) {
        int firstRecord = (pageNumber - 1) * pageSize;

        String sql = "select c.* from words w,word_connections c "
                + "where w.word_id=c.word1_id "
                + "and c.word1_language=@langOneId@ "
                + "and c.word2_language=@langTwoId@ "
                + "order by ExtractValue(w.meta_info,  \"/word/@orderItem@\")*1 desc "
                + "limit @firstRecord@,@pageSize@";

        sql = sql.replace("@langOneId@", String.valueOf(langOneId))
                .replace("@langTwoId@", String.valueOf(langTwoId))
                .replace("@orderItem@", orderItem)
                .replace("@firstRecord@", String.valueOf(firstRecord))
                .replace("@pageSize@", String.valueOf(pageSize));

        @SuppressWarnings("unchecked")
        List<WordConnection> connections = s.createSQLQuery(sql).addEntity(WordConnection.class).list();

        if (includeWords) {
            for (WordConnection record : connections) {
                record.setWordOne(Word.getWord(s, record.getWordOneId()));
                record.setWordTwo(Word.getWord(s, record.getWordTwoId()));
            }
        }
        return connections;
    }

    public static List<WordConnection> getWordAllConnections(Session s, String wordOne, boolean increaseRank) {
        List<Long> wordOneIds = Word.getWordIds(s, wordOne);
        if (wordOneIds.isEmpty()) {
            return null;
        }
        if (increaseRank) {
            Word.increaseRank(s, wordOneIds, "popRank");
            UtilLog.logInfo(WordConnection.class, "popRanks increased");
        }

        @SuppressWarnings("unchecked")
        List<WordConnection> conns = s.createCriteria(WordConnection.class).add(Restrictions.in("wordOneId", wordOneIds)).list();
        for (WordConnection conn : conns) {
            Word wordTwo = (Word) s.get(Word.class, conn.getWordTwoId());
            conn.setWordTwo(wordTwo);
            if (increaseRank) {
                Word.increaseRank(s, wordTwo, "popRank");
            }
        }
        return conns;
    }

    @SuppressWarnings("unchecked")
    public static List<WordConnection> getConnectionsPage(Session s, int langOneId, int langTwoId, int pageNumber, int pageSize, boolean includeWords) {
        int firstRecord = (pageNumber - 1) * pageSize;
        List<WordConnection> records = s.createCriteria(WordConnection.class)
                .add(Restrictions.eq("wordOneLangId", langOneId))
                .add(Restrictions.eq("wordTwoLangId", langTwoId))
                .addOrder(Order.asc("connectionId"))//ensure the default order
                .setFirstResult(firstRecord)
                .setMaxResults(pageSize)
                .list();

        if (includeWords) {
            for (WordConnection record : records) {
                record.setWordOne(Word.getWord(s, record.getWordOneId()));
                record.setWordTwo(Word.getWord(s, record.getWordTwoId()));
            }
        }
        return records;
    }

    public static void addConnection(Session s, WordConnection c) throws Exception {
        try {
            s.save(c);
            s.beginTransaction().commit();
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<WordConnection> getConnectionsByLanguage(Session s, long wordOneId, long wordTwoId) {
        return s.createCriteria(WordConnection.class)
                .add(Restrictions.eq("wordOneId", wordOneId))
                .add(Restrictions.eq("wordTwoId", wordTwoId))
                .list();
    }

    private long connectionId, wordOneId, wordTwoId;
    private int wordOneLangId, wordTwoLangId, connectionType;
    private Date updatedTime;
    private String contributor;
    private Word wordTwo, wordOne;

    public WordConnection() {
    }

    public WordConnection(long wordOneId, int wordOneLangId, long wordTwoId, int wordTwoLangId, int connectionType, String contributor) {
        this.wordOneId = wordOneId;
        this.wordOneLangId = wordOneLangId;
        this.wordTwoId = wordTwoId;
        this.wordTwoLangId = wordTwoLangId;
        this.connectionType = connectionType;
        this.contributor = contributor;
    }

    @Id
    @GeneratedValue
    @Column(name = "connection_id")
    public long getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(long connectionId) {
        this.connectionId = connectionId;
    }

    @Column(name = "word1_id")
    public long getWordOneId() {
        return wordOneId;
    }

    public void setWordOneId(long wordOneId) {
        this.wordOneId = wordOneId;
    }

    @Column(name = "word2_id")
    public long getWordTwoId() {
        return wordTwoId;
    }

    public void setWordTwoId(long wordTwoId) {
        this.wordTwoId = wordTwoId;
    }

    @Column(name = "connection_type")
    public int getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }

    @Column(name = "updated_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Column(name = "contributor_id")
    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    @Column(name = "word1_language")
    public int getWordOneLangId() {
        return wordOneLangId;
    }

    public void setWordOneLangId(int wordOneLangId) {
        this.wordOneLangId = wordOneLangId;
    }

    @Column(name = "word2_language")
    public int getWordTwoLangId() {
        return wordTwoLangId;
    }

    public void setWordTwoLangId(int wordTwoLangId) {
        this.wordTwoLangId = wordTwoLangId;
    }

    @Transient
    public Word getWordTwo() {
        return wordTwo;
    }

    public void setWordTwo(Word wordTwo) {
        this.wordTwo = wordTwo;
    }

    @Transient
    public Word getWordOne() {
        return wordOne;
    }

    public void setWordOne(Word wordOne) {
        this.wordOne = wordOne;
    }

    @Transient
    public long getUpdatedTimeLong() {
        return updatedTime.getTime();
    }
}
