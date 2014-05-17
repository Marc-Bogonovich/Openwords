package com.openwords.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Word implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String className = Word.class.getName();
    public static final String TABLE_NAME = "words";
    public static final String COL_ID = "id";
    public static final String COL_LANGUAGE = "language";
    public static final String COL_SPELLING = "spelling";

    public static void addNewWord(SQLiteDatabase db, Word word) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, word.getId());
        values.put(COL_LANGUAGE, word.getLanguage());
        values.put(COL_SPELLING, word.getSpelling());

        db.insert(TABLE_NAME, null, values);//actually SQLiteOpenHelper made this insert action a little bit simpler, but it is still a nightmare
        db.close();
        Log.d(className, "new word added");
    }

    public static void deleteAllWords(SQLiteDatabase db) {
        String sql = "delete from " + TABLE_NAME;
        db.execSQL(sql);
        db.close();
        Log.d(className, "all words cleared");
    }

    public static List<Word> getAllWordsByLanguage(SQLiteDatabase db, int languageId) {
        List<Word> words = new LinkedList<Word>();

        String sql = "select * from " + TABLE_NAME + " where " + COL_LANGUAGE + "=" + languageId;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Word w = new Word();
                w.setId(cursor.getInt(0));
                w.setLanguage(cursor.getInt(1));
                w.setSpelling(cursor.getString(2));

                words.add(w);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return words;
    }

    private int id, language;
    private String spelling;

    public Word() {
    }

    public Word(int language, String spelling) {
        this.language = language;
        this.spelling = spelling;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }
}
