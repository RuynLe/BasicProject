package com.example.ngan.basicproject.p;

import android.content.Context;

import com.example.ngan.basicproject.application.BaseApplication;
import com.example.ngan.basicproject.p.network.services.ApiResponseCallback;
import com.example.ngan.basicproject.p.network.services.ApiResponseCode;
import com.example.ngan.basicproject.p.network.services.ApiService;
import com.example.ngan.basicproject.p.network.services.ApiTask;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 28-Nov-16.
 * Email: leruyn@gmail.com
 */

abstract class BasePresenter implements ApiResponseCallback {
    final Context mContext;
    /**
     * The Api Service
     */
    final ApiService mService;
    /**
     * Realm Instance
     */
    final Realm mRealm;

    /**
     * Default Constructor
     *
     * @param context Context
     */
    public BasePresenter(Context context) {
        mContext = context;
        mRealm = Realm.getDefaultInstance();
        mService = BaseApplication.getInstance().getService();
    }


    /**
     * Parse Response
     *
     * @param response BaseResponse
     */
    protected void parseResponse(Response response) {
        /*try {
            Method method = this.getClass().getMethod(
                    "processResponse", response.getData().getClass());
            method.invoke(this, response.getData());
        } catch (Throwable e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }*/

        Object data = response.body();

//        if (data instanceof RegisterResponse) {
//            processResponse((RegisterResponse) data);
//        }


    }
    /**
     * Process Response
     *
     * @param response LoginResponse
     */

    /**
     * Process Response
     *
     * @param task ApiTask
     * @return True if Task is finished and do next task
     */
    @Override
    public final boolean onResponse(ApiTask task) {
        int status = ApiResponseCode.CANNOT_CONNECT_TO_SERVER;
        Response response = task.getResponse();

        if (response != null &&
                response.code() == 200) {
            status = (response.code());

            if (status == ApiResponseCode.SUCCESS) {
                parseResponse(response);
            }
        }

        return onPostResponse(task, status);
    }

    /**
     * Create Login Request
     *
     * @return Call
     */
    protected Call createLoginRequest(String phone) {

        return mService.login(phone);
    }

    protected Call getAuthenticate(String phone) {

        return mService.login(phone);
    }

    /**
     * Process Response
     *
     * @param task   ApiTask
     * @param status int
     * @return True if Task is finished and do next task
     */
    public abstract boolean onPostResponse(ApiTask task, int status);
}
