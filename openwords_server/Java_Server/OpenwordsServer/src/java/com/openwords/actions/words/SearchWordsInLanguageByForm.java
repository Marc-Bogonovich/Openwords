package com.openwords.actions.words;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Word;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyFieldValidation;
import com.openwords.utils.UtilLog;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class SearchWordsInLanguageByForm extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Word> targetResult;
    private List<Word> searchResult;
    private final int pageNumber = 1;
    private int pageSize, targetLang, searchLang;
    private String errorMessage, form;
    private Map<Long, Set<Long>> linkedTargetWords;
    private Map<Long, Set<Long>> linkedSearchWords;

    @Action(value = "/searchWords", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/searchWords: " + searchLang + " " + form + " " + targetLang);
        Session s = DatabaseHandler.getSession();
        try {
            List<Long> matchingForms = Word.getWordIdsInLanguageBySameForm(s, searchLang, form, pageNumber, pageSize + 1);
            if (matchingForms.size() > pageSize) {
                errorMessage = "too many results";
            }
            if (matchingForms.isEmpty()) {
                errorMessage = "no exact results";
                matchingForms = Word.getWordIdsInLanguageByForm(s, searchLang, form, pageNumber, pageSize + 1);
                if (matchingForms.size() > pageSize) {
                    errorMessage += " and " + "too many results";
                } else if (matchingForms.isEmpty()) {
                    throw new Exception("no results: matchingForms isEmpty");
                }
            }
            searchResult = Word.getWords(s, matchingForms);

            linkedTargetWords = new HashMap<>(matchingForms.size());
            linkedSearchWords = new HashMap<>(matchingForms.size());
            Word.linkWordsByUniWord(s, targetLang, searchLang, matchingForms, linkedTargetWords, linkedSearchWords);

            Set<Long> targetIds = new HashSet<>(linkedTargetWords.size());
            for (Set<Long> ids : linkedTargetWords.values()) {
                for (Long id : ids) {
                    targetIds.add(id);
                }
            }
            targetResult = Word.getWords(s, targetIds);
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    @Override
    public void validate() {
        MyFieldValidation.checkPageSize(this, pageSize, 50, 10);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTargetLang(int targetLang) {
        this.targetLang = targetLang;
    }

    public void setSearchLang(int searchLang) {
        this.searchLang = searchLang;
    }

    public void setForm(String form) {
        this.form = form.trim();
    }

    public List<Word> getTargetResult() {
        return targetResult;
    }

    public List<Word> getSearchResult() {
        return searchResult;
    }

    public Map<Long, Set<Long>> getLinkedTargetWords() {
        return linkedTargetWords;
    }

    public Map<Long, Set<Long>> getLinkedSearchWords() {
        return linkedSearchWords;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
