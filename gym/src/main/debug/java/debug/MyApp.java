package debug;

import android.app.Activity;
import android.app.Application;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.saascommon.BuildConfig;
import cn.qingchengfit.utils.ToastUtils;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;
import timber.log.Timber;

public class MyApp extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  public static Application INSTANCE;

  @Override public void onCreate() {
    super.onCreate();
    INSTANCE = this;
    DaggerAppComponent.create().inject(this);
    QcRouteUtil.init(this);
    ToastUtils.init(this);
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
