package com.openwords.database;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "user_performances")
public class UserPerformance implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void scanUserMissedConnections() {

    }

    public static int[] getPerformanceSummary(Session s, long userId, int baseLang, int learningLang) {
        String sql = "SELECT count(user_performances.word_connection_id) "
                + "FROM user_performances, word_connections "
                + "WHERE user_performances.word_connection_id=word_connections.connection_id "
                + "and word_connections.word1_language=@baseLang@ "
                + "and word_connections.word2_language=@learningLang@ "
                + "and user_performances.user_id=@userId@ "
                + "and user_performances.performance='good'";
        sql = sql.replace("@baseLang@", String.valueOf(baseLang))
                .replace("@learningLang@", String.valueOf(learningLang))
                .replace("@userId@", String.valueOf(userId));

        Number totalGood = (Number) s.createSQLQuery(sql).uniqueResult();

        String sql2 = "SELECT count(user_performances.word_connection_id) "
                + "FROM user_performances, word_connections "
                + "WHERE user_performances.word_connection_id=word_connections.connection_id "
                + "and word_connections.word1_language=@baseLang@ "
                + "and word_connections.word2_language=@learningLang@ "
                + "and user_performances.user_id=@userId@";
        sql2 = sql2.replace("@baseLang@", String.valueOf(baseLang))
                .replace("@learningLang@", String.valueOf(learningLang))
                .replace("@userId@", String.valueOf(userId));

        Number total = (Number) s.createSQLQuery(sql2).uniqueResult();

        if (total == null) {
            return new int[4];
        }

        String sql3 = "SELECT sum(user_performances.version) "
                + "FROM user_performances, word_connections "
                + "WHERE user_performances.word_connection_id=word_connections.connection_id "
                + "and word_connections.word1_language=@baseLang@ "
                + "and word_connections.word2_language=@learningLang@ "
                + "and user_performances.user_id=@userId@";
        sql3 = sql3.replace("@baseLang@", String.valueOf(baseLang))
                .replace("@learningLang@", String.valueOf(learningLang))
                .replace("@userId@", String.valueOf(userId));

        Number totalVersion = (Number) s.createSQLQuery(sql3).uniqueResult();

        if (totalGood == null) {
            totalGood = 0;
        }
        if (totalVersion == null) {
            totalVersion = 0;
        }

        int totalWordsInLanguage = Word.countLanguageWord(s, learningLang);

        return new int[]{totalGood.intValue(), total.intValue(), totalVersion.intValue() - total.intValue(), totalWordsInLanguage};
    }

    public static int countPerformance(Session s, long userId, int langOneId, int langTwoId, String learningType) {
        String sql = "select count(perf.word_connection_id) from user_performances perf,word_connections c "
                + "where perf.word_connection_id=c.connection_id "
                + "and c.word1_language=@langOneId@ "
                + "and c.word2_language=@langTwoId@ "
                + "and perf.user_id=@userId@ "
                + "and perf.learning_type='@learningType@'";

        sql = sql.replace("@langOneId@", String.valueOf(langOneId))
                .replace("@langTwoId@", String.valueOf(langTwoId))
                .replace("@userId@", String.valueOf(userId))
                .replace("@learningType@", String.valueOf(learningType));

        return ((Number) s.createSQLQuery(sql).uniqueResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    public static List<UserPerformance> getPerformancePage(Session s, long userId, int langOneId, int langTwoId, String learningType, int pageNumber, int pageSize) {
        int firstRecord = (pageNumber - 1) * pageSize;
        String sql = "select perf.* from user_performances perf,word_connections c "
                + "where perf.word_connection_id=c.connection_id "
                + "and c.word1_language=@langOneId@ "
                + "and c.word2_language=@langTwoId@ "
                + "and perf.user_id=@userId@ "
                + "and perf.learning_type='@learningType@' "
                + "order by perf.version desc, perf.word_connection_id asc "
                + "limit @firstRecord@,@pageSize@";

        sql = sql.replace("@langOneId@", String.valueOf(langOneId))
                .replace("@langTwoId@", String.valueOf(langTwoId))
                .replace("@userId@", String.valueOf(userId))
                .replace("@learningType@", String.valueOf(learningType))
                .replace("@firstRecord@", String.valueOf(firstRecord))
                .replace("@pageSize@", String.valueOf(pageSize));

        return s.createSQLQuery(sql).addEntity(UserPerformance.class).list();
    }

    @SuppressWarnings("unchecked")
    public static List<UserPerformance> getPerformances(Session s, long userId, Long[] connectionIds) {
        return s.createCriteria(UserPerformance.class)
                .add(Restrictions.eq("id.userId", userId))
                .add(Restrictions.in("id.wordConnectionId", connectionIds)).list();
    }

    public static void updatePerformances(Session s, List<UserPerformance> perfs, boolean skipOld) throws Exception {
        for (UserPerformance perf : perfs) {
            UserPerformance p = (UserPerformance) s.get(UserPerformance.class, perf.getId());
            if (p == null) {
                s.save(perf);
            } else {
                if (perf.getVersion() > p.getVersion()) {
                    p.setPerformance(perf.getPerformance());
                    p.setVersion(perf.getVersion());
                    p.setUpdatedTime(new Date());
                } else {
                    if (!skipOld) {
                        throw new Exception("performance is old: " + perf.getId());
                    }
                }

            }
        }
        s.beginTransaction().commit();
    }

    private UserPerformanceId id;

    private String performance;
    private Date updatedTime;
    private int version;//times

    public UserPerformance() {
    }

    public UserPerformance(UserPerformanceId id, String performance, int version) {
        this.id = id;
        this.performance = performance;
        this.version = version;
    }

    @Id
    @JSON(serialize = false, deserialize = false)
    public UserPerformanceId getId() {
        return id;
    }

    public void setId(UserPerformanceId id) {
        this.id = id;
    }

    @Column(name = "performance")
    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    @Column(name = "updated_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @JSON(serialize = false, deserialize = false)
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Transient
    public long getWordConnectionId() {
        return id.getWordConnectionId();
    }

    @Transient
    public String getLearningType() {
        return id.getLearningType();
    }

}
