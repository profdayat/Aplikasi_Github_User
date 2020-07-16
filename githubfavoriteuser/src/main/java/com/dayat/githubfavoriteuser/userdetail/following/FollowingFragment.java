package com.dayat.githubfavoriteuser.userdetail.following;

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

import com.dayat.githubfavoriteuser.R;
import com.dayat.githubfavoriteuser.adapter.UserAdapter;
import com.dayat.githubfavoriteuser.model.UserItems;
import com.dayat.githubfavoriteuser.userdetail.DetailUserActivity;

import java.util.ArrayList;
import java.util.Objects;

public class FollowingFragment extends Fragment implements UserAdapter.OnItemClickCallback {
    ArrayList<UserItems> userItems = new ArrayList<>();

    private final String FOLLOWING_KEY = "following_key";
    private String following;

    private UserAdapter userAdapter;
    private ProgressBar progressBar;

    public FollowingFragment() {
    }

    @SuppressLint("ValidFragment")
    public FollowingFragment(String following) {
        this.following = following;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.following_fragment, container, false);

        if (savedInstanceState != null) {
            following = savedInstanceState.getString(FOLLOWING_KEY);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(FOLLOWING_KEY, following);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = view.findViewById(R.id.frag_user);

        FollowingViewModel followingViewModel = new ViewModelProvider(this,
                new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);
        followingViewModel.setFollowing(following);
        followingViewModel.getFollowing().observe(getViewLifecycleOwner(), new Observer<ArrayList<UserItems>>() {
            @Override
            public void onChanged(ArrayList<UserItems> userItems) {
                if (userItems != null){
                    userAdapter.setData(userItems);
                    showLoading(false);
                }
            }
        });

        followingViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
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
