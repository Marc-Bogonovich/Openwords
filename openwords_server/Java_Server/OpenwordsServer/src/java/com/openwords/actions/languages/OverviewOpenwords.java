package com.openwords.actions.languages;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Language;
import com.openwords.database.Word;
import com.openwords.database.WordAudio;
import com.openwords.database.WordConnection;
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
    private static int totalWords, totalSounds;
    private String errorMessage;
    private boolean reset;

    @Action(value = "/overviewOpenwords", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/overviewOpenwords");
        if (!reset) {
            long now = System.currentTimeMillis();
            long diff = (now - lastRequest) / 1000;
            if (diff < (60 * 60 * 24) && result != null) {//one day
                UtilLog.logInfo(this, "reuse because " + diff + " seconds");
                return SUCCESS;
            }
        }
        Session s = DatabaseHandler.getSession();
        try {
            totalWords = 0;
            totalSounds = 0;
            result = new LinkedList<>();
            Language uniLang = (Language) s.get(Language.class, Word.Universal_Language);

            if (uniLang.getTotalConnections() > 0) {
                UtilLog.logInfo(this, "reuse last overall");
                //has last overall info
                List<Language> langs = Language.getAllLanguages(s);
                for (Language lang : langs) {
                    addTotal(lang);
                }
                lastRequest = System.currentTimeMillis();
            } else {
                UtilLog.logInfo(this, "re-compute");
                //need to re-compute
                List<Language> langs = Language.getAllLanguages(s);
                for (Language lang : langs) {
                    int tw = Word.countLanguageWord(s, lang.getLangId());
                    lang.setTotalWords(tw);
                    int ts = WordAudio.getAudioCount(s, 1, lang.getLangId());
                    lang.setTotalSounds(ts);
                    int tc = WordConnection.countWords(s, lang.getLangId());
                    lang.setTotalConnections(tc);
                    addTotal(lang);
                }
                s.beginTransaction().commit();
                lastRequest = 0;
            }

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    private void addTotal(Language lang) {
        totalWords += lang.getTotalWords();
        totalSounds += lang.getTotalSounds();
        result.add(new String[]{lang.getName(),
            String.valueOf(lang.getTotalWords()),
            lang.getCode(),
            String.valueOf(lang.getLangId()),
            String.valueOf(lang.getTotalSounds())});
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

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
