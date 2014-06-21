package com.openwords.util.log;

import android.util.Log;

/**
 *
 * @author hanaldo
 */
public class LogUtil {

    private static boolean doLog = true;

    public static void turnOffLog() {
        doLog = false;
    }

    public static void logDeubg(Object c, Object message) {
        if (!doLog) {
            return;
        }
        Log.d(getClassName(c), message.toString());
    }

    public static void logWarning(Object c, Object message) {
        if (!doLog) {
            return;
        }
        Log.w(getClassName(c), message.toString());
    }

    private static String getClassName(Object o) {
        if (o instanceof Class) {
            return o.toString();
        } else {
            return o.getClass().getName();
        }
    }

    private LogUtil() {
    }
}
