package com.openwords.actions.course;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.Course;
import com.openwords.database.CourseContent;
import com.openwords.database.DatabaseHandler;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class CreateCourse extends MyAction {

    private static final long serialVersionUID = 1L;
    private String name, userName, langOne, langTwo, comment, errorMessage;
    private long userId;

    @Action(value = "/createCourse", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/createCourse: " + name);
        Session s = DatabaseHandler.getSession();
        try {
            Course c = new Course();
            c.setName(name);
            c.setUserId(userId);
            c.setFileCover("");
            c.setLangOne("");
            c.setLangTwo("");
            c.setUpdated(System.currentTimeMillis());

            CourseContent content = new CourseContent();
            content.authorName = userName;
            content.comment = comment;
            c.setContent(MyGson.toJson(content));

            s.save(c);
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLangOne(String langOne) {
        this.langOne = langOne;
    }

    public void setLangTwo(String langTwo) {
        this.langTwo = langTwo;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
