package com.openwords.actions.sets;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.SetInfo;
import com.openwords.database.SetItem;
import com.openwords.database.UserPerformance;
import com.openwords.database.Word;
import com.openwords.database.WordConnection;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetSetItems extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private long setId, userId, learnerId;
    private List<SetItem> itemsResult;
    private boolean getPack;
    private List<WordConnection> connections;
    private List<Word> words;
    private List<UserPerformance> performance;

    @Action(value = "/getSetItems", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getSetItems: " + setId);
        Session s = DatabaseHandler.getSession();
        try {
            SetInfo info = SetInfo.getSetInfo(s, setId);
            if (info.getUserId() != userId) {
                throw new Exception("set information is not correct");
            }

            itemsResult = SetItem.getSetItems(s, setId);
            if (getPack) {
                loadPack(s);
            }

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    private void loadPack(Session s) {
        Set<Long> connectionIds = new HashSet<>(itemsResult.size());
        for (SetItem item : itemsResult) {
            connectionIds.add(item.getWordConnectionId());
        }
        connections = WordConnection.getConnections(s, connectionIds);

        Set<Long> wordIds = new HashSet<>(connections.size());
        for (WordConnection connection : connections) {
            wordIds.add(connection.getWordOneId());
            wordIds.add(connection.getWordTwoId());
        }
        words = Word.getWords(s, wordIds);

        performance = UserPerformance.getPerformances(s, learnerId, connectionIds);
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public List<WordConnection> getConnections() {
        return connections;
    }

    public List<Word> getWords() {
        return words;
    }

    public List<UserPerformance> getPerformance() {
        return performance;
    }

    public void setGetPack(boolean getPack) {
        this.getPack = getPack;
    }

    public void setLearnerId(long learnerId) {
        this.learnerId = learnerId;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
