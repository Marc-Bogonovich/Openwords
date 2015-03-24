package com.openwords.model;

import com.openwords.util.log.LogUtil;
import com.orm.SugarRecord;
import com.orm.query.Select;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Performance extends SugarRecord<Performance> {

    private synchronized static Performance getPerformance(int wordConnectionId, String learningType) {
        List<Performance> r = Performance.find(Performance.class, "word_connection_id = ? and learning_type = ?",
                String.valueOf(wordConnectionId), learningType);
        if (r.isEmpty()) {
            return null;
        } else {
            return r.get(0);
        }
    }

    public static List<Performance> loadUserPerformanceLocally(Set<Integer> connectionIds, String learningType) {
        LogUtil.logDeubg(UserLearningLanguages.class, "loadUserPerformanceLocally()");
        String sqlIds = connectionIds.toString().replace("[", "(").replace("]", ")");

        String whereSql = "word_connection_id in @ids@ and learning_type='@learningType@'";
        whereSql = whereSql.replace("@ids@", sqlIds)
                .replace("@learningType@", learningType);

        return Select.from(Performance.class).where(whereSql).list();
    }

    public static void saveOrUpdateAll(List<Performance> perfs, String learningType) {
        Set<Integer> ids = new HashSet<Integer>(perfs.size());
        for (Performance perf : perfs) {
            ids.add(perf.wordConnectionId);
        }
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        String sql = "delete from Performance where word_connection_id in @ids@ and learning_type='@learningType@'";
        sql = sql.replace("@ids@", sqlIds)
                .replace("@learningType@", learningType);
        Performance.executeQuery(sql);
        Performance.saveInTx(perfs);
    }
    public int wordConnectionId;
    public String learningType, performance;
    public long version, tempVersion;

    public Performance() {
    }

    public Performance(int wordConnectionId, String learningType, String performance, long version) {
        this.wordConnectionId = wordConnectionId;
        this.learningType = learningType;
        this.performance = performance;
        this.version = version;
    }

}
