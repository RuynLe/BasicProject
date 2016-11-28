package com.example.ngan.basicproject.p;

import android.content.Context;

import com.example.ngan.basicproject.application.BaseApplication;
import com.example.ngan.basicproject.m.response.BaseResponse;
import com.example.ngan.basicproject.network.services.ApiResponseCallback;
import com.example.ngan.basicproject.network.services.ApiResponseCode;
import com.example.ngan.basicproject.network.services.ApiService;
import com.example.ngan.basicproject.network.services.ApiTask;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Copyright HoanVu Solutions
 * Created by Le Ruyn on 28-Nov-16.
 * Email: leruyn@gmail.com
 */

abstract class BasePresenter implements ApiResponseCallback {
    static Map<String, String> mHeaders;
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
        if (mHeaders == null) {
            loadDeviceInfo();
        }
    }

    /**
     * Load Device Info
     */
    private void loadDeviceInfo() {
        mHeaders = new HashMap<>();
        mHeaders.put("X-DEVICE-ID", "123456");
        mHeaders.put("X-OS-TYPE", "IOS");
        mHeaders.put("X-OS-VERSION", "9.0");
        mHeaders.put("X-API-ID", "abc");
        mHeaders.put("X-API-KEY", "123");
        mHeaders.put("X-APP-VERSION", "1.0");
    }

    /**
     * Parse Response
     *
     * @param response BaseResponse
     */
    protected void parseResponse(BaseResponse response) {
        /*try {
            Method method = this.getClass().getMethod(
                    "processResponse", response.getData().getClass());
            method.invoke(this, response.getData());
        } catch (Throwable e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }*/

        Object data = response.getData();

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
            status = ((BaseResponse) response.body()).getCode();

            if (status == ApiResponseCode.SUCCESS) {
                parseResponse((BaseResponse) response.body());
            }
        }

        return onPostResponse(task, status);
    }
    /**
     * Create Login Request
     *
     * @param email String
     * @param pass  String
     * @return Call
     */
    protected Call createLoginRequest(String email, String pass) {
        Map<String, RequestBody> data = new HashMap<>();
        data.put("username", RequestBody.create(MediaType.parse("text/plain"), email));
        data.put("password", RequestBody.create(MediaType.parse("text/plain"), pass));

        return mService.login(mHeaders, data);
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
