package com.dayat.submission3.userdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dayat.submission3.FavoriteActivity;
import com.dayat.submission3.R;
import com.dayat.submission3.adapter.ViewPagerAdapter;
import com.dayat.submission3.model.DetailUserModel;
import com.dayat.submission3.model.UserItems;
import com.dayat.submission3.userdetail.followers.FollowersFragment;
import com.dayat.submission3.userdetail.following.FollowingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import static android.provider.BaseColumns._ID;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.COLUMN_NAME_USERNAME;
import static com.dayat.submission3.database.DatabaseContract.UserColumns.CONTENT_URI;


public class DetailUserActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "extra_user";
    private UserItems userItems;

    private ProgressBar progressBar;
    private FloatingActionButton fabFav;
    private boolean statusFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        Toolbar toolbar = findViewById(R.id.htab_toolbar);
        ImageView imageView = findViewById(R.id.img_item_photo);
        TextView textView = findViewById(R.id.tv_nama);
        progressBar = findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_detail));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        showLoading(true);

        userItems = getIntent().getParcelableExtra(EXTRA_USER);

        assert userItems != null;
        Glide.with(this)
                .load(userItems.getAvatar_url())
                .into(imageView);
        textView.setText(userItems.getLogin());

        loadDetail(userItems.getLogin());

        fabFav = findViewById(R.id.fabFav);
        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (statusFavorite){
                    deleteFavoriteItem();
                    showSnackbarMessage(getString(R.string.delete_fav));
                }else {
                    addedFavoriteItem();
                    showSnackbarMessage(getString(R.string.add_fav));
                }
                statusFavorite = !statusFavorite;
                setStatusFavorite(statusFavorite);

            }
        });
        
        loadUserFav();
    }

    private void loadUserFav() {
        Uri uriWithId = Uri.parse(CONTENT_URI + "/" + userItems.getId());
        Cursor cursor = getContentResolver().query(uriWithId, null, null, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()) statusFavorite = true;
            cursor.close();
        }
        Log.d("TAG", "loadUserFav: " + statusFavorite);
        setStatusFavorite(statusFavorite);
    }

    private void addedFavoriteItem() {
        if (userItems != null){
            ContentValues values = new ContentValues();
            values.put(_ID, userItems.getId());
            values.put(COLUMN_NAME_AVATAR_URL, userItems.getAvatar_url());
            values.put(COLUMN_NAME_USERNAME, userItems.getLogin());
            getContentResolver().insert(CONTENT_URI, values);
        }
    }

    private void deleteFavoriteItem() {
        getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + userItems.getId()),null,null);
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(findViewById(R.id.htab_maincontent), message, Snackbar.LENGTH_SHORT).show();
    }

    private void setStatusFavorite(Boolean statusFavorite) {
        if (statusFavorite) fabFav.setImageResource(R.drawable.ic_favorite);
        else fabFav.setImageResource(R.drawable.ic_favorite_border);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
            case R.id.action_favorite:
                Intent intentFavorite = new Intent(getApplicationContext(), FavoriteActivity.class);
                startActivity(intentFavorite);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDetail(String user) {

        DetailViewModel detailViewModel = new ViewModelProvider(this, new ViewModelProvider
                .NewInstanceFactory())
                .get(DetailViewModel.class);

        detailViewModel.setUser(user);

        detailViewModel.getStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if( status != null){
                    loadFailed(status);
                    showLoading(false);
                }
            }
        });

        detailViewModel.getUsers().observe(this, new Observer<DetailUserModel>() {
            @Override
            public void onChanged(DetailUserModel detailUserModels) {
                if (detailUserModels != null){

                    setupTab(detailUserModels);
                    showLoading(false);

                }
            }
        });
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

    private void setupTab(DetailUserModel detailUserModels) {
        final ViewPager viewPager = findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager, detailUserModels);

        TabLayout tabLayout = findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager, DetailUserModel detailUserModel) {
        String userName = detailUserModel.getLogin();
        String followers = detailUserModel.getFollowers();
        String following = detailUserModel.getFollowing();

        String titleFollowers = followers + System.lineSeparator() + getString(R.string.followers);
        String titleFollowing = following + System.lineSeparator() + getString(R.string.following);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFrag(new FollowersFragment(userName), titleFollowers);
        adapter.addFrag(new FollowingFragment(userName), titleFollowing);
        viewPager.setAdapter(adapter);
    }
}
