package com.openwords.actions.audios;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.WordAudio;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetAudioInfo extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<WordAudio> result;
    private String errorMessage;
    private String wordIds;
    private int type, language;

    @Action(value = "/getWordAudioInfo", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getWordAudioInfo: " + wordIds);
        Session s = DatabaseHandler.getSession();
        try {
            Long[] ids = MyGson.fromJson(wordIds, Long[].class);
            result = WordAudio.getAudioByIds(s, ids, type, language);
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public List<WordAudio> getResult() {
        return result;
    }

    public void setWordIds(String wordIds) {
        this.wordIds = wordIds;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
