package com.openwords.actions.course;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Lesson;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@ParentPackage("json-default")
public class DeleteLesson extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage, name;
    private long userId;

    @Action(value = "/deleteLesson", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/deleteLesson: " + name);
        Session s = DatabaseHandler.getSession();
        try {
            Criteria c = s.createCriteria(Lesson.class)
                    .add(Restrictions.eq("name", name))
                    .add(Restrictions.eq("userId", userId));
            s.delete(c.list().get(0));
            s.beginTransaction().commit();
        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            s.close();
        }
        return SUCCESS;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
