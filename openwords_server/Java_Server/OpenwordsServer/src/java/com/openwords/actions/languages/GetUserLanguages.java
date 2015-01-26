package com.openwords.actions.languages;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.UserLanguage;
import com.openwords.interfaces.HttpResultHandler;
import com.openwords.interfaces.InterfaceGetUserLanguages;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetUserLanguages extends MyAction implements InterfaceGetUserLanguages {

    private static final long serialVersionUID = 1L;
    private List<Integer> result;
    private String errorMessage;
    private int userId, langOneId;

    @Action(value = servicePath, results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, servicePath + ": " + userId + " " + langOneId);
        Session s = DatabaseHandler.getSession();
        try {
            result = UserLanguage.getUserLearningLanguages(s, userId, langOneId);

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void setLangOneId(int langOneId) {
        this.langOneId = langOneId;
    }

    @Override
    public List<Integer> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }

    @Override
    public void doRequest(int i, int i1, HttpResultHandler hrh) {
    }
}
