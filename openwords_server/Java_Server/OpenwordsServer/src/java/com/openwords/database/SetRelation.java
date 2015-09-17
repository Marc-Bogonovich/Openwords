package com.openwords.database;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "set_relations")
public class SetRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void updateRelation(Session s, long setId, long userId, int type, String meta) {
        @SuppressWarnings("unchecked")
        List<SetRelation> r = s.createCriteria(SetRelation.class)
                .add(Restrictions.eq("setId", setId))
                .add(Restrictions.eq("userId", userId))
                .list();
        if (r.isEmpty()) {
            s.save(new SetRelation(setId, userId, type, meta));
        } else {
            SetRelation relation = r.get(0);
            relation.setType(type);
            relation.setMeta(meta);
        }
        s.beginTransaction().commit();
    }

    private long setId, userId;
    private int type;
    private String meta;

    public SetRelation() {
    }

    public SetRelation(long setId, long userId, int type, String meta) {
        this.setId = setId;
        this.userId = userId;
        this.type = type;
        this.meta = meta;
    }

    @Id
    @Column(name = "set_id")
    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "relation_type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "relation_meta")
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

}
