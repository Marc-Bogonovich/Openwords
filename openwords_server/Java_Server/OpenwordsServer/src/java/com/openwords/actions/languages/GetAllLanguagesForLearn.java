package com.openwords.actions.languages;

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
public class GetAllLanguagesForLearn extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Language> result;
    private String errorMessage;
    private int minWord;

    @Action(value = "/getAllLanguagesForLearn", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getAllLanguagesForLearn");
        Session s = DatabaseHandler.getSession();
        try {
            result = Language.getLearnableLanguages(s, minWord);
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Language> getResult() {
        return result;
    }

    public void setMinWord(int minWord) {
        this.minWord = minWord;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }

}
