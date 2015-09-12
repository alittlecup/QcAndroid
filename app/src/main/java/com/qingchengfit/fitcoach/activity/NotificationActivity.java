package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.NotificationFragment;

public class NotificationActivity extends AppCompatActivity {
    public static final String TAG = NotificationActivity.class.getName();

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.notification_layout, new NotificationFragment())
                .commit();

    }
}
