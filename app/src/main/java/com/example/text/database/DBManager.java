package com.example.text.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBManager {
    private static DBManager instance;
    private SQLiteDatabase db;
    private Map<String, Map<String, String>> globalColumnsMap = new HashMap<>();

    public static synchronized DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    public DBManager(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        initTableColumnsMap(); // 初始化列名映射
    }

    private void initTableColumnsMap() {
        globalColumnsMap.clear();
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' AND name!='sqlite_sequence'", null);
        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                Map<String, String> columnMap = new HashMap<>();
                Cursor colCursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
                if (colCursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String colName = colCursor.getString(colCursor.getColumnIndex("name"));
                        columnMap.put(toCamelCase(colName), colName);
                    } while (colCursor.moveToNext());
                }
                colCursor.close();
                globalColumnsMap.put(tableName, columnMap);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private String toCamelCase(String str) {
        StringBuilder result = new StringBuilder();
        boolean toUpper = false;
        for (char c : str.toCharArray()) {
            if (c == '_') {
                toUpper = true;
            } else {
                if (toUpper) {
                    result.append(Character.toUpperCase(c));
                    toUpper = false;
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }

    private Map<String, Object> convertToObj(Cursor cursor) {
        if (cursor == null || cursor.isAfterLast()) return null;
        Map<String, Object> obj = new HashMap<>();
        String[] colNames = cursor.getColumnNames();
        for (String col : colNames) {
            int idx = cursor.getColumnIndex(col);
            switch (cursor.getType(idx)) {
                case Cursor.FIELD_TYPE_INTEGER:
                    obj.put(toCamelCase(col), cursor.getLong(idx));
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    obj.put(toCamelCase(col), cursor.getDouble(idx));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    obj.put(toCamelCase(col), cursor.getString(idx));
                    break;
                case Cursor.FIELD_TYPE_BLOB:
                    obj.put(toCamelCase(col), cursor.getBlob(idx));
                    break;
                case Cursor.FIELD_TYPE_NULL:
                default:
                    obj.put(toCamelCase(col), null);
                    break;
            }
        }
        return obj;
    }

    public List<Map<String, Object>> queryAll(String sql, String[] params) {
        Cursor cursor = db.rawQuery(sql, params);
        List<Map<String, Object>> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(convertToObj(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Map<String, Object> queryOne(String sql, String[] params) {
        Cursor cursor = db.rawQuery(sql, params);
        Map<String, Object> result = null;
        if (cursor.moveToFirst()) {
            result = convertToObj(cursor);
        }
        cursor.close();
        return result;
    }

    public int queryCount(String sql, String[] params) {
        Cursor cursor = db.rawQuery(sql, params);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int run(String sql, Object[] params) {
        SQLiteStatement stmt = db.compileStatement(sql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                int index = i + 1;
                if (param == null) {
                    stmt.bindNull(index);
                } else if (param instanceof String) {
                    stmt.bindString(index, (String) param);
                } else if (param instanceof Integer || param instanceof Long) {
                    stmt.bindLong(index, ((Number) param).longValue());
                } else if (param instanceof Float || param instanceof Double) {
                    stmt.bindDouble(index, ((Number) param).doubleValue());
                } else if (param instanceof byte[]) {
                    stmt.bindBlob(index, (byte[]) param);
                } else {
                    stmt.bindString(index, param.toString());
                }
            }
        }
        int affected = stmt.executeUpdateDelete();
        stmt.close();
        return affected;
    }

    private int insert(String sqlOp, String tableName, Map<String, Object> data) {
        Map<String, String> columnsMap = globalColumnsMap.get(tableName);
        List<String> dbColumns = new ArrayList<>();
        List<Object> paramsList = new ArrayList<>();
        for (String key : data.keySet()) {
            if (data.get(key) != null && columnsMap.containsKey(key)) {
                dbColumns.add(columnsMap.get(key));
                paramsList.add(data.get(key));
            }
        }
        String placeholders = TextUtils.join(",", Collections.nCopies(dbColumns.size(), "?"));
        String sql = sqlOp + " " + tableName + " (" + TextUtils.join(",", dbColumns) + ") VALUES (" + placeholders + ")";
        return run(sql, paramsList.toArray());
    }

    public int insertOrReplace(String tableName, Map<String, Object> data) {
        return insert("INSERT OR REPLACE INTO", tableName, data);
    }

    public int insertOrIgnore(String tableName, Map<String, Object> data) {
        return insert("INSERT OR IGNORE INTO", tableName, data);
    }

    public int update(String tableName, Map<String, Object> data, Map<String, Object> conditions) {
        Map<String, String> columnsMap = globalColumnsMap.get(tableName);
        List<String> updates = new ArrayList<>();
        List<Object> paramsList = new ArrayList<>();
        List<String> whereClauses = new ArrayList<>();
        for (String key : data.keySet()) {
            if (data.get(key) != null && columnsMap.containsKey(key)) {
                updates.add(columnsMap.get(key) + " = ?");
                paramsList.add(data.get(key));
            }
        }
        for (String key : conditions.keySet()) {
            Object value = conditions.get(key);
            if (value != null && columnsMap.containsKey(key)) {
                whereClauses.add(columnsMap.get(key) + " = ?");
                paramsList.add(value);
            }
        }
        String sql = "UPDATE " + tableName + " SET " + TextUtils.join(",", updates);
        if (!whereClauses.isEmpty()) {
            sql += " WHERE " + TextUtils.join(" AND ", whereClauses);
        }
        return run(sql, paramsList.toArray());
    }
}




//import android.annotation.SuppressLint;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DBManager {
//    private static DBManager instance;
//    private SQLiteDatabase db;
//    private Map<String, Map<String, String>> columnMap = new HashMap<>();
//
//    public static synchronized DBManager getInstance(Context context) {
//        if (instance == null) {
//            instance = new DBManager(context);
//        }
//        return instance;
//    }
//
//    private DBManager(Context context) {
//        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
//        db = dbHelper.getWritableDatabase();
//        initColumnMap(); // 初始化列名映射
//    }
//
//    // 初始化列名映射（驼峰转换）
//    private void initColumnMap() {
//        Cursor cursor = db.rawQuery(
//                "SELECT name FROM sqlite_master WHERE type='table' AND name!='sqlite_sequence'", null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String table = cursor.getString(0);
//                Map<String, String> map = new HashMap<>();
//                Cursor colCursor = db.rawQuery("PRAGMA table_info(" + table + ")", null);
//                if (colCursor != null) {
//                    while (colCursor.moveToNext()) {
//                        @SuppressLint("Range") String colName = colCursor.getString(colCursor.getColumnIndex("name"));
//                        String camelCase = toCamelCase(colName);
//                        map.put(camelCase, colName);
//                    }
//                    colCursor.close();
//                }
//                columnMap.put(table, map);
//            }
//            cursor.close();
//        }
//    }
//
//    private String toCamelCase(String str) {
//        String[] parts = str.split("_");
//        StringBuilder camelCase = new StringBuilder(parts[0]);
//        for (int i = 1; i < parts.length; i++) {
//            camelCase.append(parts[i].substring(0, 1).toUpperCase())
//                    .append(parts[i].substring(1));
//        }
//        return camelCase.toString();
//    }
//
//    // 查询所有记录（自动转换驼峰命名）
//    public List<Map<String, Object>> queryAll(String table, String where, String[] args) {
//        List<Map<String, Object>> result = new ArrayList<>();
//        Cursor cursor = db.query(table, null, where, args, null, null, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                Map<String, Object> row = new HashMap<>();
//                for (String col : columnMap.get(table).keySet()) {
//                    String dbCol = columnMap.get(table).get(col);
//                    int index = cursor.getColumnIndex(dbCol);
//                    if (index != -1) {
//                        row.put(col, cursor.getString(index));
//                    }
//                }
//                result.add(row);
//            }
//            cursor.close();
//        }
//        return result;
//    }
//
//    // 插入或替换
//    public long insertOrReplace(String table, Map<String, Object> data) {
//        ContentValues values = new ContentValues();
//        Map<String, String> cols = columnMap.get(table);
//        for (String key : data.keySet()) {
//            if (cols.containsKey(key)) {
//                values.put(cols.get(key), data.get(key).toString());
//            }
//        }
//        return db.replace(table, null, values);
//    }
//
//    // 更新数据
//    public int update(String table, Map<String, Object> data, String where, String[] args) {
//        ContentValues values = new ContentValues();
//        Map<String, String> cols = columnMap.get(table);
//        for (String key : data.keySet()) {
//            if (cols.containsKey(key)) {
//                values.put(cols.get(key), data.get(key).toString());
//            }
//        }
//        return db.update(table, values, where, args);
//    }
//}
