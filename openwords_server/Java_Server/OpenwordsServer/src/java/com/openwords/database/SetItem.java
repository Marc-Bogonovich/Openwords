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

    private long setId, wordOneId, wordTwoId;
    private int itemOrder;
    private String wordOne, wordTwo;

    public SetItem() {
    }

    public SetItem(long wordOneId, long wordTwoId, int itemOrder, String wordOne, String wordTwo) {
        this.wordOneId = wordOneId;
        this.wordTwoId = wordTwoId;
        this.itemOrder = itemOrder;
        this.wordOne = wordOne;
        this.wordTwo = wordTwo;
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
    @Column(name = "word_one_id")
    public long getWordOneId() {
        return wordOneId;
    }

    public void setWordOneId(long wordOneId) {
        this.wordOneId = wordOneId;
    }

    @Id
    @Column(name = "word_two_id")
    public long getWordTwoId() {
        return wordTwoId;
    }

    public void setWordTwoId(long wordTwoId) {
        this.wordTwoId = wordTwoId;
    }

    @Column(name = "item_order")
    public int getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }

    @Column(name = "word_one")
    public String getWordOne() {
        return wordOne;
    }

    public void setWordOne(String wordOne) {
        this.wordOne = wordOne;
    }

    @Column(name = "word_two")
    public String getWordTwo() {
        return wordTwo;
    }

    public void setWordTwo(String wordTwo) {
        this.wordTwo = wordTwo;
    }
}
