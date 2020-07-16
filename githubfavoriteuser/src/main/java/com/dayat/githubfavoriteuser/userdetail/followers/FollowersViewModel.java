package com.dayat.githubfavoriteuser.userdetail.followers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dayat.githubfavoriteuser.model.UserItems;
import com.dayat.githubfavoriteuser.service.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowersViewModel extends ViewModel {
    private MutableLiveData<List<UserItems>> listFollowers = new MutableLiveData<>();

    private MutableLiveData<String> status = new MutableLiveData<>();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private Api api = retrofit.create(Api.class);

    public void setFollowers(final String urlFollowers){

        Call<List<UserItems>> userItemsCall = api.getFollowers(urlFollowers);
        userItemsCall.enqueue(new Callback<List<UserItems>>() {
            @Override
            public void onResponse(Call<List<UserItems>> call, Response<List<UserItems>> response) {
                if (response.isSuccessful()){
                    listFollowers.setValue(response.body());
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

    LiveData<List<UserItems>> getFollowers(){
        return listFollowers;
    }
}
