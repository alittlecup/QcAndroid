package cn.qingchengfit.testmodule;

import android.app.Application;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.saasbase.student.bean.FollowUpFilterModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/5/31.
 */
@Module public class TestModule {
  private LoginStatus loginStatus;
  private GymWrapper gymWrapper;
  private TestApp app;
  private BaseRouter router;
  private QcRestRepository restRepository;

  private TestModule(Builder builder) {
    loginStatus = builder.loginStatus;
    gymWrapper = builder.gymWrapper;
    app = builder.app;
    router = builder.router;
    restRepository = builder.restRepository;
  }

  @Provides public LoginStatus provideLoginStatus() {
    return loginStatus;
  }

  @Provides public GymWrapper provideGym() {
    return gymWrapper;
  }

  @Provides public Application provideApp() {
    return app;
  }

  @Provides public BaseRouter provideRouter() {
    return router;
  }

  @Provides public QcRestRepository provideRepository() {
    return restRepository;
  }

  @Provides public SaasbaseRouterCenter providesSaasbaseRouterConter() {
    return null;
  }

  @Singleton @Provides FollowUpFilterModel providesFollowUpFilterModel() {
    return new FollowUpFilterModel();
  }

  @Named("commonFilter") @Singleton @Provides StudentFilter providesStudentFilter() {
    return new StudentFilter();
  }

  @Named("topFilter") @Singleton @Provides StudentFilter providesTopStudentFilter() {
    return new StudentFilter();
  }

  @Provides public IStudentModel provideStudentModel(QcRestRepository restRepository) {
    return new StudentModel(restRepository);
  }

  public static final class Builder {
    private LoginStatus loginStatus;
    private GymWrapper gymWrapper;
    private TestApp app;
    private BaseRouter router;
    private QcRestRepository restRepository;

    public Builder() {
    }

    public Builder loginStatus(LoginStatus val) {
      loginStatus = val;
      return this;
    }

    public Builder gymWrapper(GymWrapper val) {
      gymWrapper = val;
      return this;
    }

    public Builder app(TestApp val) {
      app = val;
      return this;
    }

    public Builder router(BaseRouter val) {
      router = val;
      return this;
    }

    public Builder restRepository(QcRestRepository val) {
      restRepository = val;
      return this;
    }

    public TestModule build() {
      return new TestModule(this);
    }
  }
  //@Provides
  //public <T> Map<Class<? extends T>, Provider<AndroidInjector.Factory<? extends T>>> providerMap(){
  //    return new HashMap<Class<? extends T>, Provider<AndroidInjector.Factory<? extends T>>>();
  //}
}
