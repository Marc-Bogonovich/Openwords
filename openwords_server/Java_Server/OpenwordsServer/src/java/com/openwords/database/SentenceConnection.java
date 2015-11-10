package com.openwords.database;

import static com.openwords.database.Word.Universal_Language;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "sentence_connections")
public class SentenceConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    public static int count(Session s, int langOne, int langTwo) {
        if (langOne == Universal_Language) {
            return ((Number) s.createCriteria(SentenceConnection.class)
                    .add(Restrictions.eq("langTwo", langTwo))
                    .setProjection(Projections.rowCount())
                    .uniqueResult()).intValue();

        } else if (langTwo == Universal_Language) {
            return ((Number) s.createCriteria(SentenceConnection.class)
                    .add(Restrictions.eq("langTwo", langOne))
                    .setProjection(Projections.rowCount())
                    .uniqueResult()).intValue();
        } else {
            String sql = "SELECT count(lang_one.lang_one_uni) FROM \n"
                    + "(SELECT uni_sentence_id as lang_two_uni FROM sentence_connections WHERE lang_two=@langTwo@) lang_two,\n"
                    + "(SELECT uni_sentence_id as lang_one_uni FROM sentence_connections WHERE lang_two=@langOne@) lang_one\n"
                    + "WHERE lang_two.lang_two_uni = lang_one.lang_one_uni";

            sql = sql.replace("@langTwo@", String.valueOf(langTwo))
                    .replace("@langOne@", String.valueOf(langOne));

            return ((Number) s.createSQLQuery(sql).uniqueResult()).intValue();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<SentenceConnection> getConnectionPage(Session s, int pageNumber, int pageSize, int langOne, int langTwo) {
        int firstRecord = (pageNumber - 1) * pageSize;
        if (langOne == Universal_Language) {
            return s.createCriteria(SentenceConnection.class)
                    .add(Restrictions.eq("langTwo", langTwo))
                    .setFirstResult(firstRecord)
                    .setMaxResults(pageSize)
                    .list();

        } else if (langTwo == Universal_Language) {
            return s.createCriteria(SentenceConnection.class)
                    .add(Restrictions.eq("langTwo", langOne))
                    .setFirstResult(firstRecord)
                    .setMaxResults(pageSize)
                    .list();
        } else {
            String sql = "SELECT * FROM\n"
                    + "(SELECT uni_sentence_id as lang_two_uni, sentence_id, lang_two, connection_type FROM sentence_connections WHERE lang_two=@langTwo@) lang_two,\n"
                    + "(SELECT uni_sentence_id as lang_one_uni, sentence_id as uni_sentence_id FROM sentence_connections WHERE lang_two=@langOne@) lang_one\n"
                    + "WHERE lang_two.lang_two_uni = lang_one.lang_one_uni "
                    + "limit @firstRecord@,@pageSize@";

            sql = sql.replace("@langTwo@", String.valueOf(langTwo))
                    .replace("@langOne@", String.valueOf(langOne))
                    .replace("@firstRecord@", String.valueOf(firstRecord))
                    .replace("@pageSize@", String.valueOf(pageSize));

            return s.createSQLQuery(sql).addEntity(SentenceConnection.class).list();
        }
    }

    private long uniId, sentenceId;
    private int langTwo;
    private String type;

    public SentenceConnection() {
    }

    public SentenceConnection(long uniId, long sentenceId, int langTwo, String type) {
        this.uniId = uniId;
        this.sentenceId = sentenceId;
        this.langTwo = langTwo;
        this.type = type;
    }

    @Id
    @Column(name = "uni_sentence_id")
    public long getUniId() {
        return uniId;
    }

    public void setUniId(long uniId) {
        this.uniId = uniId;
    }

    @Id
    @Column(name = "sentence_id")
    public long getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(long sentenceId) {
        this.sentenceId = sentenceId;
    }

    @Column(name = "connection_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Id
    @Column(name = "lang_two")
    public int getLangTwo() {
        return langTwo;
    }

    public void setLangTwo(int langTwo) {
        this.langTwo = langTwo;
    }

}
