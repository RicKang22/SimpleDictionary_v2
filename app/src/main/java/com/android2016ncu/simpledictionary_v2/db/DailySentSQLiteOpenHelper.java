package com.android2016ncu.simpledictionary_v2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//数据库DailySent表

public class DailySentSQLiteOpenHelper extends SQLiteOpenHelper {

    //建表语句
    private final String CREATE_DAILYSENT = "create table DailySent ( " +
            "id Integer primary key autoincrement," +
            "todate text," +
            "content text," +
            "note text," +
            "trans,text," +
            "tts text," +
            "pictureAddress text)";


    public DailySentSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DAILYSENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
