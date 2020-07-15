package com.dayat.submission3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.dayat.submission3.adapter.UserAdapter;
import com.dayat.submission3.database.UserHelper;
import com.dayat.submission3.helper.MappingHelper;
import com.dayat.submission3.model.UserItems;
import com.dayat.submission3.userdetail.DetailUserActivity;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

public class FavoriteActivity extends AppCompatActivity implements UserAdapter.OnItemClickCallback, LoadUsersCallback {

    private ArrayList<UserItems> userItems = new ArrayList<>();
    private static final String EXTRA_STATE = "EXTRA_STATE";

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_favorite);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        UserHelper userHelper = UserHelper.getInstance(getApplicationContext());
        userHelper.open();

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(userItems, this);
        adapter.notifyDataSetChanged();

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            new LoadUsersAsync(userHelper, this).execute();
        } else {
            ArrayList<UserItems> userItems = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (userItems != null) {
                adapter.setData(userItems);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_change_settings:
                Intent intentSetting = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intentSetting);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoading(true);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<UserItems> userItems) {
        showLoading(false);
        if (userItems.size() > 0) {
            adapter.setData(userItems);
        } else {
            adapter.setData(new ArrayList<UserItems>());
            showSnackBarMessage(getString(R.string.favorite_empty));
        }
    }

    private static class LoadUsersAsync extends AsyncTask<Void, Void, ArrayList<UserItems>> {

        private final WeakReference<UserHelper> weakUserHelper;
        private final WeakReference<LoadUsersCallback> weakCallback;

        private LoadUsersAsync(UserHelper userHelper, LoadUsersCallback callback) {
            weakUserHelper = new WeakReference<>(userHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<UserItems> doInBackground(Void... voids) {
            Cursor dataCursor = weakUserHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<UserItems> userItems) {
            super.onPostExecute(userItems);
            weakCallback.get().postExecute(userItems);
        }
    }

    private void showSelectedUser(int position) {
        int id = userItems.get(position).getId();
        String name = userItems.get(position).getLogin();
        String photo = userItems.get(position).getAvatar_url();

        UserItems userItem = new UserItems();
        userItem.setId(id);
        userItem.setLogin(name);
        userItem.setAvatar_url(photo);

        Intent intent = new Intent(this, DetailUserActivity.class);
        intent.putExtra(DetailUserActivity.EXTRA_USER, userItem);
        startActivity(intent);

        finish();
    }

    @Override
    public void onItemClicked(int position) {
        showSelectedUser(position);
    }
}

interface LoadUsersCallback {
    void preExecute();
    void postExecute(ArrayList<UserItems> userItems);
}