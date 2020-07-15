package com.dayat.submission3.userdetail.followers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dayat.submission3.R;
import com.dayat.submission3.adapter.UserAdapter;
import com.dayat.submission3.model.UserItems;
import com.dayat.submission3.userdetail.DetailUserActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FollowersFragment extends Fragment implements UserAdapter.OnItemClickCallback {
    ArrayList<UserItems> userItems = new ArrayList<>();

    private final String FOLLOWERS_KEY = "followers_key";
    private String followers;

    private UserAdapter userAdapter;
    private ProgressBar progressBar;

    public FollowersFragment() {
    }

    @SuppressLint("ValidFragment")
    public FollowersFragment(String followers) {
        this.followers = followers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.followers_fragment, container, false);

        if (savedInstanceState != null) {
            followers = savedInstanceState.getString(FOLLOWERS_KEY);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(FOLLOWERS_KEY, followers);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.frag_user);

        FollowersViewModel followersViewModel = new ViewModelProvider(this,
                new ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel.class);
        followersViewModel.setFollowers(followers);
        followersViewModel.getFollowers().observe(getViewLifecycleOwner(), new Observer<List<UserItems>>() {
            @Override
            public void onChanged(List<UserItems> userItems) {
                if (userItems != null){
                    userAdapter.setData((ArrayList<UserItems>) userItems);
                    showLoading(false);
                }
            }
        });

        followersViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if( status != null){
                    loadFailed(status);
                    showLoading(false);
                }
            }
        });

        userAdapter = new UserAdapter(userItems, this);
        userAdapter.notifyDataSetChanged();

        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);
        showLoading(true);
    }

    private void loadFailed(String status) {
        Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.title_failed);
        alertDialogBuilder
                .setMessage(status)
                .setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
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

        Intent intent = new Intent(getContext(), DetailUserActivity.class);
        intent.putExtra(DetailUserActivity.EXTRA_USER, userItem);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position) {
        showSelectedUser(position);
    }
}
