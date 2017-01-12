package com.example.ngan.basicproject.application;

import android.app.Application;

import com.example.ngan.basicproject.common.Constant;
import com.example.ngan.basicproject.p.network.services.ApiService;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 28-Nov-16.
 * Email: leruyn@gmail.com
 */

public class BaseApplication extends Application {

    /** Singleton Instance */
    private static BaseApplication mInstance;
    /** API Service Instance */
    private ApiService mService;

    /**
     * Default Constructor
     */
    public BaseApplication() {
        super();

        mInstance = this;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //creat realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //creat retrofit
        // Create Retrofit Builder
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(Constant.DOMAIN);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addConverterFactory(ScalarsConverterFactory.create());

        // Create Retrofit
        Retrofit retrofit = builder.build();
        // Set Log Enable for Retrofit
        if (Constant.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient
                    .Builder().addInterceptor(interceptor).build();
            builder.client(client);
        }
        // Create Service
        mService = retrofit.create(ApiService.class);

    }

    // Initialize the facebook sdk and then callback manager will handle the login responses.

    /**
     * Get Singleton Instance
     * @return BaseApplication
     */
    public static BaseApplication getInstance() {
        return mInstance;
    }

    /**
     * Get API Service
     * @return ApiService
     */
    public ApiService getService() {
        return mService;
    }
}
