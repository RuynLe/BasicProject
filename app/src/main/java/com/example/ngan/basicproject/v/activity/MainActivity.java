package com.example.ngan.basicproject.v.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ngan.basicproject.R;
import com.example.ngan.basicproject.common.Utils;
import com.example.ngan.basicproject.m.eventbus.SetTitleMessage;
import com.example.ngan.basicproject.network.OnPostResponseListener;
import com.example.ngan.basicproject.network.services.ApiResponseCode;
import com.example.ngan.basicproject.network.services.ApiTask;
import com.example.ngan.basicproject.network.services.ApiTaskType;
import com.example.ngan.basicproject.p.LoginPresenter;
import com.example.ngan.basicproject.v.fragment.ProfileScreen;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements OnPostResponseListener {
    LoginPresenter mPresenter;
    @BindView(R.id.main_top_bar_title)
    TextView mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        // Bind all listeners
        mUnbinder = ButterKnife.bind(this);
        //initialize LoginPresenter
        mPresenter = new LoginPresenter(this, this);

        // Register Events
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Utils.addFragment(R.id.main_content_container, new ProfileScreen(), "", this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register Events
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * Set Title top bar
     *
     * @param message String
     */
    @Subscribe
    public void setTitle(SetTitleMessage message) {
        Log.d("sssDEBUG", message.getmTitleText());
        mTitles.setText(message.getmTitleText());
    }

    @Override
    public boolean onPostResponse(ApiTask task, int status) {
        if (status == ApiResponseCode.SUCCESS) {
            if (task.getType() == ApiTaskType.LOGIN) {
                Log.d("sssDEBUG", "SUCCESS test data from activity");
            }
        }
        return true;
    }

    /**
     * Destroy all fragments and loaders.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
