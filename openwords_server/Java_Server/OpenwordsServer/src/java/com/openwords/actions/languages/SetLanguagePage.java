package com.openwords.actions.languages;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.UserLanguage;
import com.openwords.database.UserLanguageId;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class SetLanguagePage extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private boolean result;
    private int userId, langOneId, langTwoId, nextPage;

    @Action(value = "/setLanguagesPage", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/setLanguagesPage: " + userId + " " + langOneId + " " + langTwoId + " " + nextPage);
        Session s = DatabaseHandler.getSession();
        try {
            UserLanguage.advanceLanguagePage(s, new UserLanguageId(userId, langOneId, langTwoId), nextPage);
            result = true;
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLangOneId(int langOneId) {
        this.langOneId = langOneId;
    }

    public void setLangTwoId(int langTwoId) {
        this.langTwoId = langTwoId;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isResult() {
        return result;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
