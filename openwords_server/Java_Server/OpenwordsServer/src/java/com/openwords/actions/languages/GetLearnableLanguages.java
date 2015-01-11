package com.openwords.actions.languages;

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
public class GetLearnableLanguages extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Integer> result;
    private String errorMessage;
    private int langOneId;

    @Action(value = "/getLearnableLanguages", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getLearnableLanguages: " + langOneId);
        Session s = DatabaseHandler.getSession();
        try {
            result = WordConnection.getLearnableLanguageIds(s, langOneId);
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Integer> getResult() {
        return result;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
