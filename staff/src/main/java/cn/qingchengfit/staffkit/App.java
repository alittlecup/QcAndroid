package cn.qingchengfit.staffkit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.commpont.AppComponent;
import cn.qingchengfit.inject.model.CardTypeWrapper;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.inject.model.StaffWrapper;

import cn.qingchengfit.inject.moudle.AppModel;
import cn.qingchengfit.inject.moudle.CardTypeWrapperModule;
import cn.qingchengfit.inject.moudle.RealcardModule;

import cn.qingchengfit.inject.moudle.StaffWrapperMoudle;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.model.db.QCDbManagerImpl;
import cn.qingchengfit.staffkit.repository.SerPermissionImpl;
import cn.qingchengfit.staffkit.train.moudle.TrainIds;
import cn.qingchengfit.staffkit.train.moudle.TrainMoudle;
import cn.qingchengfit.staffkit.views.custom.CitiesData;
import cn.qingchengfit.utils.CrashHandler;
import cn.qingchengfit.utils.FileUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.weex.utils.WeexDelegate;
import com.google.gson.Gson;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.smtt.sdk.QbSdk;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import cn.qingchengfit.inject.commpont.DaggerAppComponent;

import dagger.android.support.HasSupportFragmentInjector;
import im.fir.sdk.FIR;
import java.util.HashMap;
import javax.inject.Inject;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import timber.log.Timber;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'l
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * Created by Paper on 15/11/19 2015.
 */
public class App extends Application implements HasActivityInjector, HasSupportFragmentInjector {
  public static HashMap<String, Object> caches = new HashMap<>();
  public static Context context;
  public static String staffId;
  public static boolean gCanReload = false;

  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  @Inject DispatchingAndroidInjector<android.support.v4.app.Fragment> dispatchingFragmentInjector;
  private AppComponent appCompoent;


  @Override public void onCreate() {

    super.onCreate();

    WeexDelegate.initWXSDKEngine(this);
    QC.init(this);
    QC.enableDebug(true);
    QC.enableVerboseLog(true);
    try {
      FIR.init(this);
    } catch (Exception e) {

    }
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    if (MsfSdkUtils.isMainProcess(this)) {
      LogUtil.d("MyApplication", "main process");

      // 设置离线推送监听器
      TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
        @Override public void handleNotification(TIMOfflinePushNotification notification) {
          LogUtil.d("MyApplication", "recv offline push");

          // 这里的doNotify是ImSDK内置的通知栏提醒，应用也可以选择自己利用回调参数notification来构造自己的通知栏提醒
          notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
        }
      });
    }
    context = this;
    ToastUtils.init(this);

    initInjcet();
    CrashHandler.getInstance().init(this);
    RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
      @Override public void handleError(Throwable e) {
        if (e != null) {
          e.printStackTrace();
        }
      }
    });
    Configs.APP_ID = getString(R.string.wechat_code);
    Gson gson = new Gson();
    CitiesData citiesData =
        gson.fromJson(FileUtils.getJsonFromAssert("cities.json", context), CitiesData.class);
    caches.put("cityData", citiesData);

    App.staffId = PreferenceUtils.getPrefString(this, Configs.PREFER_WORK_ID, "");

    initX5();
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  private void initX5() {
    // 初始化腾讯X5内核
    QbSdk.initX5Environment(this, null);
  }

  public void initInjcet() {
    LoginStatus.Builder lb = new LoginStatus.Builder();
    String staffid = PreferenceUtils.getPrefString(this, Configs.PREFER_WORK_ID, "");
    String staffname = PreferenceUtils.getPrefString(this, Configs.PREFER_WORK_NAME, "");
    String session = PreferenceUtils.getPrefString(this, Configs.PREFER_SESSION, "");
    String user_id = PreferenceUtils.getPrefString(this, Configs.PREFER_USER_ID, "");
    Staff staff = new Staff();
    staff.setId(staffid);
    staff.setUsername(staffname);
    lb.loginUser(staff);
    lb.session(session);
    lb.userId(user_id);
    appCompoent = DaggerAppComponent.builder()
        .appModel(new AppModel(this, new SerPermissionImpl(this), lb.build(),
            new GymWrapper.Builder().build(), new BaseRouter(),
            new QcRestRepository(this, Configs.getServerByBuildFLAVOR(BuildConfig.FLAVOR),
                getString(R.string.oem_tag)), new QCDbManagerImpl(this)))
        .staffWrapperMoudle(new StaffWrapperMoudle(new StaffWrapper()))
        .cardTypeWrapperModule(new CardTypeWrapperModule(new CardTypeWrapper(null)))
        .realcardModule(new RealcardModule(new RealcardWrapper(null)))
        .trainMoudle(new TrainMoudle(new TrainIds.Builder().build()))
        .build();
    appCompoent.inject(this);
  }

  @Override public DispatchingAndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  @Override
  public DispatchingAndroidInjector<android.support.v4.app.Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }
}
