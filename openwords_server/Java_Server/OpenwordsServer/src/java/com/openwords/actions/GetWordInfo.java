package com.openwords.actions;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetWordInfo extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Word> result;
    private String wordForm, errorMessage;
    private int pageNumber, pageSize;

    @Action(value = "/getWordInfo", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        pageNumber = 1;
        pageSize = 10;
        UtilLog.logInfo(this, "/getWordInfo: " + wordForm);
        Session s = DatabaseHandler.getSession();
        try {
            result = Word.getSimilarWords(s, wordForm, pageNumber, pageSize);
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

}
