package com.dayat.submission3.helper;

import android.database.Cursor;

import com.dayat.submission3.model.UserItems;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.COLUMN_NAME_USERNAME;

public class MappingHelper {

    public static ArrayList<UserItems> mapCursorToArrayList(Cursor usersCursor) {

        ArrayList<UserItems> userItems = new ArrayList<>();

        while (usersCursor.moveToNext()) {
            int id = usersCursor.getInt(usersCursor.getColumnIndexOrThrow(_ID));
            String avatar_url = usersCursor.getString(usersCursor.getColumnIndexOrThrow(COLUMN_NAME_AVATAR_URL));
            String username = usersCursor.getString(usersCursor.getColumnIndexOrThrow(COLUMN_NAME_USERNAME));
            userItems.add(new UserItems(id, username, avatar_url));
        }

        return userItems;
    }
}
