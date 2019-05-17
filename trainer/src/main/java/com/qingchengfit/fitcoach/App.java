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
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  @Inject DispatchingAndroidInjector<android.support.v4.app.Fragment> dispatchingFragmentInjector;
  private String KEY_DEX2_SHA1 = "XXDSDSFHALJFDKLASF";

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
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    initDebug();
    initBaseUser();
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
   * 初始化Debug环境
   */
  void initDebug() {
    ToastUtils.init(this);
  }

  /**
   * 初始化gUser
   */
  private void initBaseUser() {
    String u = PreferenceUtils.getPrefString(this, "user_info", "");
    if (!TextUtils.isEmpty(u)) {
      gUser = new Gson().fromJson(u, User.class);
    }
  }



  void initInject() {
    AppComponent appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule.Builder().app(this)
            .gymWrapper(new GymWrapper.Builder().build())
            .db(new QCDbManagerImpl(this))
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
