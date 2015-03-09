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

    @SuppressWarnings("unchecked")
    public static List<UserPerformance> getPerformances(Session s, int userId, Integer[] connectionIds) {
        return s.createCriteria(UserPerformance.class)
                .add(Restrictions.eq("id.userId", userId))
                .add(Restrictions.in("id.wordConnectionId", connectionIds)).list();
    }

    public static void updatePerformances(Session s, List<UserPerformance> perfs) throws Exception {
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
                    throw new Exception("performance is old: " + perf.getId());
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
    public int getWordConnectionId() {
        return id.getWordConnectionId();
    }

    @Transient
    public String getLearningType() {
        return id.getLearningType();
    }

}
