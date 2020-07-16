package com.dayat.githubfavoriteuser.userdetail.following;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dayat.githubfavoriteuser.model.UserItems;
import com.dayat.githubfavoriteuser.service.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowingViewModel extends ViewModel {
    private MutableLiveData<ArrayList<UserItems>> listFollowing = new MutableLiveData<>();

    private MutableLiveData<String> status = new MutableLiveData<>();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private Api api = retrofit.create(Api.class);

    public void setFollowing(final String urlFollowing){

        Call<List<UserItems>> userItemsCall = api.getFollowing(urlFollowing);
        userItemsCall.enqueue(new Callback<List<UserItems>>() {
            @Override
            public void onResponse(Call<List<UserItems>> call, Response<List<UserItems>> response) {
                if (response.isSuccessful()){
                    listFollowing.setValue((ArrayList<UserItems>) response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UserItems>> call, Throwable t) {
                status.postValue(t.getMessage());
            }
        });
    }

    LiveData<String> getStatus(){
        return status;
    }

    LiveData<ArrayList<UserItems>> getFollowing(){
        return listFollowing;
    }
}
