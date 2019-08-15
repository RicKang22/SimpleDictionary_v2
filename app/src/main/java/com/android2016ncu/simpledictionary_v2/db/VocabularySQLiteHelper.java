package com.android2016ncu.simpledictionary_v2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//数据库Vocabulary表

public class VocabularySQLiteHelper extends SQLiteOpenHelper {

    // 建表语句
    private final String CREATE_VOCABULARY = "create table Vocabulary ( " +
            "id Integer primary key autoincrement," +
            "wordsKey text," +
            "translation text," +
            "masteryLevel Integer," +
            "rights Integer," +
            "wrong Integer)";

    public VocabularySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VOCABULARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
