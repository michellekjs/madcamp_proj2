package com.example.camp_proj1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS USERDATA (NAME TEXT, NUMBER INTEGER, EMAIL TEXT, IMAGES INTEGER)";
        db.execSQL(sqlCreateTbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
