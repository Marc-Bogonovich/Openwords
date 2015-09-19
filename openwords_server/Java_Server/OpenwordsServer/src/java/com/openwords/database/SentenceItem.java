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
@Table(name = "sentence_items")
public class SentenceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void updateItems(Session s, long sentenceId, Collection<SentenceItem> items) {
        String sql = "delete from sentence_items where sentence_id=@sentenceId@";
        sql = sql.replace("@sentenceId@", String.valueOf(sentenceId));
        s.createSQLQuery(sql).executeUpdate();

        for (SentenceItem item : items) {
            item.setSentenceId(sentenceId);
            s.save(item);
        }
        s.beginTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public static List<SentenceItem> getItems(Session s, long sentenceId) {
        return s.createCriteria(SentenceItem.class)
                .add(Restrictions.eq("sentenceId", sentenceId))
                .addOrder(Order.asc("itemIndex"))
                .list();
    }

    private long sentenceId;
    private int itemIndex;
    private String item, type;

    public SentenceItem() {
    }

    public SentenceItem(int itemIndex, String item, String type) {
        this.itemIndex = itemIndex;
        this.item = item;
        this.type = type;
    }

    @Id
    @Column(name = "sentence_id")
    public long getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(long sentenceId) {
        this.sentenceId = sentenceId;
    }

    @Id
    @Column(name = "item_index")
    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    @Column(name = "item")
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Column(name = "item_type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
