package cn.qingchengfit.staffkit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
import cn.qingchengfit.card.StaffCardActivity;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.commpont.AppComponent;
import cn.qingchengfit.inject.commpont.DaggerAppComponent;
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
import cn.qingchengfit.saasbase.gymconfig.GymConfigAcitivty;
import cn.qingchengfit.sass.course.StaffCourseActivity;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.model.db.QCDbManagerImpl;
import cn.qingchengfit.staffkit.repository.SerPermissionImpl;
import cn.qingchengfit.staffkit.staff.StaffStaffActivity;
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
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.smtt.sdk.QbSdk;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import im.fir.sdk.FIR;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import lib_zxing.activity.ZXingLibrary;
import org.json.JSONException;
import org.json.JSONObject;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;


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
  private AppComponent appCompoent;
  //private ApplicationLike tinkerApplicationLike;

  public AppComponent getAppCompoent() {
    return appCompoent;
  }

  @Override public void onCreate() {

    super.onCreate();
    //
    //tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
    ////开始检查是否有补丁，这里配置的是每隔访问3小时服务器是否有更新。
    //if (tinkerApplicationLike != null) {
    //    TinkerPatch.init(tinkerApplicationLike)
    //        .reflectPatchLibrary()
    //        .setPatchRollbackOnScreenOff(true)
    //        .setPatchRestartOnSrceenOff(true);
    //    TinkerPatch.with().fetchPatchUpdate(true);
    //}
    WeexDelegate.initWXSDKEngine(this);
    QC.init(this);
    QC.enableDebug(true);
    QC.enableVerboseLog(true);
    try {
      FIR.init(this);
    } catch (Exception e) {

    }
    ZXingLibrary.initDisplayOpinion(this);
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
    //if (!BuildConfig.DEBUG) {
    //初始化神策
    SensorsDataAPI.sharedInstance(this,                               // 传入 Context
        SA_SERVER_URL,                      // 数据接收的 URL
        SA_CONFIGURE_URL,                   // 配置分发的 URL
        BuildConfig.DEBUG ? SensorsDataAPI.DebugMode.DEBUG_ONLY
            : SensorsDataAPI.DebugMode.DEBUG_OFF);

    try {
      SensorsDataAPI.sharedInstance(this).enableAutoTrack();
      //初始化 SDK 之后，开启自动采集 Fragment 页面浏览事件
      SensorsDataAPI.sharedInstance().trackFragmentAppViewScreen();
      List<Class<?>> classList = new ArrayList<>();
      classList.add(StaffCourseActivity.class);
      classList.add(StaffStaffActivity.class);
      classList.add(StaffCardActivity.class);
      classList.add(GymConfigAcitivty.class);
      SensorsDataAPI.sharedInstance(this).ignoreAutoTrackActivities(classList);
      JSONObject properties = new JSONObject();
      properties.put("qc_app_name", "Staff");
      SensorsDataAPI.sharedInstance(this).registerSuperProperties(properties);

      JSONObject properties2 = new JSONObject();
      //这里示例 DownloadChannel 记录下载商店的渠道(下载渠道)。如果需要多个字段来标记渠道包，请按业务实际需要添加。
      properties2.put("DownloadChannel", "qc_official");
      //记录激活事件、渠道追踪，这里激活事件取名为 AppInstall。
      SensorsDataAPI.sharedInstance().trackInstallation("AppInstall", properties2);
    } catch (JSONException e) {
      LogUtil.e("hs_bug", e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
      LogUtil.e("hs_bug", e.getMessage());
    }
    //}
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
    QCDbManagerImpl qcDbManager = new QCDbManagerImpl(this);
    appCompoent = DaggerAppComponent.builder()
        .appModel(new AppModel(this, new SerPermissionImpl(this), lb.build(),
            new GymWrapper.Builder().build(), new BaseRouter(),
            new QcRestRepository(this, Configs.Server, getString(R.string.oem_tag)),
            new QCDbManagerImpl(this)))
        .staffWrapperMoudle(new StaffWrapperMoudle(new StaffWrapper()))
        .cardTypeWrapperModule(new CardTypeWrapperModule(new CardTypeWrapper(null)))
        .realcardModule(new RealcardModule(new RealcardWrapper(null)))
        .trainMoudle(new TrainMoudle(new TrainIds.Builder().build()))
        .build();
    //appCompoent = DaggerAppComponent.create();
    appCompoent.inject(this);
  }

  public void finishActivity() {
    //杀死该应用进程
    //android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(0);
  }

  @Override public DispatchingAndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  @Override
  public DispatchingAndroidInjector<android.support.v4.app.Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }
}
