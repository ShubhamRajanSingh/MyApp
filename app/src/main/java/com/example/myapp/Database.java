package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Database.java
//author 'Shubham R Singh'

public class Database extends SQLiteOpenHelper {

    private static int version=1;
    private static String databaseName="MyApp";
    public Database(Context context){
        super(context,databaseName,null,version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table TASKS(_id INTEGER PRIMARY KEY AUTOINCREMENT,TASK TEXT,DONE BOOLEAN)";
        db.execSQL(sql);
        String sql1="create table JOBS(_id INTEGER PRIMARY KEY AUTOINCREMENT,DOMAIN TEXT,SUBDOMAIN TEXT,DONE BOOLEAN)";
        db.execSQL(sql1);
        String sql2="create table REMINDER(_id INTEGER PRIMARY KEY AUTOINCREMENT,MESSAGE TEXT,datetimes TEXT,date TEXT,time TEXT)";
        db.execSQL(sql2);

        insertData("null","2050-12-12 12:12:12","2050-12-12","12:12:12",db);

    }

    private void insertData(String task,String DONE,String date,String time,SQLiteDatabase db){
        ContentValues values=new ContentValues();
        values.put("MESSAGE",task);
        values.put("datetimes",DONE);
        values.put("date",date);
        values.put("time",time);
        db.insert("REMINDER",null,values);

    }
    private void insertDataJob(String task,String sub,String DONE,SQLiteDatabase db){
        ContentValues values=new ContentValues();
        values.put("DOMAIN",task);
        values.put("SUBDOMAIN",sub);
        values.put("DONE",DONE);
        db.insert("JOBS",null,values);

    }
    private void deleteData(String task,SQLiteDatabase db){

        db.delete("TASKS","TASK=? ",new String[]{task});

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
