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
    private String result;

    @Action(value = "/addWordConnection", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        Session s = DatabaseHandler.getSession();
        try {
            List<Word> checkWord = Word.getWordsWithSameCommonTranslation(s, translation);
            int wordOneId = -1, wordTwoId = -1;
            for (Word word : checkWord) {
                if (word.getWord().equals(wordOne)) {
                    wordOneId = word.getWordId();
                } else if (word.getWord().equals(wordTwo)) {
                    wordTwoId = word.getWordId();
                }
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
                result = "Connection already exists!";
            }

        } catch (Exception e) {
            result = e.toString();
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

}
