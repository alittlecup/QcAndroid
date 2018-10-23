package com.qingchengfit.fitcoach;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import cn.qingchengfit.db.QCDbManagerImpl;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.router.QC;
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
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import im.fir.sdk.FIR;
import javax.inject.Inject;
import org.json.JSONObject;
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
  final SensorsDataAPI.DebugMode SA_DEBUG_MODE =BuildConfig.DEBUG?SensorsDataAPI.DebugMode.DEBUG_ONLY:
  SensorsDataAPI.DebugMode.DEBUG_OFF;
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

    try {
      FIR.init(this);
    } catch (Exception e) {

    }
    AppContex = getApplicationContext();
    Configs.APP_ID = getString(R.string.wechat_code);
    initDebug();
    initBaseUser();
    initSensor();
    initInject();
    QC.init(this);
    QC.enableDebug(true);
    QC.enableVerboseLog(true);

    RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
      @Override public void handleError(Throwable e) {
        if (e != null) {
          LogUtil.e("rxError:" + e.getMessage() + e.getCause());
          e.printStackTrace();
        }
      }
    });
  }

  /**
   *  初始化Debug环境
   */
  void initDebug(){
    ToastUtils.init(this);
    if (BuildConfig.DEBUG || BuildConfig.FLAVOR.equalsIgnoreCase("dev")) {
      Timber.plant(new Timber.DebugTree());
      String ip = cn.qingchengfit.utils.PreferenceUtils.getPrefString(this, "debug_ip", Configs.Server);
      Configs.Server = ip;
    }
  }

  /**
   * 初始化gUser
   */
  private void initBaseUser(){
    String u = PreferenceUtils.getPrefString(this, "user_info", "");
    if (!TextUtils.isEmpty(u)) {
      gUser = new Gson().fromJson(u, User.class);
    }
  }

  /**
   * 初始化神策
   */
  private void initSensor(){
    SensorsDataAPI.sharedInstance(this,                               // 传入 Context
      SA_SERVER_URL,                      // 数据接收的 URL
      SA_CONFIGURE_URL,                   // 配置分发的 URL
      SA_DEBUG_MODE);
    try {
      SensorsDataAPI.sharedInstance(this).enableAutoTrack();
      SensorsDataAPI.sharedInstance().trackFragmentAppViewScreen();

      JSONObject properties = new JSONObject();
      properties.put("qc_app_name", "Trainer");
      SensorsDataAPI.sharedInstance(this).registerSuperProperties(properties);
      JSONObject properties2 = new JSONObject();
      //这里示例 DownloadChannel 记录下载商店的渠道(下载渠道)。如果需要多个字段来标记渠道包，请按业务实际需要添加。
      properties2.put("DownloadChannel", "qc_official");
      //记录激活事件、渠道追踪，这里激活事件取名为 AppInstall。
      SensorsDataAPI.sharedInstance().trackInstallation("AppInstall", properties2);
    } catch (Exception e) {

    }
  }

  void initInject(){
    AppComponent appComponent = DaggerAppComponent.builder()
      .appModule(new AppModule.Builder().app(this)
        .gymWrapper(new GymWrapper.Builder().build())
        .db(new QCDbManagerImpl(this))
        .restRepository(new RestRepository(new QcCloudClient()))
        .router(new BaseRouter())
        .loginStatus(new LoginStatus.Builder().loginUser(
          gUser == null ? new Staff() : Staff.formatFromUser(gUser, App.coachid + ""))
          .session(QcRestRepository.getSession(this))
          .userId(gUser == null ? "" : gUser.getId())
          .build())
        .build())
      .build();
    appComponent.inject(this);
  }



  private void setupWebView() {
    try {
      System.loadLibrary("xwalkcore");
      canXwalk = false;
    } catch (Error e) {
      canXwalk = false;
    }
  }


  public static void finishActivity() {
    //杀死该应用进程
    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(0);
  }

  @Override public DispatchingAndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  @Override public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }
}
