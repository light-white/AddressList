package com.example.xiaoyu.addresslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NT on 2016/6/5.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context, String name){
        super(context,name,null,1);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override//首次创建数据库的时候调用 一般可以把建库建表的操作放在这
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists Addresstb(_id integer primary key autoincrement,name text not null,cellphone text not null,homephone text,companyphone text,email text,company text,cellgroup text);");
        db.execSQL("create table if not exists Grouptb(_id integer primary key autoincrement,cellgroup text not null);");
        ContentValues values = new ContentValues();
        values.put("cellgroup","家庭");
        db.insert("Grouptb",null,values);
        values.put("cellgroup","同事");
        db.insert("Grouptb",null,values);
        values.put("cellgroup","好友");
        db.insert("Grouptb",null,values);
    }

    @Override//当数据库版本发生变化的时候 会自动执行
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
