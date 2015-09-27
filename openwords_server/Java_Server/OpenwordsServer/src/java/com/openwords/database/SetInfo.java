package com.openwords.database;

import com.openwords.utils.MyXStream;
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
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "set_info")
public class SetInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void createNewSet(Session s, SetInfo set) {
        s.save(set);
        s.beginTransaction().commit();
    }

    public static void updateSetInfo(Session s, SetInfo set) {
        SetInfo old = (SetInfo) s.get(SetInfo.class, set.getSetId());
        old.setVisibility(set.getVisibility());
        old.setName(set.getName());
        old.setUpdatedTime(new Date());
        s.beginTransaction().commit();
    }

    public static SetInfo getSetInfoByName(Session s, String name, long userId) {
        @SuppressWarnings("unchecked")
        List<SetInfo> r = s.createCriteria(SetInfo.class)
                .add(Restrictions.eq("name", name))
                .add(Restrictions.eq("userId", userId))
                .list();
        if (r.isEmpty()) {
            return null;
        }
        return r.get(0);
    }

    @SuppressWarnings("unchecked")
    public static List<SetInfo> getAllSets(Session s, int pageNumber, int pageSize) {
        int firstRecord = (pageNumber - 1) * pageSize;
        return s.createCriteria(SetInfo.class)
                .setFirstResult(firstRecord)
                .setMaxResults(pageSize)
                .list();
    }

    public static SetInfo getSetInfo(Session s, long setId) {
        return (SetInfo) s.get(SetInfo.class, setId);
    }

    private long setId, userId;
    private int learningLang, visibility, setSize;
    private String name, meta;
    private boolean valid;
    private Date updatedTime;
    private SetMetaInfo metaInfo;
    //private int maxSize;//need to think about items number, if too many then need to rework UpdateSetItems. 

    public SetInfo() {
    }

    public SetInfo(long userId, int learningLang, int visibility, String name, boolean valid, String meta) {
        this.userId = userId;
        this.learningLang = learningLang;
        this.visibility = visibility;
        this.name = name;
        this.valid = valid;
        this.meta = meta;
    }

    @Id
    @GeneratedValue
    @Column(name = "set_id")
    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "learning_lang")
    public int getLearningLang() {
        return learningLang;
    }

    public void setLearningLang(int learningLang) {
        this.learningLang = learningLang;
    }

    @Column(name = "visibility")
    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @Column(name = "set_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "valid")
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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

    @Transient
    public long getUpdatedTimeLong() {
        return updatedTime.getTime();
    }

    @Column(name = "meta_info")
    @JSON(serialize = false, deserialize = false)
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Transient
    public SetMetaInfo getWordMetaInfo() {
        if (metaInfo == null) {
            metaInfo = (SetMetaInfo) MyXStream.fromXml(meta);
        }
        return metaInfo;
    }

    @Column(name = "set_size")
    public int getSetSize() {
        return setSize;
    }

    public void setSetSize(int setSize) {
        this.setSize = setSize;
    }
}
