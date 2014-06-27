package com.openwords.model;

import android.content.Context;

import com.orm.SugarRecord;

public class InsertData{

	public InsertData(Context arg0) {
		
		// TODO Auto-generated constructor stub
		//UserWords(Context ctx, int connectionId, int wordL2Id, String wordL2, int wordL1Id, String wordL1, int l2Id, String l2Name, String audiocall
		new UserWords(arg0, 1,1,"猫",101,"cat",2,"chinese",null).save();
		new WordTranscription(arg0, 1, "mao").save();;
		new UserWords(arg0, 2,2,"鸟",102,"bird",2,"chinese",null).save();
		new WordTranscription(arg0, 2, "niao").save();
		new UserWords(arg0, 3,3,"树",103,"tree",2,"chinese",null).save();
		new WordTranscription(arg0, 3, "shu").save();
		new UserWords(arg0, 4,4,"花",104,"flower",2,"chinese",null).save();
		new WordTranscription(arg0, 4, "hua").save();
		new UserWords(arg0, 5,5,"水",105,"water",2,"chinese",null).save();
		new WordTranscription(arg0, 5, "shui").save();
		new UserWords(arg0, 6,6,"马",106,"horse",2,"chinese",null).save();
		new WordTranscription(arg0, 6, "ma").save();
		new UserWords(arg0, 7,7,"地图",107,"map",2,"chinese",null).save();
		new WordTranscription(arg0, 7, "di tu").save();
		new UserWords(arg0, 8,8,"太阳",108,"sun",2,"chinese",null).save();
		new WordTranscription(arg0, 8, "tai yang").save();
		new UserWords(arg0, 9,9,"月亮",109,"moon",2,"chinese",null).save();
		new WordTranscription(arg0, 9, "yue liang").save();
		new UserWords(arg0, 10,10,"星",110,"star",2,"chinese",null).save();
		new WordTranscription(arg0, 10, "xing").save();
		new UserWords(arg0, 11,11,"电脑",111,"computer",2,"chinese",null).save();
		new WordTranscription(arg0, 11, "dian nao").save();
		new UserWords(arg0, 12,12,"狗",112,"dog",2,"chinese",null).save();
		new WordTranscription(arg0, 12, "gou").save();
		new UserWords(arg0, 13,13,"软件",113,"software",2,"chinese",null).save();
		new WordTranscription(arg0, 13, "ruan jian").save();
		new UserWords(arg0, 14,14,"铅笔",114,"pencil",2,"chinese",null).save();
		new WordTranscription(arg0, 14, "qian bi").save();
		new UserWords(arg0, 15,15,"钢笔",115,"pen",2,"chinese",null).save();
		new WordTranscription(arg0, 15, "gang bi").save();
		new UserWords(arg0, 16,16,"壶",116,"pot",2,"chinese",null).save();
		new WordTranscription(arg0, 16, "hu").save();
		//UserPerformance(Context c, int connection_id, int user_id, int total_correct, int total_skipped, 
		//int total_exposure, int last_time, int last_performance, int user_exclude)
		new UserPerformance(arg0, 1,266,1,1,20,130455,2,0).save();
		new UserPerformance(arg0, 2,266,2,3,20,130455,2,0).save();
		new UserPerformance(arg0, 3,266,3,1,20,130455,2,0).save();
		new UserPerformance(arg0, 4,266,4,1,20,130455,2,0).save();
		new UserPerformance(arg0, 5,266,5,1,20,130455,2,0).save();
		new UserPerformance(arg0, 6,266,6,1,20,130455,2,0).save();
		new UserPerformance(arg0, 7,266,7,1,20,130455,2,0).save();
		new UserPerformance(arg0, 8,266,8,1,20,130455,2,0).save();
		new UserPerformance(arg0, 9,266,9,1,20,130455,2,0).save();
		new UserPerformance(arg0, 10,266,10,1,20,130455,2,0).save();
		new UserPerformance(arg0, 11,266,11,1,20,130455,2,0).save();
		new UserPerformance(arg0, 12,266,12,1,20,130455,2,0).save();
		new UserPerformance(arg0, 13,266,13,1,20,130455,2,0).save();
		new UserPerformance(arg0, 14,266,14,1,20,130455,2,0).save();
		new UserPerformance(arg0, 15,266,15,1,20,130455,2,0).save();
		new UserPerformance(arg0, 16,266,16,1,20,130455,2,0).save();

	}

}
