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

    private DataPool() {
    }
}