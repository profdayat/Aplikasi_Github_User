package com.dayat.submission3.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.dayat.submission3.database.UserHelper;

import java.util.Objects;

import static com.dayat.submission3.database.DatabaseContract.AUTHORITY;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.CONTENT_URI;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.TABLE_FAVORITE_USER;

public class FavoritUserProvider extends ContentProvider {

    private static final int FAVORITE = 1;
    private static final int FAVORITE_ID = 2;
    private UserHelper userHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
   Uri matcher untuk mempermudah identifier dengan menggunakan integer
   misal
   uri com.dayat.submission3 dicocokan dengan integer 1
   uri com.dayat.submission3/# dicocokan dengan integer 2
    */
    static {
        // content://com.dayat.submission3/favorite
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_USER, FAVORITE);
        // content://com.dayat.submission3/note/id
        sUriMatcher.addURI(AUTHORITY,
                TABLE_FAVORITE_USER + "/#",
                FAVORITE_ID);
    }
    
    public FavoritUserProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        userHelper = UserHelper.getInstance(getContext());
        userHelper.open();
        return true;
    }

    /*
    Method queryAll digunakan ketika ingin menjalankan queryAll Select
    Return cursor
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                cursor = userHelper.queryAll();
                break;
            case FAVORITE_ID:
                cursor = userHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long added;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                added = userHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                updated = userHelper.update(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                deleted = userHelper.deleteById(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }
}
