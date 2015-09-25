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
@Table(name = "set_items")
public class SetItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void updateSetItems(Session s, long setId, Collection<SetItem> items) {
        String sql = "delete from set_items where set_id=@setId@";
        sql = sql.replace("@setId@", String.valueOf(setId));
        s.createSQLQuery(sql).executeUpdate();

        for (SetItem item : items) {
            item.setSetId(setId);
            s.save(item);
        }
        s.beginTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public static List<SetItem> getSetItems(Session s, long setId) {
        return s.createCriteria(SetItem.class)
                .add(Restrictions.eq("setId", setId))
                .addOrder(Order.asc("itemOrder"))
                .list();
    }

    private long setId, wordId;
    private int itemOrder;

    public SetItem() {
    }

    public SetItem(long wordId, int itemOrder) {
        this.wordId = wordId;
        this.itemOrder = itemOrder;
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
    @Column(name = "word_id")
    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    @Column(name = "item_order")
    public int getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }

}
