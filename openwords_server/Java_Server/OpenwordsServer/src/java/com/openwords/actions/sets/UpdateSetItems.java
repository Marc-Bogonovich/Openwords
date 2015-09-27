package com.openwords.actions.sets;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.SetInfo;
import com.openwords.database.SetItem;
import com.openwords.database.Word;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class UpdateSetItems extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private String wordOneIds;
    private String wordTwoIds;
    private long setId, userId;
    private List<SetItem> itemsResult;

    @Action(value = "/updateSetItems", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/updateSetItems: " + wordOneIds + " " + wordTwoIds + " " + setId);
        Session s = DatabaseHandler.getSession();
        try {
            SetInfo info = SetInfo.getSetInfo(s, setId);
            if (info.getUserId() != userId) {
                throw new Exception("set information is not correct");
            }

            Long[] ids1 = MyGson.fromJson(wordOneIds, Long[].class);
            Long[] ids2 = MyGson.fromJson(wordTwoIds, Long[].class);
            if (ids1.length != ids2.length) {
                throw new Exception("input ids not match");
            }
            Map<Long, Word> words = new HashMap<>(ids1.length + ids2.length);
            List<Word> wordOnes = Word.getWords(s, ids1);
            List<Word> wordTwos = Word.getWords(s, ids2);
            for (Word w : wordOnes) {
                words.put(w.getWordId(), w);
            }
            for (Word w : wordTwos) {
                words.put(w.getWordId(), w);
            }

            itemsResult = new ArrayList<>(ids1.length);
            for (int i = 0; i < ids1.length; i++) {
                itemsResult.add(i, new SetItem(ids1[i], ids2[i], i + 1, words.get(ids1[i]).getWord(), words.get(ids2[i]).getWord()));
            }
            SetItem.updateSetItems(s, setId, itemsResult);
            info.setSetSize(itemsResult.size());
            s.beginTransaction().commit();

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setWordOneIds(String wordOneIds) {
        this.wordOneIds = wordOneIds;
    }

    public void setWordTwoIds(String wordTwoIds) {
        this.wordTwoIds = wordTwoIds;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<SetItem> getItemsResult() {
        return itemsResult;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
