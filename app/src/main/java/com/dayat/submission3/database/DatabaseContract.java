package com.dayat.submission3.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.dayat.submission3";
    private static final String SCHEME = "content";

    public static final class UserColumns implements BaseColumns {
        public static final String TABLE_FAVORITE_USER = "favorite";
        public static final String COLUMN_NAME_AVATAR_URL = "avatar_url";
        public static final String COLUMN_NAME_USERNAME = "username";

        // untuk membuat URI content://com.dayat.submission3/favorite
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE_USER)
                .build();
    }
}

