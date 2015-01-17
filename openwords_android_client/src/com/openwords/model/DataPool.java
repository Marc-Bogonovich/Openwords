package com.openwords.model;

import com.openwords.services.ModelLanguage;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used for referring all the static variables centrally.
 *
 * @author hanaldo
 */
public class DataPool {

    public final static String ServerAddress = "192.168.1.104:8080/OpenwordsServer";
    public static List<ModelLanguage> LanguageList = new LinkedList<ModelLanguage>();
    public static boolean DoRegistration = false;
    //public static List<Integer> CurrentLearningLanguages = new LinkedList<Integer>();
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
