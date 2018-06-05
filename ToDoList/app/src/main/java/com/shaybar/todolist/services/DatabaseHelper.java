package com.shaybar.todolist.services;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.shaybar.todolist.entities.Tag;
import com.shaybar.todolist.entities.Task;

import org.chalup.microorm.MicroOrm;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo_list_db";
    public static MicroOrm uOrm = new MicroOrm();


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Create task table
        database.execSQL("CREATE TABLE " + Task.TABLE_NAME +
                "( " + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " " + Task.DESCRIPTION + " TEXT NOT NULL," +
                " " + Task.TAG + " TEXT NOT NULL," +
                " " + Task.PRIORITY + " INTEGER NOT NULL," +
                " " + Task.DUE_DATE + "INTEGER NOT NULL," +
                " " + Task.DATE + " INTEGER NOT NULL," +
                " " + Task.REPEAT_DAYS + " INTEGER NOT NULL," +
                " " + Task.DONE + " BOOLEAN NOT NULL )");
        //Create tags table
        database.execSQL("CREATE TABLE " + Tag.TABLE_NAME +
                "( " + BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                " " + Tag.NAME + " TEXT NOT NULL," +
                " " + Tag.COLOR + " TEXT NOT NULL )");

        //Default tags
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'Bills', '#8af537');");
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'Car', '#f1c312');");
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'Education', '#2993a0');");
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'Pets', '#bf142f');");
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'Work', '#fc74ba');");
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'Food', '#cb006a');");
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'Sport', '#370372');");
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'Health', '#665b77');");
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'House', '#d46cf9');");
        database.execSQL("INSERT INTO " + Tag.TABLE_NAME + " VALUES (null, 'Family', '#70776b');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }

    public static SQLiteDatabase getWritableDatabase(Context context) {
        DatabaseHelper afmDb = new DatabaseHelper(context);
        return afmDb.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase(Context context) {
        DatabaseHelper afmDb = new DatabaseHelper(context);
        return afmDb.getReadableDatabase();
    }

    public static MicroOrm getOrm() {
        return uOrm;
    }

}
