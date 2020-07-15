package com.dayat.submission3;

import com.dayat.submission3.model.UserItems;
import com.dayat.submission3.model.UserObjectData;
import com.dayat.submission3.service.Api;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<UserItems>> listUsers = new MutableLiveData<>();

    private MutableLiveData<String> status = new MutableLiveData<>();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private Api api = retrofit.create(Api.class);

    void setUser(String user){

        Call<UserObjectData> userObjectCall = api.getUsers(user);
        userObjectCall.enqueue(new Callback<UserObjectData>() {
            @Override
            public void onResponse(Call<UserObjectData> call, Response<UserObjectData> response) {
                if (response.isSuccessful()) {
                    listUsers.setValue(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<UserObjectData> call, Throwable t) {
                status.postValue(t.getMessage());
            }
        });
   }

    LiveData<String> getStatus(){
        return status;
    }

    LiveData<List<UserItems>> getUsers() {
        return listUsers;
    }
}
