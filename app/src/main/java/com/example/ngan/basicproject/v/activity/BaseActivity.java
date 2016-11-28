package com.example.ngan.basicproject.v.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.example.ngan.basicproject.network.OnPostResponseListener;
import com.example.ngan.basicproject.network.OnResponseListener;
import com.example.ngan.basicproject.network.services.ApiResponseCode;
import com.example.ngan.basicproject.network.services.ApiTask;
import com.example.ngan.basicproject.v.fragment.BaseFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public abstract class BaseActivity extends AppCompatActivity implements OnResponseListener {
    /**
     * Resource Unbinder
     */
    Unbinder mUnbinder;
    /**
     * Realm Instance
     */
    Realm mRealm;

    /**
     * Application Info
     */
    ApplicationInfo mApplicationInfo;
    /**
     * Status Bar Height
     */
    int mStatusBarHeight;
    /**
     * Navigation Bar Height
     */
    int mNavigationBarHeight;
    /**
     * Current Fragment
     */
    private BaseFragment mCurrentFragment;

    /**
     * Activity is created
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if (Constant.DEBUG) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }*/
        //register event bus
        // Register Events
        // Get a Realm instance for this thread

        mRealm = Realm.getDefaultInstance();
    }

    /**
     * Setup Overlay Screen
     */
    void setupOverlayScreen() {
        // Default Values
        mStatusBarHeight = 0;
        mNavigationBarHeight = 0;

        // Get Status bar Height
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Resources resources = getResources();

            boolean hasSoftNavigationBar = hasSoftKeys();

            int statusHeightId = resources.getIdentifier(
                    "status_bar_height", "dimen", "android");
            int navigationHeightId = resources.getIdentifier(
                    "navigation_bar_height", "dimen", "android");

            if (statusHeightId > 0) {
                mStatusBarHeight = getResources().getDimensionPixelSize(statusHeightId);
            }
            if (navigationHeightId > 0 && hasSoftNavigationBar) {
                mNavigationBarHeight = resources.getDimensionPixelSize(navigationHeightId);
            }
        }
    }

    /**
     * Has Soft Navigation Bar
     *
     * @return True/False
     */
    public boolean hasSoftKeys() {
        boolean hasSoftwareKeys;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = getWindowManager().getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } else {
            boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }

        return hasSoftwareKeys;
    }

    /**
     * Show Status Bar
     */
    public void showStatusBar() {
        //if (Build.VERSION.SDK_INT < 16) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*} else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }*/
    }

    public void setKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.dermaclara",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * Hide Status Bar
     */
    public void hideStatusBar() {
        //if (Build.VERSION.SDK_INT < 16) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        /*} else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }*/
    }

    /**
     * Get Status Bar Height
     *
     * @return int
     */
    public int getStatusBarHeight() {
        return mStatusBarHeight;
    }

    /**
     * Get Navigation Bar Height
     *
     * @return int
     */
    public int getNavigationBarHeight() {
        return mNavigationBarHeight;
    }

    /**
     * Get Navigation Width
     *
     * @return width
     */
    int getNavigationWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (Math.min(metrics.widthPixels, metrics.heightPixels) * 5) / 6;
    }

    /**
     * Ser Current Fragment
     *
     * @param fragment BaseFragment
     */
    public void setCurrentFragment(BaseFragment fragment) {
        mCurrentFragment = fragment;
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
      /*  if (getMenuView() != null &&
                getMenuView().getTranslationX() == 0) {
        } else if (mCurrentFragment == null ||
                !mCurrentFragment.isVisible() ||
                !mCurrentFragment.onBackPressed()) {
            if (!getSupportFragmentManager().popBackStackImmediate()) {
                moveTaskToBack(true);
            }
        }*/
    }

    /**
     * Called to process key screen events.  You can override this to
     * intercept all key screen events before they are dispatched to the
     * window.  Be sure to call this implementation for key screen events
     * that should be handled normally.
     *
     * @param event The touch screen event.
     * @return boolean Return true if this event was consumed.
     */
 /*   @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
     *//*   if (mCurrentFragment != null && mCurrentFragment.isFragmentResumed() && mIsEnable) {
            return super.dispatchKeyEvent(event);
        } else {
            Log.i("DEBUG", "Ignore Key: " + event.getKeyCode());
        }*//*
        return true;
    }*/

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
        // hideLoading();

        switch (status) {
            case ApiResponseCode.CANNOT_CONNECT_TO_SERVER:
                //   showRetryDialog(task);
                Log.d("sss", "not connect");
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Called to process touch screen events.  You can override this to
     * intercept all touch screen events before they are dispatched to the
     * window.  Be sure to call this implementation for touch screen events
     * that should be handled normally.
     *
     * @param ev The touch screen event.
     * @return boolean Return true if this event was consumed.
     */
 /*   @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mCurrentFragment != null && mCurrentFragment.isFragmentResumed() && mIsEnable) {
            return super.dispatchTouchEvent(ev);
        } else {
            Log.i("DEBUG", "Ignore Touch: " + ev.getRawX() + "-" + ev.getRawY());
        }
        return true;
    }*/
/*
    *//**
     * Enable Activity
     *
     * @param isEnable TRUE/FALSE
     *//*
    public void setEnable(boolean isEnable) {
        mIsEnable = isEnable;
    }*/

    /**
     * Destroy all fragments and loaders.
     */
    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        super.onDestroy();
    }
}
