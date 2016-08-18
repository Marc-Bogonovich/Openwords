package com.openwords.actions.course;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.Course;
import com.openwords.database.DatabaseHandler;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@ParentPackage("json-default")
public class DeleteCourse extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage, pass;
    private long authorId, userId, makeTime;

    @Action(value = "/deleteCourse", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        UtilLog.logInfo(this, String.format("/deleteCourse\n"
                + "makeTime: %s\n"
                + "authorId: %s\n"
                + "userId: %s\n", makeTime, authorId, userId));
        if (!pass.equals("别瞎删昂!")) {
            errorMessage = "Wrong pass";
            return SUCCESS;
        }
        Session s = DatabaseHandler.getSession();
        try {
            Criteria c = s.createCriteria(Course.class)
                    .add(Restrictions.eq("makeTime", makeTime))
                    .add(Restrictions.eq("authorId", authorId))
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

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public void setMakeTime(long makeTime) {
        this.makeTime = makeTime;
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
