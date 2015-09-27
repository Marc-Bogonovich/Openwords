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
public class GetWordsInLanguageByForm extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Word> result;
    private final int pageNumber = 1;
    private int pageSize = 20, lang;
    private String errorMessage, form;

    @Action(value = "/getWords", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getWords: " + lang + " " + pageNumber + " " + pageSize);
        Session s = DatabaseHandler.getSession();
        try {
            result = Word.getWordsInLanguageByForm(s, lang, form, pageNumber, pageSize + 1);
            if (result.size() > pageSize) {
                errorMessage = "too many results";
                result = null;
            }
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    @Override
    public void validate() {
        MyFieldValidation.checkPageSize(this, pageSize, 10, 50);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public List<Word> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
