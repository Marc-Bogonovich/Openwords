package com.openwords.actions.course;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class ListLessonComment extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private List result;

    @Action(value = "/listLessonComment", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/listLessonComment");
        Session s = DatabaseHandler.getSession();
        try {
            String sql = "SELECT u.user_name, c.content FROM comment as c, user_info as u\n"
                    + "where u.user_id=c.user_id";
            result = s.createSQLQuery(sql).list();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            s.close();
        }
        return SUCCESS;
    }

    public List getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
