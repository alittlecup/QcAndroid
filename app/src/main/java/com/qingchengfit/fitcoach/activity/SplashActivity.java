package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.utils.LogUtil;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImagesAdapter;
import com.qingchengfit.fitcoach.component.CircleIndicator;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCoachServiceResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/5 2015.
 */
public class SplashActivity extends BaseAcitivity {

    @BindView(R.id.splash_viewpager)
    ViewPager splashViewpager;
    @BindView(R.id.splash_indicator)
    CircleIndicator splashIndicator;

    List<View> imageViews = new ArrayList<>();
    @BindView(R.id.main_loading)
    RelativeLayout mainLoading;
    float touchX;
    private int[] mSplashImg = new int[]{
            R.drawable.help1,
            R.drawable.help2,
            R.drawable.help3,
            R.drawable.help4,
            R.drawable.help5,
    };
    private String[] mColors = new String[]{
            "#55b37f", "#5595b3", "#b38855", "#675Eb1", "#9e74b0"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //结束你的activity
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, getString(R.string.baidu_api_release));
        if (BuildConfig.DEBUG){
            String ip = PreferenceUtils.getPrefString(this,"debug_ip", "");
            if (!TextUtils.isEmpty(ip))
                Configs.Server = Configs.ServerIp = ip;
        }
        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .flatMap(s -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    return Observable.just("");
                }).subscribe(s1 -> {
            runOnUiThread(() -> {
                if (PreferenceUtils.getPrefString(this, "session_id", null) != null) {

                    //获取用户拥有的系统
                    QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<QcCoachServiceResponse>() {
                            @Override public void onCompleted() {

                            }

                            @Override public void onError(Throwable e) {

                            }

                            @Override public void onNext(QcCoachServiceResponse qcCoachServiceResponse) {
                                if (qcCoachServiceResponse.status == ResponseResult.SUCCESS) {
                                    if (qcCoachServiceResponse.data == null || qcCoachServiceResponse.data.services == null ||
                                        qcCoachServiceResponse.data.services.size() == 0) {
                                        new MaterialDialog.Builder(SplashActivity.this).canceledOnTouchOutside(false)
                                            .title("您没有场馆")
                                            .content("您可以使用拥有场馆的账号的重新登录或者为此账号创建一所场馆. ")
                                            .positiveText("创建场馆")
                                            .negativeText("重新登录")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    Intent toGym = new Intent(SplashActivity.this, GuideActivity.class);
                                                    startActivity(toGym);
                                                    SplashActivity.this.finish();
                                                }
                                            })
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    logout();
                                                }
                                            })
                                            .show();
                                    } else {
                                        PreferenceUtils.setPrefString(App.AppContex, App.coachid + "systems", new Gson().toJson(qcCoachServiceResponse));
                                        Intent toMain = new Intent(SplashActivity.this, Main2Activity.class);
                                        startActivity(toMain);
                                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_hold);
                                        SplashActivity.this.finish();
                                    }
                                } else if (qcCoachServiceResponse.error_code.equalsIgnoreCase(ResponseResult.error_no_login)) {
                                    logout();
                                }
                            }
                        });


                } else {
                    goSplashViewpager();
                    ViewCompat.animate(mainLoading).alpha(0.1f).setDuration(1000).withLayer().setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {

                        }

                        @Override
                        public void onAnimationEnd(View view) {

                            mainLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(View view) {

                        }
                    }).start();


                }
            });

        });


    }

    public void goSplashViewpager() {
        LogUtil.e("oem:"+getString(R.string.oem_tag));
        if (getString(R.string.oem_tag).contains("qingcheng")) {

            for (int i = 0; i < mSplashImg.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(mSplashImg[i]);
                imageView.setBackgroundColor(Color.parseColor(mColors[i]));
                imageViews.add(imageView);
            }
            splashViewpager.setAdapter(new ImagesAdapter(imageViews));
            splashIndicator.setViewPager(splashViewpager);
        } else {
            goLogin(0);
        }

    }


    @UiThread
    public void goLogin(int registe) {
        Intent toLogin = new Intent(this, LoginActivity.class);
        toLogin.putExtra("isRegiste", registe);
        startActivity(toLogin);
        this.finish();
    }

    @OnClick({
            R.id.splash_login_btn,
            R.id.splash_registe_btn
    })
    public void OnBtnClick(View v) {
        if (v.getId() == R.id.splash_registe_btn) {
            goLogin(1);
        } else goLogin(0);

    }
    public void logout() {
        PreferenceUtils.setPrefBoolean(SplashActivity.this, "hasPushId", false);
        PreferenceUtils.setPrefString(App.AppContex, "session_id", null);
        PushManager.stopWork(App.AppContex);
        PreferenceUtils.setPrefBoolean(this, "first", true);
        Intent logout = new Intent(this, SplashActivity.class);
        logout.putExtra("isRegiste", 0);
        startActivity(logout);
        this.finish();
    }
}
