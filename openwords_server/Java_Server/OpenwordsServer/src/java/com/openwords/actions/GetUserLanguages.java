package com.openwords.actions;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Language;
import com.openwords.database.UserLanguage;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.LinkedList;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetUserLanguages extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Language> result;
    private String errorMessage;
    private int userId, langOneId;

    @Action(value = "/getUserLanguages", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getUserLanguages: " + userId + " " + langOneId);
        Session s = DatabaseHandler.getSession();
        try {
            List<UserLanguage> userLangs = UserLanguage.getUserLearningLanguages(s, userId, langOneId);
            if (userLangs.isEmpty()) {
                return SUCCESS;
            }

            List<Integer> langIds = new LinkedList<>();
            for (UserLanguage lang : userLangs) {
                langIds.add(lang.getLangTwo());
            }
            result = Language.getLanguages(s, langIds);
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Language> getResult() {
        return result;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
