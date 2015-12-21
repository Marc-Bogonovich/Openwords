package com.openwords.actions.sets;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.SetInfo;
import com.openwords.database.SetItem;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.UtilLog;
import java.util.LinkedList;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class CopySet extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private long user, targetSet;
    private String name;

    @Action(value = "/copySet", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/copySet: " + targetSet + " " + user);
        Session s = DatabaseHandler.getSession();
        try {
            if (name == null) {
                throw new Exception("You must give a name");
            }
            SetInfo set = SetInfo.getSetInfo(s, targetSet);
            if (set == null) {
                throw new Exception("No such set");
            }
            if (set.getUserId() == user) {
                throw new Exception("You cannot copy your own set");
            }
            SetInfo newSet = new SetInfo(0, user, set.getNativeLang(), set.getLearningLang(), 1, name, true, set.getMeta());
            SetInfo.updateSet(s, newSet);

            List<SetItem> items = SetItem.getSetItems(s, targetSet);
            List<SetItem> newItems = new LinkedList<>();
            for (SetItem item : items) {
                newItems.add(new SetItem(item.getWordOneId(), item.getWordTwoId(), item.getItemOrder(), item.getWordOne(), item.getWordTwo()));
            }
            SetItem.updateSetItems(s, newSet.getSetId(), newItems);
            newSet.setSetSize(newItems.size());
            s.beginTransaction().commit();

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, e);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public void setTargetSet(long targetSet) {
        this.targetSet = targetSet;
    }

}
