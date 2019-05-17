package debug;

import android.app.Activity;
import android.app.Application;
import cn.qingchengfit.router.QC;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;

public class MyApp extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
 public static Application INSTANCE;
 public static boolean isLogin=false;


  @Override public void onCreate() {
    super.onCreate();
    INSTANCE = this;
    DaggerAppComponent.create().inject(this);
    QC.init(this);
    QC.enableDebug(true);
    QC.enableVerboseLog(true);


  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
