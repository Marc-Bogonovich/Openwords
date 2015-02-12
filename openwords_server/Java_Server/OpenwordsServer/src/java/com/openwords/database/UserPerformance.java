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

@Entity
@Table(name = "user_performances")
public class UserPerformance implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void updatePerformances(Session s, List<UserPerformance> perfs) {
        for (UserPerformance perf : perfs) {
            UserPerformance p = (UserPerformance) s.get(UserPerformance.class, perf.getId());
            if (p == null) {
                s.save(perf);
            } else {
                if (perf.getVersion() > p.getVersion()) {
                    p.setPerformance(perf.getPerformance());
                    p.setVersion(perf.getVersion());
                    p.setUpdatedTime(new Date());
                }
            }
        }
        s.beginTransaction().commit();
    }

    private UserPerformanceId id;

    private String performance;
    private Date updatedTime;
    private int version;//times

    private int wordConnectionId;
    private String learningType;

    public UserPerformance() {
    }

    public UserPerformance(UserPerformanceId id, String performance, int version) {
        this.id = id;
        this.performance = performance;
        this.version = version;
    }

    @Id
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
        return wordConnectionId;
    }

    public void setWordConnectionId(int wordConnectionId) {
        this.wordConnectionId = wordConnectionId;
    }

    @Transient
    public String getLearningType() {
        return learningType;
    }

    public void setLearningType(String learningType) {
        this.learningType = learningType;
    }

}
