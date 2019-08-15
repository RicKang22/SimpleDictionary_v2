package com.android2016ncu.simpledictionary_v2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 数据库Word表

public class WordsSQLiteOpenHelper extends SQLiteOpenHelper {
    // 建表语句
    private final String CREATE_WORDS = "create table Words(" +
            "id Integer primary key autoincrement," +
            "isChinese text," +
            "keys text," +
            "fy text," +
            "psE text," +
            "pronE text," +
            "psA text," +
            "pronA text," +
            "posAcceptation text," +
            "sent text)";

    public WordsSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
