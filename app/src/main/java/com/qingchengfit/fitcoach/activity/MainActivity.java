package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.bean.OpenDrawer;
import com.qingchengfit.fitcoach.fragment.MyHomeFragment;
import com.qingchengfit.fitcoach.fragment.XWalkFragment;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.umeng.analytics.MobclickAgent;

import org.xwalk.core.XWalkPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.subscriptions.CompositeSubscription;

//import javax.inject.Inject;

public class MainActivity extends BaseAcitivity implements Callback<QcResponse> {

    ////    @Bind(R.id.float_btn)
//    FloatingActionButton mFloatBtn;
    FragmentManager mFragmentManager;
    @Bind(R.id.main_drawerlayout)
    DrawerLayout mainDrawerlayout;
    @Bind(R.id.main_fraglayout)
    FrameLayout mainFraglayout;
    @Bind(R.id.drawer_headerview)
    RelativeLayout drawerHeaderview;
//    @Bind(R.id.main_navi)
//    NavigationView mainNavi;

    private CompositeSubscription _subscriptions;

    //    @Inject RxBus rxBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ANIMATABLE_XWALK_VIEW preference key MUST be set before XWalkView creation.
        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, true);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        XWalkFragment xWalkFragment = new XWalkFragment();
        mFragmentManager.beginTransaction().replace(R.id.main_fraglayout, xWalkFragment).commit();
        _subscriptions = new CompositeSubscription();
        RxBus.getBus().toObserverable().subscribe(o -> {
            if (o instanceof OpenDrawer) {
                mainDrawerlayout.openDrawer(Gravity.LEFT);
            }
        });
//        View view = View.inflate(this,R.layout.drawer_header,null);
//        mainNavi.addHeaderView(view);
//        view.setOnClickListener(view1 ->
//                mFragmentManager.beginTransaction()
//                        .replace(R.id.main_fraglayout,new MyHomeFragment())
//                        .commit()
//        );
    }

    @OnClick(R.id.drawer_headerview)
    public void onHeadClick(){
        mainDrawerlayout.closeDrawers();
        mFragmentManager.beginTransaction()
                .replace(R.id.main_fraglayout,new MyHomeFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, false);
    }

    /**
     * http返回
     *
     * @param qcResponse http回调
     * @param response   返回头信息
     */
    @Override
    public void success(QcResponse qcResponse, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {

    }
}
