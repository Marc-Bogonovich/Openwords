package com.openwords.utils;

import com.openwords.database.DatabaseHandler;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author hanaldo
 */
public class MyContextListener implements ServletContextListener {

    private static String contextPath;
    private static final String cloudContextPath = "/var/lib/openshift/55cb78702d52716caf000105/app-root/data/";

    public static String getContextPath(boolean useExternal) {
        if (useExternal) {
            return cloudContextPath;
        } else {
            return contextPath;
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        contextPath = sce.getServletContext().getRealPath("/");
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        UtilLog.logInfo(this, "Servlet Context Path: " + contextPath);

        DatabaseHandler.getInstance();
        UtilLog.logInfo(this, "contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseHandler.getInstance().clean();
        UtilLog.logInfo(this, "contextDestroyed");
    }
}
