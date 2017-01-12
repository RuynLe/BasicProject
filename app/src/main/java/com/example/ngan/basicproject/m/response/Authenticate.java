package com.example.ngan.basicproject.m.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 03-Jan-17.
 * Email: leruyn@gmail.com
 */

public class Authenticate {
    @SerializedName("token")
    @Expose
    private String token;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
