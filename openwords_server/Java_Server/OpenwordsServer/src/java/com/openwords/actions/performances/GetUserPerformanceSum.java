package com.openwords.actions.performances;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.UserPerformance;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetUserPerformanceSum extends MyAction {

    private static final long serialVersionUID = 1L;
    private int userId, baseLang, learningLang;
    private String errorMessage;
    private int totalGood;
    private int total;
    private int totalVersion;

    @Action(value = "/getUserPerformanceSum", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getUserPerformanceSum: " + userId + " " + baseLang + " " + learningLang);
        Session s = DatabaseHandler.getSession();
        try {
            int[] r = UserPerformance.getPerformanceSummary(s, userId, baseLang, learningLang);
            totalGood = r[0];
            total = r[1];
            totalVersion = r[2];
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBaseLang(int baseLang) {
        this.baseLang = baseLang;
    }

    public void setLearningLang(int learningLang) {
        this.learningLang = learningLang;
    }

    public int getTotalGood() {
        return totalGood;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalVersion() {
        return totalVersion;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
