package com.openwords.actions.words;

import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.database.WordForTTS;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletOutputStream;
import org.apache.struts2.convention.annotation.Action;
import org.hibernate.Session;

public class GetWordFormsInTextFile extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<WordForTTS> result;
    private int pageNumber = 1, pageSize = 10, languageId;

    @Action(value = "/getWordFormsText")
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getWordFormsText: " + languageId + " " + pageNumber + " " + pageSize);
        Session s = DatabaseHandler.getSession();
        try {
            ServletOutputStream stream = getHttpResponse().getOutputStream();
            result = Word.getOnlyWordForms(s, languageId, pageNumber, pageSize);
            for (WordForTTS word : result) {
                String line = word.getWordId() + " " + word.getWord() + "\n";
                stream.write(line.getBytes());
            }
        } catch (Exception e) {
            UtilLog.logWarn(this, e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return null;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
