package com.openwords.database;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "set_content")
public class SetContent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static void updateSetItems(Session s, long setId, Collection<SetContent> items) {
        String sql = "delete from set_content where set_id=@setId@";
        sql = sql.replace("@setId@", String.valueOf(setId));
        s.createSQLQuery(sql).executeUpdate();
        
        for (SetContent item : items) {
            item.getId().setSetId(setId);
            s.save(item);
        }
        s.beginTransaction().commit();
    }
    
    @SuppressWarnings("unchecked")
    public static List<SetContent> getSetItems(Session s, long setId) {
        return s.createCriteria(SetContent.class)
                .add(Restrictions.eq("id.setId", setId))
                .addOrder(Order.asc("itemOrder"))
                .list();
    }
    
    private SetContentId id;
    private int direction, itemOrder;
    
    public SetContent() {
    }
    
    public SetContent(SetContentId id, int direction, int itemOrder) {
        this.id = id;
        this.direction = direction;
        this.itemOrder = itemOrder;
    }
    
    @Id
    public SetContentId getId() {
        return id;
    }
    
    public void setId(SetContentId id) {
        this.id = id;
    }
    
    @Column(name = "direction")
    public int getDirection() {
        return direction;
    }
    
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    @Column(name = "item_order")
    public int getItemOrder() {
        return itemOrder;
    }
    
    public void setItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }
    
}
