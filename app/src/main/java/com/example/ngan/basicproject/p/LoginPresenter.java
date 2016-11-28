package com.example.ngan.basicproject.p;

import android.content.Context;

import com.example.ngan.basicproject.network.OnResponseListener;
import com.example.ngan.basicproject.network.services.ApiTask;
import com.example.ngan.basicproject.network.services.ApiTaskType;

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
     *
     * @param email    String
     * @param password String
     */
    public void login(final String email, final String password) {
        ApiTask.execute(new ApiTask.OnCreateCallCallback() {
            @Override
            public Call onCreateCall() {
                return createLoginRequest(email, password);
            }
        }, ApiTaskType.LOGIN, this);
    }

    @Override
    public boolean onPostResponse(ApiTask task, int status) {
        return mListener.onResponse(task, status);
    }

}
