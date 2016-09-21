package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.adapter.NotiFragmentAdater;
import com.qingchengfit.fitcoach.bean.EventNotiFresh;
import com.qingchengfit.fitcoach.component.PagerSlidingTabImageStrip;
import com.qingchengfit.fitcoach.fragment.FragmentCallBack;
import com.qingchengfit.fitcoach.fragment.NotificationFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationActivity extends BaseAcitivity implements FragmentCallBack, WebActivityInterface {
    public static final String TAG = NotificationActivity.class.getName();

    FragmentManager mFragmentManager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.strip)
    PagerSlidingTabImageStrip strip;
    private Observable<EventNotiFresh> mOb;
    private NotiFragmentAdater adater;

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
        toolbar.inflateMenu(R.menu.menu_clear_noti);
        toolbar.setTitle("全部通知");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                QcCloudClient.getApi().postApi.qcClearAllNotification(App.coachid)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<QcResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(QcResponse qcResponse) {

                                for (int i = 0; i < adater.getCount(); i++) {
                                    if (adater.getItem(i) instanceof NotificationFragment) {
                                        ((NotificationFragment) adater.getItem(i)).freshData();
                                    }
                                }
                            }
                        });
                return false;
            }
        });


        mOb = RxBus.getBus().register(EventNotiFresh.class);
        mOb.debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventNotiFresh -> {
                    strip.notifyDataSetChanged();
                });
        List<Fragment> fs = new ArrayList<>();
        fs.add(NotificationFragment.newInstance());
        fs.add(NotificationFragment.newInstance());
        fs.add(NotificationFragment.newInstance());

        adater = new NotiFragmentAdater(getSupportFragmentManager(), fs);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(adater);
        strip.setViewPager(viewpager);


    }


    @Override
    public void onFragmentChange(Fragment fragment) {
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
        RxBus.getBus().unregister(EventNotiFresh.class.getName(), mOb);
        super.onDestroy();
//        App.getRefWatcher().watch(this);
    }
}
