package com.openwords.model;

import android.content.Context;

import com.orm.SugarRecord;

public class InsertData{

	public InsertData(Context arg0) {
		
		// TODO Auto-generated constructor stub
		UserPerformance record = new UserPerformance(4,266,10,1,11,2,130455,0,arg0);
		record.save();
		record = new UserPerformance(5,266,10,1,12,2,130500,0,arg0);
		record.save();
		record = new UserPerformance(6,266,11,0,11,2,130455,0,arg0);
		record.save();
		//bad stuff
		record = new UserPerformance(10,266,1,1,11,2,130000,0,arg0);
		record.save();
		record = new UserPerformance(11,266,0,1,11,2,128800,0,arg0);
		record.save();
		record = new UserPerformance(12,266,1,10,11,2,12950,0,arg0);
		record.save();
		record = new UserPerformance(13,266,1,10,11,2,12950,0,arg0);
		record.save();
		record = new UserPerformance(14,266,1,9,10,2,12950,0,arg0);
		record.save();
		record = new UserPerformance(15,266,1,5,6,2,12950,0,arg0);
		record.save();
	}

}
