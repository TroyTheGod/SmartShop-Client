package com.example.man.smartshop.DBclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table userinfo (uid TEXT,password TEXT)";
        Log.v("MYDBHelper:onCreate:sql",sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS userinfo";
        Log.v("MYDBHelper:onUp:sql",sql);
        db.execSQL(sql);
        onCreate(db);
    }

}
