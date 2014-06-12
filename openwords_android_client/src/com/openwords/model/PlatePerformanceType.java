package com.openwords.model;

/**
 *
 * @author hanaldo
 */
public enum PlatePerformanceType {

    Performance_Correct(1), Performance_Incorrect(2), Performance_Null(0);
    private final int value;

    private PlatePerformanceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
