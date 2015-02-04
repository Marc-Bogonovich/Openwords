package com.openwords.model;

import com.openwords.learningmodule.LearningModuleType;
import java.util.List;

/**
 * This class is used for referring all the static variables centrally.
 *
 * @author hanaldo
 */
public class DataPool {

    public final static String ServerAddress = "192.168.1.109:8080/OpenwordsServer";
    public static boolean DoRegistration = false;
    public static boolean OffLine = false;
    public static LearningModuleType LmType;
    public static List<WordConnection> LmPool;
    public static int LmCurrentCard = -1;
    public static boolean LmReverseNav;

    private DataPool() {
    }
}
