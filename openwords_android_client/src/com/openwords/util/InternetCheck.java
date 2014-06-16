package com.openwords.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetCheck {
	
	public static boolean checkConn(Context ctx) {
	    ConnectivityManager connMgr = (ConnectivityManager) ctx
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if(networkInfo!=null && networkInfo.isConnectedOrConnecting()) {
	    	return true;
	    }
	    return false;
	}
}
