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

    public static String getContextPath() {
        return contextPath;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        contextPath = sce.getServletContext().getRealPath("/");
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        UtilLog.logEnvironment(this, "Servlet Context Path: " + contextPath);

        DatabaseHandler.getInstance();
        UtilLog.logEnvironment(this, "contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseHandler.getInstance().clean();
        UtilLog.logEnvironment(this, "contextDestroyed");
    }
}
