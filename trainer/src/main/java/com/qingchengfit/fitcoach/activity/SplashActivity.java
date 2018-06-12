package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.network.HttpThrowable;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.saasbase.login.LoginActivity;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.ImagesAdapter;
import com.qingchengfit.fitcoach.component.CircleIndicator;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCoachServiceResponse;
import com.qingchengfit.fitcoach.http.bean.ResponseResult;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.tencent.smtt.sdk.QbSdk;
import io.reactivex.Maybe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
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
public class SplashActivity extends BaseActivity {

	ViewPager splashViewpager;
	CircleIndicator splashIndicator;
  List<View> imageViews = new ArrayList<>();
	RelativeLayout mainLoading;
  @Inject LoginStatus loginStatus;
  @Inject RepoCoachServiceImpl repoCoachService;
  //@BindView(R.id.animation_view) LottieAnimationView animationView;
	ImageView imgGif;
  private int[] mSplashImg = new int[] {
      R.drawable.help1, R.drawable.help2, R.drawable.help3, R.drawable.help4, R.drawable.help5,
  };
  private String[] mColors = new String[] {
      "#55b37f", "#5595b3", "#b38855", "#675Eb1", "#9e74b0"
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
      //结束你的activity
      finish();
      return;
    }
    setContentView(R.layout.activity_splash);
    splashViewpager = (ViewPager) findViewById(R.id.splash_viewpager);
    splashIndicator = (CircleIndicator) findViewById(R.id.splash_indicator);
    mainLoading = (RelativeLayout) findViewById(R.id.main_loading);
    imgGif = (ImageView) findViewById(R.id.img_gif);
    findViewById(R.id.splash_login_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        OnBtnClick(v);
      }
    });
    findViewById(R.id.splash_registe_btn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        OnBtnClick(v);
      }
    });

    initX5();

    Glide.with(this)
        .load(R.mipmap.qc_logo)
        .dontAnimate()
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into((new GlideDrawableImageViewTarget(imgGif, 1)));

    PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
        getString(R.string.baidu_api_release));

    String u = PreferenceUtils.getPrefString(this, "user_info", "");
    String id = PreferenceUtils.getPrefString(this, cn.qingchengfit.saasbase.constant.Configs.PREFER_COACH_ID, "");
    if (!TextUtils.isEmpty(u) && !TextUtils.isEmpty(id) && !TextUtils.equals("0",id)) {
            /*
                已登录跳转
             */
            App.coachid = Integer.parseInt(id);
      User gUser = new Gson().fromJson(u, User.class);


      //Staff curCoach = new Gson().fromJson(id,Staff.class);
      String session_id = PreferenceUtils.getPrefString(this, "session_id", "");

      loginStatus.setLoginUser(Staff.formatFromUser(gUser, id));
      loginStatus.setSession(session_id);
      loginStatus.setUserId(gUser.getId());
      SensorsDataAPI.sharedInstance(getApplicationContext()).login(loginStatus.getUserId());
      try {
        JSONObject properties = new JSONObject();
        properties.put("qc_app_name", "Trainer");
        properties.put("qc_user_id", gUser.id);
        SensorsDataAPI.sharedInstance(getApplicationContext()).registerSuperProperties(properties);
      } catch (JSONException e) {
        e.printStackTrace();
      }

      Observable.just("")
          .delay(1500, TimeUnit.MILLISECONDS)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.newThread())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(s1 -> {
            runOnUiThread(() -> {
              if (PreferenceUtils.getPrefString(this, "session_id", null) != null) {

                //获取用户拥有的系统
                QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<QcCoachServiceResponse>() {
                      @Override public void onCompleted() {

                      }

                      @Override public void onError(Throwable e) {
                        ToastUtils.show("服务器错误，请稍后再试");
                        SplashActivity.this.finish();
                      }

                      @Override public void onNext(QcCoachServiceResponse qcCoachServiceResponse) {
                        if (qcCoachServiceResponse.status == ResponseResult.SUCCESS) {
                          if (qcCoachServiceResponse.data == null
                              || qcCoachServiceResponse.data.services == null
                              || qcCoachServiceResponse.data.services.size() == 0) {
                            repoCoachService.deleteAllServices();
                            Intent toMain = new Intent(SplashActivity.this, Main2Activity.class);
                            startActivity(toMain);
                            SplashActivity.this.finish();
                          } else {
                            PreferenceUtils.setPrefString(App.AppContex, App.coachid + "systems",
                                new Gson().toJson(qcCoachServiceResponse));
                            repoCoachService.createServices(qcCoachServiceResponse.data.services);
                            Intent toMain = new Intent(SplashActivity.this, Main2Activity.class);
                            startActivity(toMain);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_hold);
                            SplashActivity.this.finish();
                          }
                        } else if (qcCoachServiceResponse.error_code.equalsIgnoreCase(
                            ResponseResult.error_no_login)) {
                          logout();
                        }
                      }
                    });
              } else {
                goSplashViewpager();
                ViewCompat.animate(mainLoading)
                    .alpha(0.1f)
                    .setDuration(1000)
                    .withLayer()
                    .setListener(new ViewPropertyAnimatorListener() {
                      @Override public void onAnimationStart(View view) {

                      }

                      @Override public void onAnimationEnd(View view) {

                        mainLoading.setVisibility(View.GONE);
                      }

                      @Override public void onAnimationCancel(View view) {

                      }
                    })
                    .start();
              }
            });
          },new HttpThrowable());
    } else {
            /*
             *未登录 直接进入主页
             */
      Maybe.just("")
        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
        .delay(1,TimeUnit.SECONDS)
        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
        .subscribe(s -> {
          QcRestRepository.clearSession(this);
          Intent toMain = new Intent(SplashActivity.this, Main2Activity.class);
          startActivity(toMain);
          overridePendingTransition(R.anim.slide_right_in, R.anim.slide_hold);
          SplashActivity.this.finish();
        },new HttpThrowable());

    }
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  private void initX5() {
    // 初始化腾讯X5内核
    QbSdk.initX5Environment(this, null);
  }

  public void goSplashViewpager() {
    LogUtil.e("oem:" + getString(R.string.oem_tag));
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

  @UiThread public void goLogin(int registe) {
    Intent toLogin = new Intent(this, LoginActivity.class);
    toLogin.putExtra("isRegiste", registe==1);
    startActivity(toLogin);
    this.finish();
  }

 public void OnBtnClick(View v) {
    if (v.getId() == R.id.splash_registe_btn) {
      goLogin(1);
    } else {
      goLogin(0);
    }
  }

  public void logout() {
    PreferenceUtils.setPrefBoolean(SplashActivity.this, "hasPushId", false);
    PreferenceUtils.setPrefString(App.AppContex, "session_id", null);
    PushManager.stopWork(App.AppContex);
    PreferenceUtils.setPrefBoolean(this, "first", true);
    Intent logout = new Intent(this, SplashActivity.class);
    logout.putExtra("isRegiste", false);
    startActivity(logout);
    this.finish();
  }
}
