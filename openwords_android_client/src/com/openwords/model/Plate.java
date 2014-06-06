package com.openwords.model;

import android.content.Context;
import com.orm.SugarRecord;

/**
 *
 * @author hanaldo
 */
public class Plate extends SugarRecord<Plate> {

    public int connectionId;
    public String wordOne;
    public String wordTwo;
    public String transcription;
    public int perf;
    public int exposure;

    public Plate(Context cntxt) {
        super(cntxt);
    }

    public Plate(int connectionId, String wordOne, String wordTwo, String transcription, int perf, int exposure, Context cntxt) {
        super(cntxt);
        this.connectionId = connectionId;
        this.wordOne = wordOne;
        this.wordTwo = wordTwo;
        this.transcription = transcription;
        this.perf = perf;
        this.exposure = exposure;
    }
}
