package com.openwords.model;
import java.util.List;
import android.util.Log;
import com.openwords.util.WSAinterface;

import com.openwords.util.preference.OpenwordsSharedPreferences;

public class LeafCardAdapter {
	
	protected List<WSAinterface> wordSelectionAlg = OpenwordsSharedPreferences.getWordSelectionAlgList();
	protected int algIndex = OpenwordsSharedPreferences.getAlgIndex();
	
}
