package debug;

import android.app.Activity;
import android.app.Application;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.utils.ToastUtils;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;

public class MyApp extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  public static Application INSTANCE;


  @Override public void onCreate() {
    super.onCreate();
    INSTANCE = this;
    DaggerAppComponent.create().inject(this);
    QcRouteUtil.init(this);
    ToastUtils.init(this);

  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
