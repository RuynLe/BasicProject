package com.example.ngan.basicproject.v.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ngan.basicproject.R;
import com.example.ngan.basicproject.m.eventbus.SetTitleMessage;
import com.example.ngan.basicproject.p.network.OnPostResponseListener;
import com.example.ngan.basicproject.p.network.services.ApiResponseCode;
import com.example.ngan.basicproject.p.network.services.ApiTask;
import com.example.ngan.basicproject.p.network.services.ApiTaskType;
import com.example.ngan.basicproject.p.LoginPresenter;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileScreen extends BaseFragment implements OnPostResponseListener {
    private LoginPresenter mPresenter;

    public ProfileScreen() {
        // Required empty public constructor
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p/>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LoginPresenter(mActivity, this);
        // Set Title
        EventBus.getDefault().post(new SetTitleMessage(""));
        mActivity.setTitle("Profile");
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_screen, container, false);
//        mPresenter.login("sonntt079@gmail.com", "123456");
        return rootView;
    }

    @Override
    public boolean onPostResponse(ApiTask task, int status) {
        if (status == ApiResponseCode.SUCCESS) {
            if (task.getType() == ApiTaskType.LOGIN) {
                Log.d("sssDEBUG", "OK test data from fragment");

            }
        }
        return true;
    }

    @Override
    public boolean willProcess(ApiTask task, int status) {
        return super.willProcess(task, status);
    }


}
