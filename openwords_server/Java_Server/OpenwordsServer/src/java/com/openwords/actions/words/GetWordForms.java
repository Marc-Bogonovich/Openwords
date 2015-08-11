package com.openwords.actions.words;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.database.WordForTTS;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetWordForms extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<WordForTTS> result;
    private int pageNumber = 1, pageSize = 10, languageId;

    @Action(value = "/getWordForms", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getWordForms: " + languageId + " " + pageNumber + " " + pageSize);
        Session s = DatabaseHandler.getSession();
        try {
            result = Word.getOnlyWordForms(s, languageId, pageNumber, pageSize);
            System.out.println(MyGson.toPrettyJson(result));
        } catch (Exception e) {
            UtilLog.logWarn(this, e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public List<WordForTTS> getResult() {
        return result;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
