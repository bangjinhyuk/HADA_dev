package com.hada.pillmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists mytable ("
                + "_id integer primary key autoincrement,"
                + "name text,"
                + "day text,"
                + "date text,"
                + "setHour integer,"
                + "setMin integer,"
                + "lastdate text,"
                + "caseNum integer);";

        db.execSQL(sql);
        String sql1 = "CREATE TABLE if not exists date ("
                + "_id integer primary key autoincrement,"
                + "date text,"
                + "completepill text);";

        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists mytable";
        db.execSQL(sql);

        String sql1 = "DROP TABLE if exists date";
        db.execSQL(sql1);

        onCreate(db);
    }
}