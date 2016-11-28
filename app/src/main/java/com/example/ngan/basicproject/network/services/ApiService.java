package com.example.ngan.basicproject.network.services;

import com.example.ngan.basicproject.m.response.BaseResponse;
import com.example.ngan.basicproject.m.response.LoginResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 28-Nov-16.
 * Email: leruyn@gmail.com
 */

public interface ApiService {
    @POST("/api/login")
    @Multipart
    Call<BaseResponse<LoginResponse>>
    login(@HeaderMap Map<String, String> headers,
          @PartMap Map<String, RequestBody> data);
}
