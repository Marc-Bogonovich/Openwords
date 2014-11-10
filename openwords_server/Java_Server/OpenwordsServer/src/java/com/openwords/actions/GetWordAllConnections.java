package com.openwords.actions;

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
public class GetWordAllConnections extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<WordConnection> result;
    private String wordOne, errorMessage;

    @Action(value = "/getWordAllConnections", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getWordAllConnections");
        Session s = DatabaseHandler.getSession();
        try {
            wordOne = wordOne.trim().toLowerCase();
            result = WordConnection.getWordAllConnections(s, wordOne, true);
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setWordOne(String wordOne) {
        this.wordOne = wordOne;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<WordConnection> getResult() {
        return result;
    }

}
