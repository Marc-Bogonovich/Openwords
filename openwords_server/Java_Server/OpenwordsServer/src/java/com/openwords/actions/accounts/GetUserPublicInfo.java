package com.openwords.actions.accounts;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.UserInfo;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetUserPublicInfo extends MyAction {

    private static final long serialVersionUID = 1L;
    private long userId;
    private String errorMessage, username;

    @Action(value = "/getUserPublicInfo", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getUserPublicInfo");
        Session s = DatabaseHandler.getSession();
        try {
            UserInfo user = (UserInfo) s.get(UserInfo.class, userId);
            if (user != null) {
                username = user.getUsername();
            }
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
