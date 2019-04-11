package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import cn.qingchengfit.views.activity.BaseActivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.NotificationFragment;

public class NotificationActivity extends BaseActivity{
    public static final String TAG = NotificationActivity.class.getName();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.notification_layout, NotificationFragment.newInstance(getIntent().getStringExtra("type")))
            .commit();
    }
}
