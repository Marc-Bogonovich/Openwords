package com.openwords.database;

public class StepContentItem {

    public static final String Item_Type_Problem = "pro";
    public static final String Item_Type_Answer = "ans";
    public static final String Item_Type_Marplot = "mar";
    public static final String Answer_Placeholder = "@";

    public String type;
    public String[] text;

    public StepContentItem(String[] text, String type) {
        this.text = text;
        this.type = type;
    }

}
