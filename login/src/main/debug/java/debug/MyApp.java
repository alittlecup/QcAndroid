package debug;

import android.app.Activity;
import android.app.Application;
import cn.qingchengfit.login.LoginDepModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.saascommon.constant.Configs;
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
    Timber.plant(new Timber.DebugTree());
    DaggerAppComponent.builder()
      .loginDepModel(new LoginDepModel(this,new BaseRouter(),new QcRestRepository(this, Configs.Server,"qc-test")))
      .build().inject(this);
    QcRouteUtil.init(this);
    ToastUtils.init(this);

    );
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
