package com.dayat.submission3.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dayat.submission3.R;
import com.dayat.submission3.helper.MappingHelper;
import com.dayat.submission3.model.UserItems;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.dayat.submission3.database.DatabaseContract.UserColumns.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private Cursor cursor;
    private ArrayList<UserItems> userItems = new ArrayList<>();

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        if (cursor != null){
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        // querying ke database
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        assert cursor != null;
        userItems = MappingHelper.mapCursorToArrayList(cursor);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return userItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        Bitmap bmp = null;
        try {
            bmp = Glide.with(mContext)
                    .asBitmap()
                    .load(userItems.get(position).getAvatar_url())
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.imageView, bmp);
        Bundle extras = new Bundle();
        extras.putInt(FavoriteAppWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
