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
public class SimpleTranslate extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Word> result;
    private String word, errorMessage, langIn, langOut;

    @Action(value = "/simpleTranslate", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/simpleTranslate: " + word + ", from " + langIn + " to " + langOut);
        Session s = DatabaseHandler.getSession();
        try {
            result = Word.getConnectionsByEnglish(s, word, langIn, langOut);
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setLangIn(String langIn) {
        this.langIn = langIn;
    }

    public void setLangOut(String langOut) {
        this.langOut = langOut;
    }

    public List<Word> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
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
