package com.openwords.actions.languages;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Language;
import com.openwords.database.Word;
import com.openwords.database.WordAudio;
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
    private static long lastRequest;
    private static List<String[]> result;
    private String errorMessage;
    private static int totalWords, totalSounds;

    @Action(value = "/overviewOpenwords", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/overviewOpenwords");
        long now = System.currentTimeMillis();
        long diff = (now - lastRequest) / 1000;
        if (diff < (60 * 60 * 24) && result != null) {//one day
            UtilLog.logInfo(this, "reuse because " + diff + " seconds");
            return SUCCESS;
        }

        Session s = DatabaseHandler.getSession();
        try {
            List<Language> langs = Language.getAllLanguages(s);
            result = new LinkedList<>();
            totalWords = Word.countLanguageWord(s, -1);
            totalSounds = WordAudio.countAll(s);
            for (Language lang : langs) {
                result.add(new String[]{lang.getName(), String.valueOf(Word.countLanguageWord(s, lang.getLangId())), lang.getCode(), String.valueOf(lang.getLangId())});
            }
            lastRequest = now;
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

    public int getTotalWords() {
        return totalWords;
    }

    public int getTotalSounds() {
        return totalSounds;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
