package com.example.ngan.basicproject.v.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;


import com.example.ngan.basicproject.common.Utils;
import com.example.ngan.basicproject.network.OnPostResponseListener;
import com.example.ngan.basicproject.network.OnResponseListener;
import com.example.ngan.basicproject.network.services.ApiResponseCode;
import com.example.ngan.basicproject.network.services.ApiTask;
import com.example.ngan.basicproject.v.activity.MainActivity;

import butterknife.Unbinder;


public abstract class BaseFragment
        extends Fragment
        implements OnResponseListener {

    /**
     * fragment's activity
     */
    MainActivity mActivity;
    /**
     * Check Fragment is Resumed
     */
    private boolean mIsResumed = false;
    /**
     * Dialog Button Bar
     */
    private static View mDialogButtonsBar;
    /**
     * Resource Unbinder
     */
    Unbinder mUnbinder;
    /**
     * The Dialog
     */
    private Dialog mDialog;

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        mActivity.setCurrentFragment(this);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        mIsResumed = true;
    }

    /**
     * Show Loading Dialog
     *
     * @param message int
     */
    protected void showLoading(@StringRes int message) {
        mDialog = Utils.showProgressDialog(mActivity, message);
    }

    /**
     * Hide Current Loading Dialog
     */
    protected void hideLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * Get Resumed Value
     *
     * @return TRUE if Fragment is resumed
     */
    public boolean isFragmentResumed() {
        return mIsResumed;
    }

    /**
     * Receive Response
     *
     * @param task   ApiTask
     * @param status int
     * @return True if Task is Finished and Execute next Task
     */
    @Override
    public final boolean onResponse(ApiTask task, int status) {
        if (this instanceof OnPostResponseListener) {
            OnPostResponseListener listener = (OnPostResponseListener) this;

            if (listener.willProcess(task, status)) {
                return listener.onPostResponse(task, status);
            }
        }

        return onProcessResponse(task, status);
    }

    /**
     * Will Process on Child Classes
     *
     * @param task   ApiTask
     * @param status int
     * @return True if Response is Process on Child Classes
     */
    @Override
    public boolean willProcess(ApiTask task, int status) {
        return status == ApiResponseCode.SUCCESS ||
                status != ApiResponseCode.CANNOT_CONNECT_TO_SERVER;
    }

    /**
     * Local Process Response
     *
     * @param task   ApiTask
     * @param status int
     * @return True if Task is Finished and Execute next Task
     */
    private boolean onProcessResponse(ApiTask task, int status) {
        hideLoading();

        switch (status) {
            case ApiResponseCode.CANNOT_CONNECT_TO_SERVER:
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * Retry When Not Connect to Server
     *
     * @param task ApiTask
     */
 /*   private void showRetryDialog(final ApiTask task) {
        Utils.showConfirmDialog(mActivity,
                R.string.dialog_title_attention, R.string.dialog_message_no_connect,
                R.string.dialog_message_no_connect_no, null,
                R.string.dialog_message_no_connect_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        task.execute();
                    }
                }, this);
    }*/


    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        mIsResumed = false;
        super.onPause();
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        if (mDialogButtonsBar != null && mDialogButtonsBar.getParent() != null) {
            ((ViewGroup) mDialogButtonsBar.getParent()).removeView(mDialogButtonsBar);
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

}
