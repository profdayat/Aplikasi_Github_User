package com.dayat.submission3.database;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static final class UserColumns implements BaseColumns {
        public static final String TABLE_FAVORITE_USER = "favorite";
        public static final String COLUMN_NAME_AVATAR_URL = "avatar_url";
        public static final String COLUMN_NAME_USERNAME = "username";
    }
}

