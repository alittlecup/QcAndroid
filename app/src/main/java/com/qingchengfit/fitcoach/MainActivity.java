package com.qingchengfit.fitcoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.fragment.XWalkFragment;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcResponse;

import org.xwalk.core.XWalkPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import javax.inject.Inject;

public class MainActivity extends  BaseAcitivity implements Callback<QcResponse>{

    @Bind(R.id.float_btn)
    FloatingActionButton mFloatBtn;
    FragmentManager mFragmentManager;
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
        mFragmentManager.beginTransaction().replace(R.id.main_fraglayout,xWalkFragment).commit();
        startActivity(new Intent(this, LoginActivity.class));
//        mWebview.load("http://www.baidu.com", null);
//        getAppCompent().inject(this);
//        getAppCompent().getRxBus().hasObservers();

    }

    @OnClick(R.id.float_btn)
    public void onFloatClick(){
        LogUtil.i("onclick:");
        QcCloudClient.getApi().qcCloudServer
                .getTest();
//        PhoneFuncUtils.initContactList(this);
//        PhoneFuncUtils.insertCalendar(this);
//        PhoneFuncUtils.queryCalender(this);
//        PhoneFuncUtils.queryEvent(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, false);
    }

    /**
     * http返回
     * @param qcResponse
     * @param response
     */
    @Override
    public void success(QcResponse qcResponse, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {

    }
}
