package com.openwords.actions.course;

import com.openwords.database.DatabaseHandler;
import com.openwords.database.Lesson;
import com.openwords.database.LessonContent;
import com.openwords.database.StepContent;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.apache.struts2.convention.annotation.Action;
import org.hibernate.Session;

public class UploadLesson extends MyAction {

    private static final long serialVersionUID = 1L;
    private File file;
    private String userId, name;

    @Action(value = "/uploadLesson")
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/uploadLesson: " + userId + " " + name);
        if (file == null) {
            sendBadRequest("Need actual file");
            return null;
        }
        if (userId == null) {
            sendBadRequest("Need user info");
            return null;
        }
        if (name == null) {
            sendBadRequest("Need name");
            return null;
        }

        Session s = DatabaseHandler.getSession();
        try {
            long uid = Long.parseLong(userId);
            long now = System.currentTimeMillis();

            Lesson le = Lesson.getLesson(s, uid, name);
            if (le != null) {
                getHttpResponse().sendError(901, "Lessons cannot have the same name");
                return null;
            }

            List<String> lines;
            try (Scanner scan = new Scanner(file)) {
                lines = new LinkedList<>();
                while (scan.hasNextLine()) {
                    lines.add(scan.nextLine().trim().replaceAll("\uFEFF", ""));
                }
            }

            List<StepContent> steps = LessonContentConverter.getStepsFromText(lines);
            if (steps.isEmpty()) {
                sendBadRequest("No valid content");
                return null;
            }
            le = new Lesson(uid, name, "", "", now);
            LessonContent content = new LessonContent();
            content.steps = steps;
            le.setContent(MyGson.toJson(content));

            s.save(le);
            s.beginTransaction().commit();

        } catch (Exception e) {
            UtilLog.logError(this, e);
            sendError(e.getMessage());
        } finally {
            s.close();
        }
        return null;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }

}
