package com.example.ngan.basicproject.p;

import android.content.Context;
import android.util.Log;

import com.example.ngan.basicproject.p.network.OnResponseListener;
import com.example.ngan.basicproject.p.network.services.ApiTask;
import com.example.ngan.basicproject.p.network.services.ApiTaskType;

import retrofit2.Call;

/**
 * Created by Hau on 2016-07-18.
 */
public class LoginPresenter extends BasePresenter {
    private OnResponseListener mListener;

    public LoginPresenter(Context context, OnResponseListener mListener) {
        super(context);
        this.mListener = mListener;
    }


    /**
     * Login
     */
    public void login(final String phone) {
        ApiTask.execute(new ApiTask.OnCreateCallCallback() {
            @Override
            public Call onCreateCall() {
                return createLoginRequest(phone);
            }
        }, ApiTaskType.LOGIN, this);
    }

    @Override
    public boolean onPostResponse(ApiTask task, int status) {
        Log.i("RLV login", status + "");
        return mListener.onResponse(task, status);
    }

}
