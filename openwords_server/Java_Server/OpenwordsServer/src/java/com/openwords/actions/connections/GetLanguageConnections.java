package com.openwords.actions.connections;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.WordConnection;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetLanguageConnections extends MyAction {

    private static final long serialVersionUID = 1L;
    private int langOneId, langTwoId, pageNumber, pageSize;
    private List<WordConnection> result;
    private String errorMessage;
    private boolean doOrder;
    private String orderBy;

    @Action(value = "/getLanguageConnections", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getLanguageConnections");
        Session s = DatabaseHandler.getSession();
        try {
            if (pageSize > 100) {
                throw new Exception("PageSize is too large!");
            }
            if (doOrder) {
                if (orderBy == null || orderBy.isEmpty()) {
                    throw new Exception("Please give an order criteria!");
                }
            }
            if (doOrder) {
                result = WordConnection.getConnectionsPageWithOrder(s, langOneId, langTwoId, pageNumber, pageSize, orderBy.trim());
            } else {
                result = WordConnection.getConnectionsPage(s, langOneId, langTwoId, pageNumber, pageSize);
            }
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
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

    public List<WordConnection> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setDoOrder(boolean doOrder) {
        this.doOrder = doOrder;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}