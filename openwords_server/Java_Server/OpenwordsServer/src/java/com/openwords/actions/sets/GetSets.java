package com.openwords.actions.sets;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.SetInfo;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyFieldValidation;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetSets extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private int pageNumber, pageSize, langOne, langTwo;
    private long searchUser;
    private String searchName;
    private List<SetInfo> result;

    @Action(value = "/getSets", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getSets: " + pageNumber + " " + pageSize + " " + langOne + " " + langTwo);
        Session s = DatabaseHandler.getSession();
        try {
            result = SetInfo.getAllSets(s, pageNumber, pageSize, langOne, langTwo, searchUser, searchName);
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
        MyFieldValidation.checkPageSize(this, pageSize, 100, 20);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<SetInfo> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setLangOne(int langOne) {
        this.langOne = langOne;
    }

    public void setLangTwo(int langTwo) {
        this.langTwo = langTwo;
    }

    public void setSearchUser(long searchUser) {
        this.searchUser = searchUser;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}
