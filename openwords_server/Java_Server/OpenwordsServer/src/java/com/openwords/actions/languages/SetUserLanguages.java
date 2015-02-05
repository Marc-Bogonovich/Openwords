package com.openwords.actions.languages;

import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.UserLanguage;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class SetUserLanguages extends MyAction {

    private static final long serialVersionUID = 1L;
    private String langTwoIds;
    private String errorMessage;
    private boolean result;
    private int userId, langOneId;

    @Action(value = "/setUserLanguages", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/setUserLanguages: " + userId + " " + langOneId + " " + langTwoIds);
        Session s = DatabaseHandler.getSession();
        try {
            int[] langTwos = new Gson().fromJson(langTwoIds, int[].class);
            if (langTwos.length == 0) {
                errorMessage = "langTwoIds is empty";
                return SUCCESS;
            }
            UserLanguage.setUserLearningLanguages(s, userId, langOneId, langTwos);
            result = true;

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setLangTwoIds(String langTwoIds) {
        this.langTwoIds = langTwoIds;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLangOneId(int langOneId) {
        this.langOneId = langOneId;
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
