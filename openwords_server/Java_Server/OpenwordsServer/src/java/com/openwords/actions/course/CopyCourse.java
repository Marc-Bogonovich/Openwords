package com.openwords.actions.course;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.Course;
import com.openwords.database.DatabaseHandler;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@ParentPackage("json-default")
public class CopyCourse extends MyAction {

    private static final long serialVersionUID = 1L;
    private Course result;
    private String errorMessage;
    private long authorId, makeTime, userId;
    private boolean learn;

    @Action(value = "/copyCourse", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        UtilLog.logInfo(this, String.format("/copyCourse\n"
                + "makeTime: %s\n"
                + "authorId: %s\n"
                + "userId: %s\n", makeTime, authorId, userId));
        Session s = DatabaseHandler.getSession();
        try {
            if (userId <= 0) {
                throw new Exception("userId not valid");
            }
            List<Course> record = s.createCriteria(Course.class)
                    .add(Restrictions.eq("userId", userId))
                    .add(Restrictions.eq("authorId", authorId))
                    .add(Restrictions.eq("makeTime", makeTime)).list();

            if (record.isEmpty()) {
                result = (Course) s.createCriteria(Course.class)
                        .add(Restrictions.eq("userId", 0l))
                        .add(Restrictions.eq("authorId", authorId))
                        .add(Restrictions.eq("makeTime", makeTime))
                        .list().get(0);

                s.evict(result);
                result.setUserId(userId);
                s.save(result);
                s.beginTransaction().commit();
            } else {
                result = record.get(0);
            }

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            s.close();
        }
        return SUCCESS;
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

    public void setLearn(boolean learn) {
        this.learn = learn;
    }

    public Course getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
