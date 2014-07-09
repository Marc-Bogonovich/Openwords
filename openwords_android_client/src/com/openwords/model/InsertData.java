package com.openwords.model;

import android.content.Context;



public class InsertData{

	public InsertData(Context arg0) {
		
		// TODO Auto-generated constructor stub
		//UserWords(Context ctx, int connectionId, int wordL2Id, String wordL2, int wordL1Id, String wordL1, int l2Id, String l2Name, String audiocall
		new UserWords(1,1,"猫",101,"cat",2,"chinese",null).save();
		new WordTranscription( 1, "mao").save();;
		new UserWords(2,2,"鸟",102,"bird",2,"chinese",null).save();
		new WordTranscription( 2, "niao").save();
		new UserWords( 3,3,"树",103,"tree",2,"chinese",null).save();
		new WordTranscription( 3, "shu").save();
		new UserWords( 4,4,"花",104,"flower",2,"chinese",null).save();
		new WordTranscription(4, "hua").save();
		new UserWords( 5,5,"水",105,"water",2,"chinese",null).save();
		new WordTranscription( 5, "shui").save();
		new UserWords( 6,6,"马",106,"horse",2,"chinese",null).save();
		new WordTranscription( 6, "ma").save();
		new UserWords( 7,7,"地图",107,"map",2,"chinese",null).save();
		new WordTranscription( 7, "di tu").save();
		new UserWords( 8,8,"太阳",108,"sun",2,"chinese",null).save();
		new WordTranscription( 8, "tai yang").save();
		new UserWords( 9,9,"月亮",109,"moon",2,"chinese",null).save();
		new WordTranscription( 9, "yue liang").save();
		new UserWords( 10,10,"星",110,"star",2,"chinese",null).save();
		new WordTranscription( 10, "xing").save();
		new UserWords( 11,11,"电脑",111,"computer",2,"chinese",null).save();
		new WordTranscription( 11, "dian nao").save();
		new UserWords( 12,12,"狗",112,"dog",2,"chinese",null).save();
		new WordTranscription( 12, "gou").save();
		new UserWords( 13,13,"软件",113,"software",2,"chinese",null).save();
		new WordTranscription( 13, "ruan jian").save();
		new UserWords( 14,14,"铅笔",114,"pencil",2,"chinese",null).save();
		new WordTranscription( 14, "qian bi").save();
		new UserWords( 15,15,"钢笔",115,"pen",2,"chinese",null).save();
		new WordTranscription( 15, "gang bi").save();
		new UserWords( 16,16,"壶",116,"pot",2,"chinese",null).save();
		new WordTranscription( 16, "hu").save();
        new UserWords( 17,17,"字典",117,"dictionary",2,"chinese",null).save();
		new WordTranscription( 17, "zìdiǎn").save();
		//UserPerformance(Context c, int connection_id, int user_id, int total_correct, int total_skipped, 
		//int total_exposure, int last_time, int last_performance, int user_exclude)
//		new UserPerformance( 1,266,1,1,20,130455,2,0).save();
//		new UserPerformance( 2,266,2,3,20,130455,2,0).save();
//		new UserPerformance( 3,266,3,1,20,130455,2,0).save();
//		new UserPerformance( 4,266,4,1,20,130455,2,0).save();
//		new UserPerformance( 5,266,5,1,20,130455,2,0).save();
//		new UserPerformance( 6,266,6,1,20,130455,2,0).save();
//		new UserPerformance( 7,266,7,1,20,130455,2,0).save();
//		new UserPerformance( 8,266,8,1,20,130455,2,0).save();
//		new UserPerformance( 9,266,9,1,20,130455,2,0).save();
//		new UserPerformance( 10,266,10,1,20,130455,2,0).save();
//		new UserPerformance( 11,266,11,1,20,130455,2,0).save();
//		new UserPerformance( 12,266,12,1,20,130455,2,0).save();
//		new UserPerformance( 13,266,13,1,20,130455,2,0).save();
//		new UserPerformance( 14,266,14,1,20,130455,2,0).save();
//		new UserPerformance( 15,266,15,1,20,130455,2,0).save();
//		new UserPerformance( 16,266,16,1,20,130455,2,0).save();
		new UserPerformance(1,266,3,0,20,130455,2,0).save();
		new UserPerformance(2,266,3,0,20,130455,2,0).save();
		new UserPerformance(3,266,3,0,20,130455,2,0).save();
		new UserPerformance(4,266,3,0,20,130455,2,0).save();
		new UserPerformance(5,266,3,0,20,130455,2,0).save();
		new UserPerformance(6,266,3,0,20,130455,2,0).save();
		new UserPerformance(7,266,3,0,20,130455,2,0).save();
		new UserPerformance(8,266,3,0,20,130455,2,0).save();
		new UserPerformance(9,266,3,0,20,130455,2,0).save();
		new UserPerformance(10,266,3,0,20,130455,2,0).save();
		new UserPerformance(11,266,3,0,20,130455,2,0).save();
		new UserPerformance(12,266,3,0,20,130455,2,0).save();
		new UserPerformance(13,266,3,0,20,130455,2,0).save();
		new UserPerformance(14,266,3,0,20,130455,2,0).save();
		new UserPerformance(15,266,3,0,20,130455,2,0).save();
		new UserPerformance(16,266,3,0,20,130455,2,0).save();
        new UserPerformance(17,266,3,0,20,130455,2,0).save();

	}

}
