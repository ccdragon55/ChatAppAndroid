package com.example.text.utils;

import static android.provider.BaseColumns._ID;
import static com.example.text.utils.UserSettingContract.UserSettingEntry.COLUMN_CONTACT_NO_READ;
import static com.example.text.utils.UserSettingContract.UserSettingEntry.COLUMN_EMAIL;
import static com.example.text.utils.UserSettingContract.UserSettingEntry.COLUMN_SERVER_PORT;
import static com.example.text.utils.UserSettingContract.UserSettingEntry.COLUMN_SYS_SETTING;
import static com.example.text.utils.UserSettingContract.UserSettingEntry.COLUMN_USER_ID;
import static com.example.text.utils.UserSettingContract.UserSettingEntry.TABLE_NAME;

import android.provider.BaseColumns;

public final class UserSettingContract {
    private UserSettingContract() {}

    public static class UserSettingEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_setting";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_SYS_SETTING = "sys_setting";
        public static final String COLUMN_CONTACT_NO_READ = "contact_no_read";
        public static final String COLUMN_SERVER_PORT = "server_port";
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_USER_ID + " TEXT UNIQUE," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_SYS_SETTING + " TEXT," +
                    COLUMN_CONTACT_NO_READ + " INTEGER DEFAULT 0," +
                    COLUMN_SERVER_PORT + " INTEGER)";
}
