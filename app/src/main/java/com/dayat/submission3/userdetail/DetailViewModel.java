package com.dayat.submission3.userdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dayat.submission3.model.DetailUserModel;
import com.dayat.submission3.service.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailViewModel extends ViewModel {
    private MutableLiveData<DetailUserModel> listDetailUser = new MutableLiveData<>();

    private MutableLiveData<String> status = new MutableLiveData<>();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private Api api = retrofit.create(Api.class);

    void setUser(String user){

        Call<DetailUserModel> detailUserModelCall = api.getDetailUser(user);
        detailUserModelCall.enqueue(new Callback<DetailUserModel>() {
            @Override
            public void onResponse(Call<DetailUserModel> call, Response<DetailUserModel> response) {
                if (response.isSuccessful()){
                    listDetailUser.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DetailUserModel> call, Throwable t) {

            }
        });
    }

    LiveData<String> getStatus(){
        return status;
    }

    LiveData<DetailUserModel> getUsers() {
        return listDetailUser;
    }
}
