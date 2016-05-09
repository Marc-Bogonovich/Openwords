package com.openwords.database;

public class LessonContentItem {

    public String type;
    public String[] text;

    public LessonContentItem(String[] text, String type) {
        this.text = text;
        this.type = type;
    }

}
