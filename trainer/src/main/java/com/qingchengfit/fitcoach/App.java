package com.qingchengfit.fitcoach;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import cn.qingchengfit.db.QCDbManagerImpl;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.component.DiskLruCache;
import com.qingchengfit.fitcoach.di.AppComponent;
import com.qingchengfit.fitcoach.di.AppModule;
import com.qingchengfit.fitcoach.di.DaggerAppComponent;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.Coach;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import im.fir.sdk.FIR;
import javax.inject.Inject;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import timber.log.Timber;

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
 * Created by Paper on 15/7/29 2015.
 */
public class App extends Application implements HasActivityInjector, HasSupportFragmentInjector {
  public static Context AppContex;
  public static boolean canXwalk;
  public static boolean gMainAlive = false;
  public static User gUser;
  public static int coachid;
  public static boolean gCanReload = false;
  public static DiskLruCache diskLruCache;
  // 数据接收的 URL
  final String SA_SERVER_URL =
    "http://qingchengfit.cloud.sensorsdata.cn:8006/sa?token=2f79f21494c6f970";
  // 配置分发的 URL
  final String SA_CONFIGURE_URL =
    "http://qingchengfit.cloud.sensorsdata.cn:8006/config?project=default";
  // Debug 模式选项
  //   SensorsDataAPI.DebugMode.DEBUG_OFF - 关闭 Debug 模式
  //   SensorsDataAPI.DebugMode.DEBUG_ONLY - 打开 Debug 模式，校验数据，但不进行数据导入
  //   SensorsDataAPI.DebugMode.DEBUG_AND_TRACK - 打开 Debug 模式，校验数据，并将数据导入到 Sensors Analytics 中
  // 注意！请不要在正式发布的 App 中使用 Debug 模式！
  final SensorsDataAPI.DebugMode SA_DEBUG_MODE = SensorsDataAPI.DebugMode.DEBUG_OFF;
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  @Inject DispatchingAndroidInjector<android.support.v4.app.Fragment> dispatchingFragmentInjector;
  private String KEY_DEX2_SHA1 = "XXDSDSFHALJFDKLASF";
  //private ApplicationLike tinkerApplicationLike;

  public static void setgUser(User ser) {
    gUser = ser;
  }

  public static String getCurProcessName(Context context) {
    try {
      int pid = android.os.Process.myPid();
      ActivityManager mActivityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
      for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
        if (appProcess.pid == pid) {
          return appProcess.processName;
        }
      }
    } catch (Exception e) {
      // ignore
    }
    return null;
  }

  //
  @Override public void onCreate() {
    super.onCreate();
    //tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
    ////开始检查是否有补丁，这里配置的是每隔访问3小时服务器是否有更新。
    //if (tinkerApplicationLike != null) {
    //    TinkerPatch.init(tinkerApplicationLike)
    //        .reflectPatchLibrary()
    //        .fetchPatchUpdate(true)
    //        .setPatchRollbackOnScreenOff(true)
    //        .setPatchRestartOnSrceenOff(true);
    //    TinkerPatch.with().fetchPatchUpdate(true);
    //}
    try {
      FIR.init(this);
    } catch (Exception e) {

    }
    AppContex = getApplicationContext();
    if (!BuildConfig.DEBUG) CrashHandler.getInstance().init(this);
    ToastUtils.init(this);
    if (BuildConfig.FLAVOR.equalsIgnoreCase("dev")){
      Timber.plant(new Timber.DebugTree());
    }
    //初始化神策
    SensorsDataAPI.sharedInstance(this,                               // 传入 Context
      SA_SERVER_URL,                      // 数据接收的 URL
      SA_CONFIGURE_URL,                   // 配置分发的 URL
      SA_DEBUG_MODE);
    try {

      SensorsDataAPI.sharedInstance(this).enableAutoTrack();
    } catch (Exception e) {

    }

    String u = PreferenceUtils.getPrefString(this, "user_info", "");
    if (!TextUtils.isEmpty(u)) {
      gUser = new Gson().fromJson(u, User.class);
    }

    Configs.APP_ID = getString(R.string.wechat_code);
    String id = PreferenceUtils.getPrefString(this, "coach", "");
    String session_id = PreferenceUtils.getPrefString(this, "session_id", "");

    if (TextUtils.isEmpty(id)) {
    } else {
      Coach coach = new Gson().fromJson(id, Coach.class);
      App.coachid = Integer.parseInt(coach.id);
    }
    AppComponent appComponent = DaggerAppComponent.builder()
      .appModule(new AppModule.Builder().app(this)
        .gymWrapper(new GymWrapper.Builder().build())
        .db(new QCDbManagerImpl(this))
        .restRepository(new RestRepository(new QcCloudClient()))
        .router(new BaseRouter())
        .loginStatus(new LoginStatus.Builder().loginUser(
          gUser == null ? new Staff() : Staff.formatFromUser(gUser, App.coachid + ""))
          .session(session_id)
          .userId(gUser == null ? "" : gUser.getId())
          .build())
        .build())
      .build();
    appComponent.inject(this);

    ToastUtils.init(this);
    RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
      @Override public void handleError(Throwable e) {
        if (e != null) {
          LogUtil.e("rxError:" + e.getMessage() + e.getCause());
          e.printStackTrace();
        }
      }
    });
  }

  private void setupWebView() {
    try {
      System.loadLibrary("xwalkcore");
      canXwalk = false;
    } catch (Error e) {
      canXwalk = false;
    }
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  public void finishActivity() {

    //杀死该应用进程
    android.os.Process.killProcess(android.os.Process.myPid());
  }

  @Override public DispatchingAndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  @Override public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }
}
