package com.openwords.actions.languages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Language;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetNewLanguages extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Language> result;
    private String errorMessage;
    private String exclude;

    @Action(value = "/getNewLanguages", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getNewLanguages");
        Session s = DatabaseHandler.getSession();
        try {
            List<Integer> old = new Gson().fromJson(exclude, new TypeToken<List<Integer>>() {
            }.getType());
            List<Integer> langIds = Language.getNewLanguageIds(s, old);
            if (langIds.isEmpty()) {
                return SUCCESS;
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

    public void setExclude(String exclude) {
        this.exclude = exclude;
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
