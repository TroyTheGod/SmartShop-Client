package com.example.man.smartshop.DBclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDbAdapter {
    private static String DB_NAME="SmartShop";
    private static int DB_VERSION=1;
    public String userid,passwd;

    private Context context;
    private SQLiteOpenHelper myDBHelper;

    public MyDbAdapter(Context context){
        this.context=context;
        this.myDBHelper=new MyDBHelper(this.context,DB_NAME,null,DB_VERSION);
    }
    public void insertSql(String uid,String pw){
        String sql = "INSERT INTO userinfo (uid, password) VALUES ('"+uid+"','"+pw+"')";
        Log.v("MyDBadpt:insert:sql",sql);
        SQLiteDatabase sqLiteDatabase=myDBHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }
    public Cursor select(){
        SQLiteDatabase db=myDBHelper.getReadableDatabase();
        Cursor cursor=db.query("userinfo",new String[]{"uid","password"},null,null,null,null,null);
        displayCursor(cursor);
        db.close();
        return cursor;
    }
    public void displayCursor(Cursor cursor){
        try {
            cursor.moveToFirst();
            String uid=cursor.getString(0);
            String pw=cursor.getString(1);
            userid=uid;
            passwd=pw;
        }catch (Exception e){
            Log.v("MyDBAdapt:display:","ERROR:"+e);
        }
    }
    public void delete(){
        SQLiteDatabase db=myDBHelper.getWritableDatabase();
        String sql ="DELETE FROM userinfo where uid IS NOT NULL";
        db.execSQL(sql);
        db.close();
    }

}
