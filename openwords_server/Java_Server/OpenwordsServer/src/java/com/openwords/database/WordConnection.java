package com.openwords.database;

import com.openwords.utils.UtilLog;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
        List<Integer> wordOneIds = Word.getWordIds(s, wordOne);
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
    public static List<WordConnection> getConnectionByIds(Session s, int wordOneId, int wordTwoId) {
        return s.createCriteria(WordConnection.class)
                .add(Restrictions.eq("wordOneId", wordOneId))
                .add(Restrictions.eq("wordTwoId", wordTwoId))
                .list();
    }

    private int connectionId, wordOneId, wordOneLangId, wordTwoId, wordTwoLangId, connectionType;
    private Date updatedTime;
    private String contributor;
    private Word wordTwo, wordOne;

    public WordConnection() {
    }

    public WordConnection(int wordOneId, int wordOneLangId, int wordTwoId, int wordTwoLangId, int connectionType, String contributor) {
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
    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    @Column(name = "word1_id")
    public int getWordOneId() {
        return wordOneId;
    }

    public void setWordOneId(int wordOneId) {
        this.wordOneId = wordOneId;
    }

    @Column(name = "word2_id")
    public int getWordTwoId() {
        return wordTwoId;
    }

    public void setWordTwoId(int wordTwoId) {
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
