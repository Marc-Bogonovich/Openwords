package com.openwords.actions.performances;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.UserPerformance;
import com.openwords.database.UserPerformanceId;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class SetUserPerformance extends MyAction {

    private static final long serialVersionUID = 1L;
    private String performance;
    private String errorMessage;
    private boolean result;
    private int userId;

    @Action(value = "/setUserPerformance", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/setUserPerformance: " + userId + " " + performance);
        Session s = DatabaseHandler.getSession();
        try {
            Object[] versions = (Object[]) MyGson.fromJson(performance, Object[].class);
            List<UserPerformance> perfs = new ArrayList<>(versions.length / 3);
            for (int i = 0; i < versions.length; i += 3) {
                int connectionId = ((Number) versions[i]).intValue();
                String perf = (String) versions[i + 1];
                int version = ((Number) versions[i + 2]).intValue();
                perfs.add(new UserPerformance(new UserPerformanceId(userId, connectionId, "all"), perf, version));
            }
            UserPerformance.updatePerformances(s, perfs);
            result = true;
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
