package com.example.ngan.basicproject.p.network.services;

import com.example.ngan.basicproject.m.response.Authenticate;
import com.example.ngan.basicproject.m.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 28-Nov-16.
 * Email: leruyn@gmail.com
 */

public interface ApiService {
    @POST("/api/v1/login/driver")
    Call<LoginResponse> login(@Query("phone") String phone);
    @POST("/api/v1/authenticate")
    Call<Authenticate> authenticate(@Query("phone") String phone,
                                    @Query("code") String code,
                                    @Query("deviceToken") String deviceToken,
                                    @Query("deviceType") String deviceType);
}
