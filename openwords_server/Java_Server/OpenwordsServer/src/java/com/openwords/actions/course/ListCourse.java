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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@ParentPackage("json-default")
public class ListCourse extends MyAction {

    private static final long serialVersionUID = 1L;
    private List<Course> result;
    private String errorMessage;
    private int pageNumber = 1, pageSize = 5;
    private long userId, authorId;

    @Action(value = "/listCourse", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/listCourse: " + authorId + " " + userId);
        Session s = DatabaseHandler.getSession();
        try {
            int firstRecord = (pageNumber - 1) * pageSize;
            Criteria c = s.createCriteria(Course.class)
                    .setFirstResult(firstRecord)
                    .setMaxResults(pageSize + 1)
                    .addOrder(Order.desc("makeTime"));

            if (userId > 0) {
                c.add(Restrictions.eq("userId", userId));
            } else if (authorId > 0) {
                c.add(Restrictions.eq("authorId", authorId));
            }

            result = c.list();
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

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Course> getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
