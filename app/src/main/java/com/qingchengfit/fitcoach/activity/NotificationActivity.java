package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.FragmentCallBack;
import com.qingchengfit.fitcoach.fragment.NotificationFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationActivity extends BaseAcitivity implements FragmentCallBack, WebActivityInterface {
    public static final String TAG = NotificationActivity.class.getName();

    FragmentManager mFragmentManager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> {
            this.onBackPressed();
            overridePendingTransition(R.anim.slide_hold, R.anim.slide_right_out);
        });

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.notification_layout, new NotificationFragment())
                .commit();
    }


    @Override
    public void onFragmentChange(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(R.id.notification_layout, fragment)
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_right_out, R.anim.slide_left_in)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onToolbarMenu(@MenuRes int menu, int icon, String title) {
        toolbar.setTitle(title);
        toolbar.getMenu().clear();
        if (menu != 0)
            toolbar.inflateMenu(menu);
        else toolbar.inflateMenu(R.menu.menu_blank);
        if (icon != 0)
            toolbar.setNavigationIcon(icon);
    }

    @Override
    public void onToolbarClickListener(Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }

    @Override
    public void hindToolbar() {

    }

    @Override
    public void showToolbar() {

    }

    @Override
    public void ShowLoading(String content) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void fixCount() {

    }


    @Override
    public void onfinish() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getRefWatcher().watch(this);
    }
}
