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

    public static List<ModelLanguage> LanguageList = new LinkedList<ModelLanguage>();
    public static int BaseLanguage = -1;
    public static int UserId = -1;
    public static String Username;
    public static String Password;
    public static boolean DoRegistration = false;
    public static List<Integer> CurrentLearningLanguages = new LinkedList<Integer>();

    private DataPool() {
    }
}
