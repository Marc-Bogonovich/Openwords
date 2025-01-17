package com.openwords.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SentenceItem extends SugarRecord<SentenceItem> {

    public static void refreshAll(List<SentenceItem> items) {
        Set<Long> ids = new HashSet<Long>(items.size());
        for (SentenceItem item : items) {
            ids.add(item.sentenceId);
        }
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        SentenceItem.deleteAll(SentenceItem.class, "sentence_id in " + sqlIds);
        SentenceItem.saveInTx(items);
    }

    public static List<SentenceItem> getItems(long sentenceId) {
        List<SentenceItem> r = Select.from(SentenceItem.class)
                .where(Condition.prop("sentence_id").eq(sentenceId))
                .orderBy("item_index asc")
                .list();
        return r;
    }

    public long sentenceId;
    public int itemIndex;
    public String item, type;

    public SentenceItem() {
    }

    public SentenceItem(long sentenceId, int itemIndex, String item, String type) {
        this.sentenceId = sentenceId;
        this.itemIndex = itemIndex;
        this.item = item;
        this.type = type;
    }

}
