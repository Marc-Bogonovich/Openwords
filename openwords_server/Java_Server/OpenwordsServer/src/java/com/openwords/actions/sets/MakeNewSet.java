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
public class MakeNewSet extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private SetInfo setResult;
    private long userId;
    private int lang;
    private String name;

    @Action(value = "/makeNewSet", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/makeNewSet: " + name);
        Session s = DatabaseHandler.getSession();
        try {
            if (name.isEmpty()) {
                throw new Exception("set name cannot be empty");
            }
            SetInfo set = new SetInfo(userId, lang, 1, name, true, new SetMetaInfo("test").getXmlString());
            SetInfo.createNewSet(s, set);
            s.refresh(set);
            setResult = set;
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

    public void setLang(int lang) {
        this.lang = lang;
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

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}