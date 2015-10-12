package com.openwords.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataPool {

    public final static String ServerAddress = "rose-openwords.rhcloud.com";
    public final static String ServiceProtocol = "https://";
    public static boolean DoRegistration = false;
    public static boolean OffLine = false;
    public static int LmType;
    private static final List<WordConnection> LmPool = new LinkedList<WordConnection>();
    private static final Map<Long, Performance> LmPerformance = new HashMap<Long, Performance>(10);
    public static int LmCurrentCard = -1;
    public static boolean LmReverseNav;
    public static int LmLearningLang = -1;
    public final static int PageSize = 10;
    public final static SetInfo currentSet = new SetInfo();
    public final static List<SetItem> currentSetItems = new ArrayList<SetItem>(0);
    public final static List<Performance> currentPerformance = new LinkedList<Performance>();
    public static int Color_Main;

    public static int getPoolSize() {
        //return LmPool.size();
        return currentSetItems.size();
    }

    public static WordConnection getWordConnection(int index) {
        return LmPool.get(index);
    }

    public static Performance getPerformance(long wordConectionId) {
        return LmPerformance.get(wordConectionId);
    }

    public static Collection<Performance> getAllPerformance(boolean applyLocalVersion) {
        if (applyLocalVersion) {
            for (Performance perf : LmPerformance.values()) {
                perf.version = perf.tempVersion;
            }
        }
        return LmPerformance.values();
    }

    public static void clearLmPool() {
        LmPool.clear();
        LmPerformance.clear();
    }

    public static void addLmPool(List<WordConnection> wc, List<Performance> performance) {
        clearLmPool();
        LmPool.addAll(wc);
        for (Performance perf : performance) {
            LmPerformance.put(perf.wordConnectionId, perf);
        }
    }

    private DataPool() {
    }

}
