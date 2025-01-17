package com.openwords.actions.connections;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.WordConnection;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyFieldValidation;
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
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getLanguageConnections");
        Session s = DatabaseHandler.getSession();
        try {
            if (doOrder) {
                result = WordConnection.getConnectionsPageWithOrder(s, langOneId, langTwoId, pageNumber, pageSize, orderBy.trim(), true);
            } else {
                result = WordConnection.getConnectionsPage(s, langOneId, langTwoId, pageNumber, pageSize, true);
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
    public void validate() {
        MyFieldValidation.checkPageSize(this, pageSize, 100, 10);
        MyFieldValidation.checkBooleanAndString(this, "doOrder", doOrder, orderBy);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
