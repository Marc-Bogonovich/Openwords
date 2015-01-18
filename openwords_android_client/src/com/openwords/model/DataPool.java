package com.openwords.model;

import java.util.List;

/**
 * This class is used for referring all the static variables centrally.
 *
 * @author hanaldo
 */
public class DataPool {

    public final static String ServerAddress = "192.168.1.104:8080/OpenwordsServer";
    public static boolean DoRegistration = false;
    public static boolean OffLine = false;
    private static LocalSettings localSettings = null;

    public static LocalSettings getLocalSettings() {
        if (localSettings == null) {
            List<LocalSettings> r = LocalSettings.listAll(LocalSettings.class);
            if (r.isEmpty()) {
                localSettings = new LocalSettings();
            } else {
                localSettings = r.get(0);
            }
        }
        return localSettings;
    }

    public static void saveLocalSettings() {
        localSettings.save();
    }

    public static void clearLocalSettings() {
        LocalSettings.deleteAll(LocalSettings.class);
    }

    private DataPool() {
    }
}
