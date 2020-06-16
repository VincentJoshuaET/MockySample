package com.example.form;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitInterface {
    @GET
    Call<User> getUser(@Url String url);
}
