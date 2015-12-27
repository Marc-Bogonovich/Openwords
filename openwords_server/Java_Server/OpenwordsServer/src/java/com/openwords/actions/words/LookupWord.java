package com.openwords.actions.words;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyFieldValidation;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class LookupWord extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Word> result;
    private String wordForm, errorMessage;
    private int pageSize, lang;
    private boolean definition;

    @Action(value = "/lookupWord", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/lookupWord: " + wordForm + " " + lang);
        Session s = DatabaseHandler.getSession();
        try {
            if (definition) {
                result = Word.searchDefinition(s, wordForm, lang, 1, pageSize + 1);
            } else {
                result = Word.getSameWords(s, wordForm, lang, 1, pageSize + 1);
            }
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setWordForm(String wordForm) {
        this.wordForm = wordForm;
    }

    public List<Word> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setDefinition(boolean definition) {
        this.definition = definition;
    }

    @Override
    public void validate() {
        MyFieldValidation.checkBlackList(this, getHttpRequest());
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
