package com.openwords.actions.connections;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.database.WordConnection;
import com.openwords.interfaces.InterfaceGetWordConnectionsPack;
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
public class GetWordConnectionsPack extends MyAction implements InterfaceGetWordConnectionsPack {

    private static final long serialVersionUID = 1L;
    private int langOneId, langTwoId, pageNumber, pageSize;
    private String errorMessage;
    private boolean doOrder;
    private String orderBy;
    private List<WordConnection> connections;
    private List<Word> words;

    @Action(value = servicePath, results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, servicePath);
        Session s = DatabaseHandler.getSession();
        try {
            if (doOrder) {
                connections = WordConnection.getConnectionsPageWithOrder(s, langOneId, langTwoId, pageNumber, pageSize, orderBy.trim(), false);
            } else {
                connections = WordConnection.getConnectionsPage(s, langOneId, langTwoId, pageNumber, pageSize, false);
            }
            if (connections.isEmpty()) {
                return SUCCESS;
            }
            Set<Integer> wordIds = new HashSet<>(connections.size());
            for (WordConnection connection : connections) {
                wordIds.add(connection.getWordOneId());
                wordIds.add(connection.getWordTwoId());
            }
            words = Word.getWords(s, wordIds);

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    @Override
    public void setLangOneId(int langOneId) {
        this.langOneId = langOneId;
    }

    @Override
    public void setLangTwoId(int langTwoId) {
        this.langTwoId = langTwoId;
    }

    @Override
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setDoOrder(boolean doOrder) {
        this.doOrder = doOrder;
    }

    @Override
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public void validate() {
        MyFieldValidation.checkPageSize(this, pageSize, 100, 10);
        MyFieldValidation.checkBooleanAndString(this, "doOrder", doOrder, orderBy);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Object getResultConnections() {
        return connections;
    }

    @Override
    public Object getResultWords() {
        return words;
    }

}
