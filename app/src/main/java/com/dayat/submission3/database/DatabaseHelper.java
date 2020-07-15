package com.dayat.submission3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.COLUMN_NAME_USERNAME;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.TABLE_FAVORITE_USER;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydb1";
    private static final int USER_DB_VERSION = 1;

    DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, USER_DB_VERSION);
    }

    private static final String SQL_CREATE_TABLE_USER = String.format("CREATE TABLE %s" +
            "(%s INTEGER PRIMARY KEY, " +
            "%s TEXT NOT NULL," +
            "%s TEXT NOT NULL)", TABLE_FAVORITE_USER, _ID, COLUMN_NAME_AVATAR_URL, COLUMN_NAME_USERNAME);

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_USER);
        onCreate(db);
    }

}
