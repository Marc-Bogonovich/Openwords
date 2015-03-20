package com.openwords.actions.connections;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.WordConnection;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

@ParentPackage("json-default")
public class GetConnectionSheet extends MyAction {

    private static final long serialVersionUID = 1L;
    public static AtomicBoolean inUse = new AtomicBoolean(false);
    private int langTwoId;
    private String pass;
    private InputStream inputStream;
    private String fileName;
    private String error;

    @Action(value = "/getConnectionSheet",
            results = {
                @Result(name = SUCCESS, type = "stream", params = {
                    "contentType", "application/octet-stream",
                    "inputName", "inputStream",
                    "bufferSize", "4096",
                    "contentDisposition", "attachment;filename=\"${fileName}\""}),
                @Result(name = INPUT, type = "json")
            })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getConnectionSheet: " + langTwoId + " " + pass);
        if (inUse.get()) {
            error = "Sorry, the server is busy with other downloadings, please try again later";
            return INPUT;
        }

        if (!pass.equals("openwordsfree")) {
            error = "Password is not correct";
            return INPUT;
        }
        inUse.set(true);
        Session s = DatabaseHandler.getSession();
        try {
            makeDoc(s);
        } catch (Exception e) {
            UtilLog.logWarn(this, e);
            error = e.toString();
            inUse.set(false);
            return INPUT;
        } finally {
            DatabaseHandler.closeSession(s);
        }
        inUse.set(false);
        return SUCCESS;
    }

    private void makeDoc(Session s) throws Exception {
        List<Map<String, Object>> r = WordConnection.getWordConnectionPack(s, langTwoId);
        if (r.isEmpty()) {
            throw new Exception("No data");
        }
        fileName = "language_" + langTwoId + ".ods";
        File file = new File("temp.ods");
        SpreadSheet doc = SpreadSheet.create(1, 8, r.size() + 1);
        Sheet sheet = doc.getSheet(0);
        int index = 1;
        for (Map<String, Object> row : r) {
            sheet.getCellAt(0, index).setValue(row.get("connection_id"));
            sheet.getCellAt(1, index).setValue(row.get("word1_language"));
            sheet.getCellAt(2, index).setValue(row.get("word"));
            sheet.getCellAt(3, index).setValue(row.get("common_translation"));
            sheet.getCellAt(4, index).setValue(row.get("word2_language"));
            sheet.getCellAt(5, index).setValue(row.get("word2"));
            sheet.getCellAt(6, index).setValue(row.get("connection_time").toString());
            index += 1;
        }
        doc.saveAs(file);
        UtilLog.logInfo(this, file.getAbsolutePath());
        inputStream = new FileInputStream(file);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setLangTwoId(int langTwoId) {
        this.langTwoId = langTwoId;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getError() {
        return error;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }

}
