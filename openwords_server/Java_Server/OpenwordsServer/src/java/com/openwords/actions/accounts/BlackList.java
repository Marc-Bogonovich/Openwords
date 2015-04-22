package com.openwords.actions.accounts;

import com.openwords.utils.MyGson;
import com.openwords.utils.UtilLog;
import java.util.HashMap;
import java.util.Map;

public class BlackList {

    private static Map<String, Object[]> list = new HashMap<>(100);
    private final static int legalTimes = 50;

    public synchronized static String printList() {
        return MyGson.toPrettyJson(list);
    }

    public synchronized static boolean requestLegal(String client) {
        if (list.containsKey(client)) {
            long now = System.currentTimeMillis();

            Object[] record = list.get(client);
            int totalFoul = (int) record[1];
            if (totalFoul > legalTimes) {
                UtilLog.logInfo(BlackList.class, "Client is in BlackList: " + client);
                return false;
            }

            long lastTime = (long) record[0];
            long diff = now - lastTime;

            if (diff > 5000) {
                record[0] = now;
                record[1] = 0;
                UtilLog.logInfo(BlackList.class, "Reset client: " + client);
                return true;
            } else {
                record[0] = now;
                record[1] = totalFoul + 1;
                UtilLog.logInfo(BlackList.class, "Client foul: " + client + ", " + record[1]);
                return true;
            }
        } else {
            list.put(client, new Object[]{System.currentTimeMillis(), 0});
            return true;
        }
    }

    private BlackList() {
    }
}
