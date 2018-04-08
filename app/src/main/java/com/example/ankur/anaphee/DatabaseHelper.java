package com.example.ankur.anaphee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ankur on 9/4/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "heartBeat.db";
    public static final String TABLE_NAME = "stats_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Date";
    public static final String COL_3 = "Time";
    public static final String COL_4 = "beats";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db= this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, TIME INTEGER, BEATS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public  boolean insertData(String date, long time,int beats){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,date);
        contentValues.put(COL_3,time);
        contentValues.put(COL_4,beats);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1) return false;

        else return true;
    }
    public Cursor getAllData(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from "+TABLE_NAME,null);
        return res;
    }
}
