package com.openwords.actions;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.database.WordConnection;
import com.openwords.database.WordMetaInfo;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyMessageDigest;
import com.openwords.utils.UtilLog;
import java.util.Date;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class AddWordConnection extends MyAction {

    private static final long serialVersionUID = 1L;

    private String wordOne, wordTwo, translation, wordOneOriginalForm, wordTwoOriginalForm, contributor;
    private int wordOneLang, wordTwoLang;
    private String result, accessToken, errorMessage;

    @Action(value = "/addWordConnection", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/addWordConnection");
        Session s = DatabaseHandler.getSession();
        try {
            if (!accessToken.equals("wordsopen2014")) {
                throw new Exception("AccessToken is not valid!");
            }

            List<Word> checkWord1 = Word.checkSameWord(s, translation, wordOneLang, wordOne);
            List<Word> checkWord2 = Word.checkSameWord(s, translation, wordTwoLang, wordTwo);

            int wordOneId = -1, wordTwoId = -1;
            if (!checkWord1.isEmpty()) {
                wordOneId = checkWord1.get(0).getWordId();
            }
            if (!checkWord2.isEmpty()) {
                wordTwoId = checkWord2.get(0).getWordId();
            }

            if (wordOneId < 0) {
                Word w = new Word(wordOneLang, wordOne, new WordMetaInfo(translation, wordOneOriginalForm).getXmlString(), contributor, MyMessageDigest.digest(translation.getBytes()));
                s.save(w);
                wordOneId = w.getWordId();
            }
            if (wordTwoId < 0) {
                Word w = new Word(wordTwoLang, wordTwo, new WordMetaInfo(translation, wordTwoOriginalForm).getXmlString(), contributor, MyMessageDigest.digest(translation.getBytes()));
                s.save(w);
                wordTwoId = w.getWordId();
            }

            List<WordConnection> checkConnection = WordConnection.getConnectionByIds(s, wordOneId, wordTwoId);
            if (checkConnection.isEmpty()) {
                WordConnection connnection = new WordConnection(wordOneId, wordOneLang, wordTwoId, wordTwoLang, 1, contributor);
                WordConnection.addConnection(s, connnection);

                s.beginTransaction().commit();

                result = "New connection added: " + connnection.getConnectionId() + " at " + new Date();
                UtilLog.logInfo(this, result);
            } else {
                errorMessage = "Connection already exists!";
                UtilLog.logInfo(this, errorMessage);
            }

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage + "\nwordOne: " + wordOne + " wordTwo: " + wordTwo);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setWordOne(String wordOne) {
        wordOneOriginalForm = wordOne;
        this.wordOne = wordOne.trim().toLowerCase();
    }

    public void setWordTwo(String wordTwo) {
        wordTwoOriginalForm = wordTwo;
        this.wordTwo = wordTwo.trim().toLowerCase();
    }

    public void setTranslation(String translation) {
        this.translation = translation.trim().toLowerCase();
    }

    public void setWordOneLang(int wordOneLang) {
        this.wordOneLang = wordOneLang;
    }

    public void setWordTwoLang(int wordTwoLang) {
        this.wordTwoLang = wordTwoLang;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getResult() {
        return result;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
