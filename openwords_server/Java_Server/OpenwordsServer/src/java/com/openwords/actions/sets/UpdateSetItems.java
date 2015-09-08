package com.openwords.actions.sets;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.SetInfo;
import com.openwords.database.SetItem;
import com.openwords.database.SetItemId;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class UpdateSetItems extends MyAction {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private String connectionIds;
    private long setId, userId;
    private List<SetItem> itemsResult;

    @Action(value = "/updateSetItems", results = {
        @Result(name = SUCCESS, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/updateSetItems: " + connectionIds + " " + setId);
        Session s = DatabaseHandler.getSession();
        try {
            SetInfo info = SetInfo.getSetInfo(s, setId);
            if (info.getUserId() != userId) {
                throw new Exception("set information is not correct");
            }

            long[] connections = MyGson.fromJson(connectionIds, long[].class);
            itemsResult = new ArrayList<>(connections.length);
            for (int i = 0; i < connections.length; i++) {
                itemsResult.add(i, new SetItem(new SetItemId(connections[i]), 0, i + 1));
            }
            SetItem.updateSetItems(s, setId, itemsResult);

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setConnectionIds(String connectionIds) {
        this.connectionIds = connectionIds;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<SetItem> getItemsResult() {
        return itemsResult;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
