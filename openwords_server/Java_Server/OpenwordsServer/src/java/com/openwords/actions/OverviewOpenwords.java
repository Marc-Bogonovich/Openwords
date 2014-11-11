package com.openwords.actions;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Language;
import com.openwords.database.Word;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.LinkedList;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class OverviewOpenwords extends MyAction {

    private static final long serialVersionUID = 1L;

    private List<String[]> result;
    private String errorMessage;

    @Action(value = "/overviewOpenwords", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/overviewOpenwords");
        Session s = DatabaseHandler.getSession();
        try {
            List<Language> langs = Language.getAllLanguages(s);
            result = new LinkedList<>();
            result.add(new String[]{"ALL", String.valueOf(Word.countLanguageWord(s, -1))});
            for (Language lang : langs) {
                result.add(new String[]{lang.getName(), String.valueOf(Word.countLanguageWord(s, lang.getId())), lang.getCode(), String.valueOf(lang.getId())});
            }

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public List<String[]> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
