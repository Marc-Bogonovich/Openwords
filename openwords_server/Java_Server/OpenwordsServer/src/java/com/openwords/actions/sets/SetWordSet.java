package com.openwords.actions.sets;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.SetInfo;
import com.openwords.database.SetMetaInfo;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class SetWordSet extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private SetInfo setResult;
    private long userId, setId;
    private int nlang, llang;
    private String name;

    @Action(value = "/setWordSet", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/setWordSet: " + name);
        Session s = DatabaseHandler.getSession();
        try {
            if (name.isEmpty()) {
                throw new Exception("set name cannot be empty");
            }
            SetInfo set = new SetInfo(setId, userId, nlang, llang, 1, name, true, new SetMetaInfo("test").getXmlString());
            setResult = SetInfo.updateSet(s, set);
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setNlang(int nlang) {
        this.nlang = nlang;
    }

    public void setLlang(int llang) {
        this.llang = llang;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public SetInfo getSetResult() {
        return setResult;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
