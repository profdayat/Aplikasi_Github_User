package com.dayat.githubfavoriteuser.service;

import com.dayat.githubfavoriteuser.model.DetailUserModel;
import com.dayat.githubfavoriteuser.model.UserItems;
import com.dayat.githubfavoriteuser.model.UserObjectData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://api.github.com/";
    String TOKEN = "9c639df48ffd339a7bc86cf457f1a56ded63f86d";

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
