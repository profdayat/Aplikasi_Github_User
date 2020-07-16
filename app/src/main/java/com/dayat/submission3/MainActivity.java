package com.dayat.submission3;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dayat.submission3.adapter.UserAdapter;
import com.dayat.submission3.model.UserItems;
import com.dayat.submission3.userdetail.DetailUserActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnItemClickCallback {

    private UserAdapter adapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private MainViewModel mainViewModel;

    private ArrayList<UserItems> userItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.searchView);
        searchView.onActionViewExpanded();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(userItems,this);
        adapter.notifyDataSetChanged();

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        setSearch();

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.getStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if( status != null){
                    loadFailed(status);
                    showLoading(false);
                }
            }
        });

        mainViewModel.getUsers().observe(this, new Observer<List<UserItems>>() {
            @Override
            public void onChanged(List<UserItems> userItems) {
                if (userItems != null){
                    adapter.setData((ArrayList<UserItems>) userItems);
                    showLoading(false);
                }
            }
        });
    }

    private void setSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) return true;
                showLoading(true);
                mainViewModel.setUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reminder_settings:
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.change_language_settings:
                Intent intentSetting = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intentSetting);
                return true;
            case R.id.action_favorite:
                Intent intentFavorite = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(intentFavorite);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void loadFailed(String status) {
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.title_failed);
        alertDialogBuilder
                .setMessage(status)
                .setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showSelectedUser(int position) {
        int id = userItems.get(position).getId();
        String name = userItems.get(position).getLogin();
        String photo = userItems.get(position).getAvatar_url();

        UserItems userItem = new UserItems();
        userItem.setId(id);
        userItem.setLogin(name);
        userItem.setAvatar_url(photo);

        Intent intent = new Intent(MainActivity.this, DetailUserActivity.class);
        intent.putExtra(DetailUserActivity.EXTRA_USER, userItem);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position) {
        showSelectedUser(position);
    }
}
