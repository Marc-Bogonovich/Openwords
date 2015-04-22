package com.openwords.actions.accounts;

import com.openwords.interfaces.MyAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;

@ParentPackage("json-default")
public class ShowBlackList extends MyAction {

    private static final long serialVersionUID = 1L;

    @Action(value = "/showBlack")
    @Override
    public String execute() throws Exception {
        getHttpResponse().getWriter().println(BlackList.printList());
        return null;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
    }
}
