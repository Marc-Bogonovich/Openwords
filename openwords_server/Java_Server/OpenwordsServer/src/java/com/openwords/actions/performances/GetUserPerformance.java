package com.openwords.actions.performances;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.UserPerformance;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetUserPerformance extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<UserPerformance> performance;
    private String connctions;
    private String errorMessage;
    private boolean result;
    private int userId;

    @Action(value = "/getUserPerformance", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getUserPerformance: " + userId);
        Session s = DatabaseHandler.getSession();
        try {
            performance = UserPerformance.getPerformances(s, userId, (Integer[]) MyGson.fromJson(connctions, Integer[].class));
            result = true;
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setConnctions(String connctions) {
        this.connctions = connctions;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<UserPerformance> getPerformance() {
        return performance;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isResult() {
        return result;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
