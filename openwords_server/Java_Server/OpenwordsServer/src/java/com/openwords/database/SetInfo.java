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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "set_info")
public class SetInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static SetInfo updateSet(Session s, SetInfo set) {
        SetInfo r;
        if (set.getSetId() <= 0) {
            set.setUpdatedTime(new Date());
            s.save(set);
            r = set;
        } else {
            SetInfo old = (SetInfo) s.get(SetInfo.class, set.getSetId());
            old.setName(set.getName());
            old.setUpdatedTime(new Date());
            r = old;
        }
        s.beginTransaction().commit();
        return r;
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
    public static List<SetInfo> getAllSets(Session s, int pageNumber, int pageSize, int langOne, int langTwo) {
        int firstRecord = (pageNumber - 1) * pageSize;
        Criteria c = s.createCriteria(SetInfo.class);
        if (langOne != 0 && langTwo != 0) {
            c.add(Restrictions.eq("nativeLang", langOne))
                    .add(Restrictions.eq("learningLang", langTwo));
        }
        return c.setFirstResult(firstRecord)
                .setMaxResults(pageSize)
                .list();
    }

    public static SetInfo getSetInfo(Session s, long setId) {
        return (SetInfo) s.get(SetInfo.class, setId);
    }

    private long setId, userId;
    private int nativeLang, learningLang, visibility, setSize;
    private String name, meta;
    private boolean valid;
    private Date updatedTime;
    private SetMetaInfo metaInfo;
    //private int maxSize;//need to think about items number, if too many then need to rework UpdateSetItems. 

    public SetInfo() {
    }

    public SetInfo(long setId, long userId, int nativeLang, int learningLang, int visibility, String name, boolean valid, String meta) {
        this.setId = setId;
        this.userId = userId;
        this.nativeLang = nativeLang;
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

    @Column(name = "native_lang")
    public int getNativeLang() {
        return nativeLang;
    }

    public void setNativeLang(int nativeLang) {
        this.nativeLang = nativeLang;
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
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Transient
    @JSON(serialize = false, deserialize = false)
    public SetMetaInfo getMetaInfo() {
        if (metaInfo == null && meta != null) {
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
