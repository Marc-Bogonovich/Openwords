package com.openwords.actions.sentence;

import com.openwords.database.DatabaseHandler;
import com.openwords.database.Sentence;
import com.openwords.database.SentenceItem;
import com.openwords.database.SystemSetting;
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
public class UploadSentenceFile extends MyAction {

    private static final long serialVersionUID = 1L;
    private File file;
    private int language;
    private boolean doParseBySpace;

    @Action(value = "/uploadSentence")
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/uploadSentence");
        if (file == null) {
            sendBadRequest("Need actual file");
            return null;
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
            if (setting.getId() == 7 && setting.getValue() > 0) {
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
                String[] tokens = line.trim().split("\t");

                long sentenceId = Long.parseLong(tokens[0]);
                String content = tokens[1];

                Sentence sen = new Sentence(sentenceId, language, content, "<sentence></sentence>");
                s.save(sen);
                UtilLog.logInfo(this, "sentence " + sen.getSentenceId());

                if (doParseBySpace) {
                    String[] items = content.split(" ");
                    for (int i = 0; i < items.length; i++) {
                        SentenceItem it = new SentenceItem(sentenceId, i + 1, items[i], "test");
                        s.save(it);
                    }
                } else {
                    char[] items = content.toCharArray();
                    for (int i = 0; i < items.length; i++) {
                        SentenceItem it = new SentenceItem(sentenceId, i + 1, String.valueOf(items[i]), "test");
                        s.save(it);
                    }
                }
            }
            s.beginTransaction().commit();
            UtilLog.logInfo(this, "sentence done");
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

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setDoParseBySpace(boolean doParseBySpace) {
        this.doParseBySpace = doParseBySpace;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }

}
