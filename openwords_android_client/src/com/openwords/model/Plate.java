package com.openwords.model;

import android.content.Context;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class Plate extends SugarRecord<Plate> {

    private int connectionId;
    @Ignore
    private PlatePerformanceType performance;
    private int performanceInt;
    @Ignore
    private PlateTestType testType;
    private int testTypeInt;
    private String audioCall;
    private String clarification;
    private String problemOne;
    private String problemTwo;//for now, we put the transcription here
    private String problemThree;
    private String answerOne;
    private String answerTwo;//for now, we put a wrong answer here
    private long plateId;

    public Plate(Context cntxt) {
        super(cntxt);
    }

    public Plate(int connectionId, PlatePerformanceType performance, PlateTestType testType, String audioCall, String clarification, String problemOne, String problemTwo, String problemThree, String answerOne, String answerTwo, long plateId, Context cntxt) {
        super(cntxt);
        this.connectionId = connectionId;
        this.performance = performance;
        performanceInt = performance.getValue();
        this.testType = testType;
        testTypeInt = testType.getValue();
        this.audioCall = audioCall;
        this.clarification = clarification;
        this.problemOne = problemOne;
        this.problemTwo = problemTwo;
        this.problemThree = problemThree;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.plateId = plateId;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public PlatePerformanceType getPerformance() {
        if (performance == null) {
            if (performanceInt == PlatePerformanceType.Performance_Correct.getValue()) {
                performance = PlatePerformanceType.Performance_Correct;
            } else if (performanceInt == PlatePerformanceType.Performance_Incorrect.getValue()) {
                performance = PlatePerformanceType.Performance_Incorrect;
            } else if (performanceInt == PlatePerformanceType.Performance_Null.getValue()) {
                performance = PlatePerformanceType.Performance_Null;
            }
        }
        return performance;
    }

    public void setPerformance(PlatePerformanceType performance) {
        this.performance = performance;
        performanceInt = performance.getValue();
    }

    public PlateTestType getTestType() {
        if (testType == null) {
            if (testTypeInt == PlateTestType.Test_Type_Review.getValue()) {
                testType = PlateTestType.Test_Type_Review;
            } else if (testTypeInt == PlateTestType.Test_Type_Self_Evaluate.getValue()) {
                testType = PlateTestType.Test_Type_Self_Evaluate;
            }
        }
        return testType;
    }

    public void setTestType(PlateTestType testType) {
        this.testType = testType;
        testTypeInt = testType.getValue();
    }

    public String getAudioCall() {
        return audioCall;
    }

    public void setAudioCall(String audioCall) {
        this.audioCall = audioCall;
    }

    public String getClarification() {
        return clarification;
    }

    public void setClarification(String clarification) {
        this.clarification = clarification;
    }

    public String getProblemOne() {
        return problemOne;
    }

    public void setProblemOne(String problemOne) {
        this.problemOne = problemOne;
    }

    public String getProblemTwo() {
        return problemTwo;
    }

    public void setProblemTwo(String problemTwo) {
        this.problemTwo = problemTwo;
    }

    public String getProblemThree() {
        return problemThree;
    }

    public void setProblemThree(String problemThree) {
        this.problemThree = problemThree;
    }

    public String getAnswerOne() {
        return answerOne;
    }

    public void setAnswerOne(String answerOne) {
        this.answerOne = answerOne;
    }

    public String getAnswerTwo() {
        return answerTwo;
    }

    public void setAnswerTwo(String answerTwo) {
        this.answerTwo = answerTwo;
    }

    public long getPlateId() {
        return plateId;
    }

    public void setPlateId(long plateId) {
        this.plateId = plateId;
    }
}