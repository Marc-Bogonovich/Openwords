package com.openwords.actions.words;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyFieldValidation;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class LookupWordForSameMeaning extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Word> result;
    private String errorMessage;
    private int langOut;
    private String wordsIn;

    @Action(value = "/lookupWordSameMeaning", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/lookupWordSameMeaning: " + wordsIn + " " + langOut);
        Session s = DatabaseHandler.getSession();
        try {
            Long[] ids = MyGson.fromJson(wordsIn, Long[].class);
            result = Word.getWordsBySameMD5(s, ids, langOut);
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setWordsIn(String wordsIn) {
        this.wordsIn = wordsIn;
    }

    public List<Word> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setLangOut(int langOut) {
        this.langOut = langOut;
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
