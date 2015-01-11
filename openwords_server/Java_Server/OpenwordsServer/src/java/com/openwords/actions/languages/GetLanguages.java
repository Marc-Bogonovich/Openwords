package com.openwords.actions.languages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Language;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyFieldValidation;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetLanguages extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Language> result;
    private String errorMessage;
    private String exclude;
    private String include;

    @Action(value = "/getLanguages", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getLanguages: exclude->" + exclude + " include->" + include);
        Session s = DatabaseHandler.getSession();
        try {
            if (include == null) {
                List<Integer> old = new Gson().fromJson(exclude, new TypeToken<List<Integer>>() {
                }.getType());
                List<Integer> langIds = Language.getNewLanguageIds(s, old);
                if (langIds.isEmpty()) {
                    return SUCCESS;
                }
                result = Language.getLanguages(s, langIds);
            } else {
                List<Integer> needed = new Gson().fromJson(include, new TypeToken<List<Integer>>() {
                }.getType());
                if (needed.isEmpty()) {
                    return SUCCESS;
                }
                result = Language.getLanguages(s, needed);
            }

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

    public void setInclude(String include) {
        this.include = include;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Language> getResult() {
        return result;
    }

    @Override
    public void validate() {
        try {
            if (exclude == null) {
                if (include.isEmpty()) {
                    throw new Exception("include is empty");
                }
            } else if (include == null) {
                if (exclude.isEmpty()) {
                    throw new Exception("exclude is empty");
                }
            } else {
                throw new Exception("both include and exclude are provided");
            }
        } catch (Exception e) {
            setErrorMessage("please either provide excluded ids or included ids, not both");
            UtilLog.logWarn(MyFieldValidation.class, e.toString());
            addFieldError("error", "error");
        }
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
