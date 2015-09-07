package com.openwords.actions.connections;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.openwords.database.DatabaseHandler;
import com.openwords.database.UserInfo;
import com.openwords.database.UserPerformance;
import com.openwords.database.UserPerformanceId;
import com.openwords.database.Word;
import com.openwords.database.WordConnection;
import com.openwords.interfaces.MyAction;
import com.openwords.utils.MyFieldValidation;
import com.openwords.utils.UtilLog;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Session;

@ParentPackage("json-default")
public class GetWordConnectionsPack extends MyAction {

    private static final long serialVersionUID = 1L;
    private int langOneId, langTwoId, pageNumber, pageSize;
    private String errorMessage;
    private boolean doOrder;
    private String orderBy;
    private List<WordConnection> connections;
    private List<Word> words;
    private List<UserPerformance> performance;
    private int userId;

    @Action(value = "/getWordConnectionsPack", results = {
        @Result(name = SUCCESS, type = "json"),
        @Result(name = INPUT, type = "json")
    })
    @Override
    public String execute() throws Exception {
        UtilLog.logInfo(this, "/getWordConnectionsPack: " + langOneId + " " + langTwoId + " " + pageNumber + " " + pageSize + ", user " + userId);
        Session s = DatabaseHandler.getSession();
        try {
            if (UserInfo.checkUserId(s, userId)) {
                throw new Exception("no user");
            }

            int totalPerformance = UserPerformance.countPerformance(s, userId, langOneId, langTwoId, "all");
            int targetTotal = pageNumber * pageSize;

            if (targetTotal > totalPerformance) {
                //if user does not have enough words, just use/re-use the requested page
                UtilLog.logInfo(this, "Use next page connections for user:" + userId);
                if (doOrder) {
                    connections = WordConnection.getConnectionsPageWithOrder(s, langOneId, langTwoId, pageNumber, pageSize, orderBy.trim(), false);
                } else {
                    connections = WordConnection.getConnectionsPage(s, langOneId, langTwoId, pageNumber, pageSize, false);
                }

                performance = getPerformances(s, connections);
                loadPerformance(s, connections, performance);
            } else {
                //else just chose connections from user current performance
                UtilLog.logInfo(this, "Pick current performance connections for user:" + userId);
                performance = UserPerformance.getPerformancePage(s, userId, langOneId, langTwoId, "all", pageNumber, pageSize);
                connections = getWordConnections(s, performance);
            }

            words = fillWords(s, connections);

        } catch (Exception e) {
            errorMessage = e.toString();
            UtilLog.logWarn(this, errorMessage);
        } finally {
            DatabaseHandler.closeSession(s);
        }
        return SUCCESS;
    }

    private List<Word> fillWords(Session s, List<WordConnection> connections) {
        Set<Long> wordIds = new HashSet<>(connections.size());
        for (WordConnection connection : connections) {
            wordIds.add(connection.getWordOneId());
            wordIds.add(connection.getWordTwoId());
        }
        return Word.getWords(s, wordIds);
    }

    private List<UserPerformance> getPerformances(Session s, List<WordConnection> connections) {
        Long[] connectionIds = new Long[connections.size()];
        for (int i = 0; i < connections.size(); i++) {
            connectionIds[i] = connections.get(i).getConnectionId();
        }
        return UserPerformance.getPerformances(s, userId, connectionIds);
    }

    private List<WordConnection> getWordConnections(Session s, List<UserPerformance> perf) {
        Long[] connectionIds = new Long[perf.size()];
        for (int i = 0; i < perf.size(); i++) {
            connectionIds[i] = perf.get(i).getWordConnectionId();
        }
        return WordConnection.getConnections(s, connectionIds);
    }

    private void loadPerformance(Session s, List<WordConnection> target, List<UserPerformance> existed) {
        if (target.size() != existed.size()) {
            UtilLog.logInfo(this, "Create new UserPerformance records: " + (target.size() - existed.size()));
            Set<Long> newIds = new HashSet<>(target.size());
            for (WordConnection conn : target) {
                newIds.add(conn.getConnectionId());
            }
            for (UserPerformance perf : existed) {
                newIds.remove(perf.getWordConnectionId());
            }
            for (Long newId : newIds) {
                UserPerformance newPerf = new UserPerformance(new UserPerformanceId(userId, newId, "all"), "new", 1);
                s.save(newPerf);
                existed.add(newPerf);
            }
            s.beginTransaction().commit();
        }
    }

    public void setLangOneId(int langOneId) {
        this.langOneId = langOneId;
    }

    public void setLangTwoId(int langTwoId) {
        this.langTwoId = langTwoId;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setDoOrder(boolean doOrder) {
        this.doOrder = doOrder;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public void validate() {
        MyFieldValidation.checkPageSize(this, pageSize, 100, 10);
        MyFieldValidation.checkBooleanAndString(this, "doOrder", doOrder, orderBy);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<WordConnection> getConnections() {
        return connections;
    }

    public List<Word> getWords() {
        return words;
    }

    public List<UserPerformance> getPerformance() {
        return performance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
