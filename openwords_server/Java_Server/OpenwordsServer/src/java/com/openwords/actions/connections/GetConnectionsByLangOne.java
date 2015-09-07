package com.openwords.actions.connections;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.database.WordConnection;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyFieldValidation;
import com.openwords.utils.UtilLog;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetConnectionsByLangOne extends MyAction {

    private static final long serialVersionUID = 1L;
    private int langOneId, langTwoId, pageNumber, pageSize, total;
    private List<WordConnection> connections;
    private List<Word> words;
    private String errorMessage, form;

    @Action(value = "/getConnectionsByLangOne", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getConnectionsByLangOne: " + form + " " + langOneId + " " + langTwoId + ", page " + pageNumber);
        Session s = DatabaseHandler.getSession();
        try {
            int[] t = new int[1];
            connections = WordConnection.getSimilarWordsByLangOne(s, langOneId, langTwoId, form.toLowerCase(), pageNumber, pageSize, t);
            total = t[0];
            if (connections.isEmpty()) {
                throw new Exception("no records");
            }
            words = fillWords(s, connections);
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    private List<Word> fillWords(Session s, List<WordConnection> connections) {
        Set<Long> wordIds = new HashSet<>(connections.size());
        for (WordConnection connection : connections) {
            wordIds.add(connection.getWordOneId());
            wordIds.add(connection.getWordTwoId());
        }
        return Word.getWords(s, wordIds);
    }

    public void setLangOneId(int langOneId) {
        this.langOneId = langOneId;
    }

    public void setLangTwoId(int langTwoId) {
        this.langTwoId = langTwoId;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public List<WordConnection> getConnections() {
        return connections;
    }

    public List<Word> getWords() {
        return words;
    }

    public int getTotal() {
        return total;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void validate() {
        MyFieldValidation.checkPageSize(this, pageSize, 10, 5);
        MyFieldValidation.checkBooleanAndString(this, "form", true, form);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
