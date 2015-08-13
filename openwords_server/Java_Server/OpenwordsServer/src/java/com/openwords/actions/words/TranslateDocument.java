package com.openwords.actions.words;

import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyContextListener;
import com.openwords.utils.MyFieldValidation;
import com.openwords.utils.UtilLog;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.Session;

@ParentPackage("json-default")
public class TranslateDocument extends MyAction {

    private static final long serialVersionUID = 1L;
    public static AtomicBoolean inUse = new AtomicBoolean(false);
    private String langIn, langOut, place;
    private File file;

    @Action(value = "/translateDocument")
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/translateDocument: from " + langIn + " to " + langOut + " with " + place);
        if (inUse.get()) {
            sendError("Sorry, someone else is using this service, please try again later");
            return null;
        }
        if (file != null) {
            inUse.set(true);
            Session s = DatabaseHandler.getSession();
            parseFile(s);
        }
        return null;
    }

    private void parseFile(Session s) {
        try {
            String contextPath = MyContextListener.getContextPath(false);
            String newFileName = "file_out/" + System.currentTimeMillis() + "_" + langIn + "_" + langOut + ".txt";
            String fileOutPath = contextPath + newFileName;
            File fileOut = new File(fileOutPath);
            UtilLog.logInfo(this, "new file: " + fileOut.getAbsolutePath());

            try (FileWriter fw = new FileWriter(fileOut)) {
                Scanner scan = new Scanner(file);
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    String[] words = line.split(" ");
                    for (String word : words) {
                        String niceWord = word.replace(",", "").replace(".", "");
                        inputTrans(s, niceWord, fw);
                    }
                    fw.append("\n");
                }
            }
            getHttpResponse().getWriter().println(newFileName);
        } catch (Exception e) {
            UtilLog.logError(this, e);
            sendError(e.toString());
            inUse.set(false);
        } finally {
            DatabaseHandler.closeSession(s);
            inUse.set(false);
        }
    }

    private void inputTrans(Session s, String word, FileWriter fw) throws Exception {
        List<Word> trans;
        try {
            trans = Word.getConnectionsByEnglish(s, word, langIn, langOut);
        } catch (Exception e) {
            noTrans(fw, word);
            UtilLog.logError(this, e);
            return;
        }
        if (trans == null) {
            noTrans(fw, word);
            return;
        }
        if (trans.isEmpty()) {
            noTrans(fw, word);
            return;
        }
        fw.append(trans.get(0).getWord());
        fw.append(" ");
    }

    private void noTrans(FileWriter fw, String word) throws Exception {
        if (place.contains("undefined") || place == null || place.isEmpty()) {
            fw.append("{" + word + "} ");
        } else {
            fw.append("{" + place + "} ");
        }
    }

    public void setFile(File image) {
        this.file = image;
    }

    public void setLangIn(String langIn) {
        this.langIn = langIn;
    }

    public void setLangOut(String langOut) {
        this.langOut = langOut;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public void validate() {
        MyFieldValidation.checkBlackList(this, getHttpRequest());
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
