package debug;

import android.app.Activity;
import android.app.Application;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.saascommon.BuildConfig;
import cn.qingchengfit.utils.ToastUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;
import timber.log.Timber;

public class MyApp extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  public static Application INSTANCE;

  // 数据接收的 URL
  final String SA_SERVER_URL =
      "http://qingchengfit.cloud.sensorsdata.cn:8006/sa?token=2f79f21494c6f970";
  // 配置分发的 URL
  final String SA_CONFIGURE_URL =
      "http://qingchengfit.cloud.sensorsdata.cn:8006/config?project=default";

  @Override public void onCreate() {
    super.onCreate();
    INSTANCE = this;
    DaggerAppComponent.create().inject(this);
    QcRouteUtil.init(this);
    ToastUtils.init(this);
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    //初始化神策
    SensorsDataAPI.sharedInstance(this,                               // 传入 Context
        SA_SERVER_URL,                      // 数据接收的 URL
        SA_CONFIGURE_URL,                   // 配置分发的 URL
        BuildConfig.DEBUG ? SensorsDataAPI.DebugMode.DEBUG_ONLY
            : SensorsDataAPI.DebugMode.DEBUG_OFF);
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
