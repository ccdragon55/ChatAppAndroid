package com.example.text.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chatroom.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;

    // 单例模式
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db); // 创建表
        addIndexes(db);   // 添加索引
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        alterTables(db); // 修改表结构
    }

    private void createTables(SQLiteDatabase db) {
        for (String sql : Tables.ADD_TABLES) {
            db.execSQL(sql);
        }
    }

    private void addIndexes(SQLiteDatabase db) {
        for (String sql : Tables.ADD_INDEX) {
            db.execSQL(sql);
        }
    }

    private void alterTables(SQLiteDatabase db) {
        for (Tables.TableAlter alter : Tables.ALTER_TABLES) {
            if (!isColumnExists(db, alter.tableName, alter.columnName)) {
                db.execSQL(alter.sql);
            }
        }
    }

    private boolean isColumnExists(SQLiteDatabase db, String table, String column) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + table + ")", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    if (column.equalsIgnoreCase(name)) {
                        return true;
                    }
                }
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }
}