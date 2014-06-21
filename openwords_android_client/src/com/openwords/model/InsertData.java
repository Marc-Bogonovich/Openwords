package com.openwords.model;

import android.content.Context;

import com.orm.SugarRecord;

public class InsertData extends SugarRecord<UserPerformance>{

	public InsertData(Context arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		UserPerformance record = new UserPerformance(4,226,10,1,11,2,130455,0,arg0);
		record.save();
		record = new UserPerformance(5,226,10,1,12,2,130500,0,arg0);
		record.save();
		record = new UserPerformance(6,226,11,0,11,2,130455,0,arg0);
		record.save();
		//bad stuff
		record = new UserPerformance(10,226,1,1,11,2,130000,0,arg0);
		record.save();
		record = new UserPerformance(11,226,0,1,11,2,128800,0,arg0);
		record.save();
		record = new UserPerformance(12,226,1,10,11,2,12950,0,arg0);
		record.save();
	}

}
