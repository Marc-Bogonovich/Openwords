package com.openwords.actions.audios;

import com.openwords.database.DatabaseHandler;
import com.openwords.database.SystemSetting;
import com.openwords.database.WordAudio;
import com.openwords.database.WordAudioId;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.Session;

@ParentPackage("json-default")
public class UploadAudioFile extends MyAction {

    private static final long serialVersionUID = 1L;
    private File file;
    private int type, language;
    private String delimiter;

    @Action(value = "/uploadAudio")
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/uploadAudio");
        if (file == null) {
            sendBadRequest("Need actual file");
            return null;
        }
        if (delimiter == null) {
            delimiter = " ";
        }
        Session s = DatabaseHandler.getSession();
        if (!isPermitted(s)) {
            sendError("Not permitted!");
            return null;
        }
        try {
            parseFile(s);
        } catch (Exception e) {
            sendBadRequest(e.toString());
        }
        return null;
    }

    private boolean isPermitted(Session s) {
        List<SystemSetting> settings = SystemSetting.loadAll(s);
        for (SystemSetting setting : settings) {
            if (setting.getId() == 6 && setting.getValue() > 0) {
                UtilLog.logInfo(this, MyGson.toJson(setting));
                return true;
            }
        }
        return false;
    }

    private void parseFile(Session s) {
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] tokens = line.trim().split(delimiter);

                long wordId = Long.parseLong(tokens[0]);
                String soundName = tokens[2];

                WordAudio audio = new WordAudio(new WordAudioId(wordId, type), language, soundName);
                s.save(audio);
                s.beginTransaction().commit();
                UtilLog.logInfo(this, wordId + " " + tokens[1]);
            }
            UtilLog.logInfo(this, "audio done");
        } catch (Exception e) {
            UtilLog.logError(this, e);
            sendError(e.toString());
        } finally {
            DatabaseHandler.closeSession(s);
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }

}
