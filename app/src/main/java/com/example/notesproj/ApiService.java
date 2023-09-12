package com.example.notesproj;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/Note/Create")
    Call<Void> createNote(@Body JsonObject jsonObject);

    @POST("api/User/AuthroizationName")
    Call<Integer> userDataAuth();
}
