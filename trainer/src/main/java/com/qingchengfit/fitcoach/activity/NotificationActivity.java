package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.views.activity.BaseActivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.NotiFragmentAdater;
import cn.qingchengfit.bean.EventLatestNoti;
import cn.qingchengfit.bean.EventNotiFresh;
import com.qingchengfit.fitcoach.fragment.FragmentCallBack;
import com.qingchengfit.fitcoach.fragment.NotificationFragment;
import rx.Observable;

public class NotificationActivity extends BaseActivity
    implements FragmentCallBack, WebActivityInterface {
    public static final String TAG = NotificationActivity.class.getName();

    FragmentManager mFragmentManager;
    private Observable<EventNotiFresh> mOb;
    private NotiFragmentAdater adater;
    private Observable<EventLatestNoti> mLatestOb;
    private long mLatestEvent;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.notification_layout, NotificationFragment.newInstance(getIntent().getStringExtra("type")))
            .commit();
    }

    @Override public void onFragmentChange(Fragment fragment) {
    }

    @Override public void onToolbarMenu(@MenuRes int menu, int icon, String title) {

    }

    @Override public void onToolbarClickListener(Toolbar.OnMenuItemClickListener listener) {
    }

    @Override public void hindToolbar() {

    }

    @Override public void showToolbar() {

    }

    @Override public void ShowLoading(String content) {

    }

    @Override public void hideLoading() {

    }

    @Override public void fixCount() {

    }

    @Override public void onfinish() {

    }

    @Override protected void onDestroy() {
        RxBus.getBus().unregister(EventNotiFresh.class.getName(), mOb);
        RxBus.getBus().unregister(EventLatestNoti.class.getName(), mLatestOb);
        super.onDestroy();
    }
}
