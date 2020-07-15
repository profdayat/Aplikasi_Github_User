package com.dayat.submission3.service;

import com.dayat.submission3.model.UserObjectData;
import com.dayat.submission3.model.DetailUserModel;
import com.dayat.submission3.model.UserItems;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://api.github.com/";
    String TOKEN = "f4ef29cbe0fa8689e93949e3da87b451a7d22f87";

    @GET("search/users")
    Call<UserObjectData> getUsers(@Query("q") String username);

    @Headers({
            "Authorization: token " + TOKEN,
            "User-Agent: request"
    })
    @GET("users/{username}")
    Call<DetailUserModel> getDetailUser(@Path("username") String username);

    @Headers({
            "Authorization: token " + TOKEN,
            "User-Agent: request"
    })
    @GET("users/{username}/followers")
    Call<List<UserItems>> getFollowers(@Path("username") String username);

    @Headers({
            "Authorization: token " + TOKEN,
            "User-Agent: request"
    })
    @GET("users/{username}/following")
    Call<List<UserItems>> getFollowing(@Path("username") String username);
}
