package com.openwords.util;

public class TimeConvertor {
	
	public TimeConvertor()
	{}
	
	public static long getUnixTime()
	{
		long unixTime = System.currentTimeMillis() / 1000L;
		return unixTime;
	}

}
