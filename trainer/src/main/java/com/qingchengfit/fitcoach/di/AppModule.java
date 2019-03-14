package com.qingchengfit.fitcoach.di;

import android.app.Application;
import cn.qingcheng.gym.gymconfig.IGymConfigModel;
import cn.qingchengfit.bean.CurentPermissions;
import cn.qingchengfit.card.TrainerCardRouters;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.login.ILoginModel;
import cn.qingchengfit.model.CourseModel;
import cn.qingchengfit.model.GymConfigModel;
import cn.qingchengfit.model.LoginModel;
import cn.qingchengfit.model.UserModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.saasbase.routers.billImpl;
import cn.qingchengfit.saasbase.routers.commonImpl;
import cn.qingchengfit.saasbase.routers.exportImpl;
import cn.qingchengfit.saasbase.routers.staffImpl;
import cn.qingchengfit.saasbase.routers.userImpl;
import cn.qingchengfit.saasbase.user.IUserModel;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.routers.StudentRouterCenter;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.card.TrainerStudentImpl;
import com.qingchengfit.fitcoach.routers.CourseRouter;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import dagger.Module;
import dagger.Provides;
import java.util.List;

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
 * Created by Paper on 2017/4/17.
 */
@Module public class AppModule {
  private final IWXAPI api;
  private final UserModel userModel;
  private LoginStatus loginStatus;
  private GymWrapper gymWrapper;
  private App app;
  private BaseRouter router;
  private QcRestRepository qcRestRepository;
  private ICourseModel courseModel;
  private IGymConfigModel gymConfigModel;
  private ILoginModel loginModel;
  private QcDbManager db;
  private SaasbaseRouterCenter saasbaseRouterCenter =
      new SaasbaseRouterCenter().registe(new exportImpl())
          .registe(new staffImpl())
          .registe(new commonImpl())
          .registe(new TrainerCardRouters())
          .registe(new CourseRouter())
          .registe(new userImpl())
          .registe(new billImpl());

  private IPermissionModel permissionModel = new IPermissionModel() {
    @Override public boolean check(String permission) {
      if (permission.contains("manage_costs")) return true;
      return CurentPermissions.newInstance().queryPermission(permission);
    }

    @Override public boolean checkAllGym(String permission) {
      return CurentPermissions.newInstance().queryPermission(permission);
    }

    @Override public boolean checkInBrand(String permission) {
      return CurentPermissions.newInstance().queryPermission(permission);
    }

    @Override public boolean check(String permission, List<String> shopids) {
      return CurentPermissions.newInstance().queryPermission(permission);
    }
  };

  private AppModule(Builder builder) {
    loginStatus = builder.loginStatus;
    gymWrapper = builder.gymWrapper;
    app = builder.app;
    router = builder.router;
    db = builder.db;
    qcRestRepository = new QcRestRepository(app, cn.qingchengfit.saascommon.constant.Configs.getServerByBuildFLAVOR(BuildConfig.FLAVOR), app.getString(R.string.oem_tag));
    courseModel = new CourseModel(qcRestRepository, gymWrapper, loginStatus);
    gymConfigModel = new GymConfigModel(gymWrapper, loginStatus, qcRestRepository);
    loginModel = new LoginModel(gymWrapper, loginStatus, qcRestRepository);
    userModel = new UserModel(gymWrapper, loginStatus, qcRestRepository);
    api = WXAPIFactory.createWXAPI(app, app.getString(R.string.wechat_code));
  }

  @Provides IWXAPI provideWx() {
    return api;
  }

  @Provides QcDbManager provideDb() {
    return db;
  }

  @Provides ILoginModel provideLoginModel() {
    return loginModel;
  }

  @Provides IUserModel provideUserModel() {
    return userModel;
  }

  @Provides IGymConfigModel provideGymConfig() {
    return gymConfigModel;
  }

  @Provides ICourseModel provideCourseModel() {
    return courseModel;
  }

  @Provides IPermissionModel providerPermission() {
    return permissionModel;
  }

  @Provides LoginStatus providerLoginStatus() {
    return loginStatus;
  }

  @Provides GymWrapper provideGym() {
    return gymWrapper;
  }

  @Provides App provideApplicationContext() {
    return app;
  }

  @Provides Application provideApplication() {
    return app;
  }

  @Provides public BaseRouter provideRouter() {
    return router;
  }


  @Provides public QcRestRepository provideQcRepository() {
    return qcRestRepository;
  }

  @Provides SaasbaseRouterCenter provideRc() {
    return saasbaseRouterCenter;
  }



    @Provides StudentRouterCenter provideStudentRouterCenter() {
    return new StudentRouterCenter().registe(new TrainerStudentImpl());
  }

  public static final class Builder {
    private LoginStatus loginStatus;
    private GymWrapper gymWrapper;
    private App app;
    private BaseRouter router;
    private QcDbManager db;

    public Builder() {
    }

    public Builder loginStatus(LoginStatus val) {
      loginStatus = val;
      return this;
    }

    public Builder db(QcDbManager val) {
      db = val;
      return this;
    }

    public Builder gymWrapper(GymWrapper val) {
      gymWrapper = val;
      return this;
    }

    public Builder app(App val) {
      app = val;
      return this;
    }

    public Builder router(BaseRouter val) {
      router = val;
      return this;
    }


    public AppModule build() {
      return new AppModule(this);
    }
  }
}
