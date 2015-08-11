package com.openwords.actions.audios;

import com.openwords.database.DatabaseHandler;
import com.openwords.database.WordAudio;
import com.openwords.database.WordAudioId;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.io.File;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.Session;

@ParentPackage("json-default")
public class UploadAudioFile extends MyAction {

    private static final long serialVersionUID = 1L;
    private File file;
    private String filePostfix;
    private int wordId, type, language;

    @Action(value = "/uploadAudio")
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/uploadAudio");
        if (file == null) {
            sendBadRequest("Need actual file");
            return null;
        }
        if (filePostfix == null) {
            sendBadRequest("Need file postfix");
            return null;
        }
        Session s = DatabaseHandler.getSession();
        try {
            WordAudio.saveAudioFile(s, new WordAudio(new WordAudioId(wordId, type), language, null),
                    file, filePostfix);
        } catch (Exception e) {
            sendBadRequest(e.toString());
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return null;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setFilePostfix(String filePostfix) {
        this.filePostfix = filePostfix;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }

}
