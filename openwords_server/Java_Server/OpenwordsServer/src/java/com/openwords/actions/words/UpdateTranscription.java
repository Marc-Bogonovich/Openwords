package com.openwords.actions.words;

import com.openwords.database.DatabaseHandler;
import com.openwords.database.SystemSetting;
import com.openwords.database.Word;
import com.openwords.database.WordMetaInfo;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyFieldValidation;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.struts2.convention.annotation.Action;
import org.hibernate.Session;

public class UpdateTranscription extends MyAction {

    private static final long serialVersionUID = 1L;
    public static AtomicBoolean inUse = new AtomicBoolean(false);
    private File file;

    @Action(value = "/updateTranscription")
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/updateTranscription: " + file.getName());
        if (inUse.get()) {
            sendError("Sorry, someone else is using this service, please try again later");
            return null;
        }
        if (file != null) {
            Session s = DatabaseHandler.getSession();
            if (!isPermitted(s)) {
                sendError("Not permitted!");
                return null;
            }
            inUse.set(true);
            parseFile(s);
        }
        return null;
    }

    private boolean isPermitted(Session s) {
        List<SystemSetting> settings = SystemSetting.loadAll(s);
        for (SystemSetting setting : settings) {
            if (setting.getId() == 5 && setting.getValue() > 0) {
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
                String line1 = scan.nextLine();
                if (!line1.startsWith(" ")) {
                    UtilLog.logInfo(this, "xxxxx " + line1);
                    continue;
                }
                String line2 = scan.nextLine();
                String[] words = line2.split(" ");
                long wordId = Long.parseLong(words[0]);
                String word = words[1];
                String tran = line1.trim();

                Word w = (Word) s.get(Word.class, wordId);
                if (w != null) {
                    WordMetaInfo meta = w.getWordMetaInfo();
                    meta.setNativeTranscription(tran);
                    w.setMeta(meta.getXmlString());
                    UtilLog.logInfo(this, wordId + " " + word + " " + tran);
                }
            }
            s.beginTransaction().commit();
        } catch (Exception e) {
            UtilLog.logError(this, e);
            sendError(e.toString());
            inUse.set(false);
        } finally {
            DatabaseHandler.closeSession(s);
            inUse.set(false);
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void validate() {
        MyFieldValidation.checkBlackList(this, getHttpRequest());
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
