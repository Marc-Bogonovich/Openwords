package com.openwords.actions.sentence;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Sentence;
import com.openwords.database.SentenceConnection;
import com.openwords.database.SentenceItem;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetSentencePack extends MyAction {

    private static final long serialVersionUID = 1L;
    private static final Random ran = new Random();
    private String errorMessage;
    private int langOne, langTwo, pageSize = 10;
    private List<Sentence> sentences;
    private List<SentenceItem> items;
    private List<SentenceConnection> connections;

    @Action(value = "/getSentencePack", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getSentencePack: " + langOne + " " + langTwo);
        Session s = DatabaseHandler.getSession();
        try {
            int total = SentenceConnection.count(s, langOne, langTwo);
            if (total <= 0) {
                throw new Exception("we do not have sentence data for these languages yet");
            }
            loadPack(s, total);

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    private void loadPack(Session s, int total) {
        int page = ran.nextInt((total + pageSize) / pageSize);
        UtilLog.logInfo(this, "page: " + page);
        connections = SentenceConnection.getConnectionPage(s, page, pageSize, langOne, langTwo);
        Set<Long> ids = new HashSet<>(connections.size());
        for (SentenceConnection c : connections) {
            ids.add(c.getUniId());
            ids.add(c.getSentenceId());
        }
        sentences = Sentence.getSentences(s, ids);
        items = SentenceItem.getItems(s, ids);
    }

    public void setLangTwo(int langTwo) {
        this.langTwo = langTwo;
    }

    public void setLangOne(int langOne) {
        this.langOne = langOne;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public List<SentenceItem> getItems() {
        return items;
    }

    public List<SentenceConnection> getConnections() {
        return connections;
    }

}
