package com.openwords.model;

/**
 *
 * @author hanaldo
 */
public enum PlateTestType {

    Test_Type_Review(1), Test_Type_Self_Evaluate(2);
    private final int value;

    private PlateTestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
