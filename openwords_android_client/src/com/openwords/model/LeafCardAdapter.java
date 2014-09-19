package com.openwords.model;

import com.openwords.util.WSAinterface;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.List;

public abstract class LeafCardAdapter {

    protected List<WSAinterface> wordSelectionAlg = OpenwordsSharedPreferences.getWordSelectionAlgList();
    protected int algIndex = OpenwordsSharedPreferences.getAlgIndex();

}
