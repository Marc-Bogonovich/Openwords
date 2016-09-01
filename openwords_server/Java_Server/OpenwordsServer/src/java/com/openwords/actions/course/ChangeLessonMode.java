package com.openwords.actions.course;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.Lesson;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@ParentPackage("json-default")
public class ChangeLessonMode extends MyAction {

    private static final long serialVersionUID = 1L;
    private String name, errorMessage;
    private long userId;
    private boolean imf;

    @Action(value = "/changeLessonMode", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        Session s = DatabaseHandler.getSession();
        try {
            UtilLog.logInfo(this, "/changeLessonMode: " + name + " " + imf);

            Lesson old = (Lesson) s.createCriteria(Lesson.class)
                    .add(Restrictions.eq("userId", userId))
                    .add(Restrictions.eq("name", name))
                    .list().get(0);
            old.setImf(imf);
            old.setUpdated(System.currentTimeMillis());
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

    public void setImf(boolean imf) {
        this.imf = imf;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
