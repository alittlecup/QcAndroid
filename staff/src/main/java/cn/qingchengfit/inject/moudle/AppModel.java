package cn.qingchengfit.inject.moudle;

import android.app.Application;
import cn.qingchengfit.card.StaffCardRouters;
import cn.qingchengfit.card.network.CardRealModel;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.gym.gymconfig.IGymConfigModel;
import cn.qingchengfit.login.ILoginModel;
import cn.qingchengfit.model.CardModel;
import cn.qingchengfit.model.ExportModel;
import cn.qingchengfit.model.LoginModel;
import cn.qingchengfit.model.UserModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.apis.CourseModel;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.repository.IExportModel;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.saasbase.routers.billImpl;
import cn.qingchengfit.saasbase.routers.commonImpl;
import cn.qingchengfit.saasbase.routers.courseImpl;
import cn.qingchengfit.saasbase.routers.exportImpl;
import cn.qingchengfit.saasbase.routers.staffImpl;
import cn.qingchengfit.saasbase.routers.userImpl;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.user.IUserModel;
import cn.qingchengfit.saascommon.model.QcDbManager;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.staff.routers.StaffRouterCenter;
import cn.qingchengfit.staff.routers.dianpingImpl;
import cn.qingchengfit.staff.routers.settingImpl;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.CardStudentRouters;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.model.db.QCDbManagerImpl;
import cn.qingchengfit.staffkit.repository.GymConfigModel;
import cn.qingchengfit.staffkit.repository.SerPermissionImpl;
import cn.qingchengfit.staffkit.staff.StaffModel;
import cn.qingchengfit.staffkit.student.network.StudentModel;
import cn.qingchengfit.student.routers.StudentRouterCenter;
import cn.qingchengfit.student.routers.studentImpl;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import dagger.Module;
import dagger.Provides;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/11/19 2015.
 */
@Module public class AppModel {
  private IWXAPI api;
  private App app;
  private SerPermissionImpl serPermission;
  private LoginStatus loginStatus;
  private GymWrapper gymWrapper;
  private BaseRouter router;
  private QcRestRepository qcrestRepository;
  private QcDbManager qcDbManager;
  private CardRealModel cardModel;
  private IExportModel exportModel;
  private ICourseModel courseModel;
  private SaasbaseRouterCenter saasbaseRouterCenter;
  private IGymConfigModel gymConfigModel;
  private ILoginModel loginModel;
  private IUserModel userModel;

  public AppModel() {
  }

  public AppModel(App app, SerPermissionImpl serPermission, LoginStatus loginStatus,
      GymWrapper gymWrapper, BaseRouter router, QcRestRepository qcrestRepository,
      QcDbManager qcDbManager) {
    this.app = app;
    this.serPermission = serPermission;
    this.loginStatus = loginStatus;
    this.gymWrapper = gymWrapper;
    this.router = router;
    this.qcrestRepository = qcrestRepository;
    this.qcDbManager = qcDbManager;
    cardModel = new CardModel(qcrestRepository, gymWrapper, loginStatus);
    exportModel = new ExportModel(qcrestRepository, gymWrapper, loginStatus);
    courseModel = new CourseModel(qcrestRepository, gymWrapper, loginStatus);
    gymConfigModel = new GymConfigModel(gymWrapper, loginStatus, qcrestRepository);
    loginModel = new LoginModel(gymWrapper, loginStatus, qcrestRepository, qcDbManager);
    userModel = new UserModel(gymWrapper, loginStatus, qcrestRepository);
    api = WXAPIFactory.createWXAPI(app, app.getString(R.string.wechat_code));
    this.saasbaseRouterCenter = new SaasbaseRouterCenter().registe(new exportImpl())
        .registe(new StaffCardRouters())
        .registe(new staffImpl())
        .registe(new commonImpl())
        .registe(new courseImpl())
        .registe(new CardStudentRouters())
        .registe(new userImpl())
        .registe(new billImpl());
  }

  @Provides IWXAPI provideWx() {
    return api;
  }

  @Provides ILoginModel providerLogin() {
    return loginModel;
  }

  @Provides IUserModel providerUserModel() {
    return userModel;
  }

  @Provides IGymConfigModel provideGymConfigModel() {
    return gymConfigModel;
  }

  @Provides App provideApplicationContext() {
    return app;
  }

  @Provides Application provideApplication() {
    return app;
  }

  @Provides QCDbManagerImpl provoideQcDbManager() {
    return new QCDbManagerImpl(app);
  }

  @Provides SerPermissionImpl provoidePermission() {
    return serPermission;
  }

  @Provides LoginStatus providerLoginStatus() {
    return loginStatus;
  }

  @Provides GymWrapper provideGym() {
    return gymWrapper;
  }

  @Provides BaseRouter providerBaserouter() {
    return router;
  }

  @Provides QcRestRepository providerQcRepository() {
    return qcrestRepository;
  }

  @Provides QcDbManager providerQcDBManager() {
    return qcDbManager;
  }



  @Provides CardRealModel provideCardRealModel() {
    return cardModel;
  }

  @Provides IStaffModel providerStaffModel() {
    return new StaffModel(qcrestRepository, gymWrapper, loginStatus);
  }

  @Provides SaasbaseRouterCenter provideRc() {
    return saasbaseRouterCenter;
  }

  @Provides ICourseModel provideCourseApi() {
    return courseModel;
  }

  @Provides static StudentRouterCenter provideStudentRouterCenter() {
    return new StudentRouterCenter().registe(new studentImpl());
  }

  @Provides static StaffRouterCenter provideStaffRouterCenter() {
    return new StaffRouterCenter().registe(new dianpingImpl()).registe(new settingImpl()).registe(new cn.qingchengfit.staff.routers.staffImpl());
  }

  @Provides public IPermissionModel providePermissModel() {
    return new IPermissionModel() {
      @Override public boolean check(String permission) {
        return qcDbManager.check(gymWrapper.shop_id(), permission);
      }

      @Override public boolean checkAllGym(String permission) {
        return qcDbManager.checkAll(permission);
      }

      @Override public boolean checkInBrand(String permission) {
        return qcDbManager.checkAtLeastOne(permission);
      }

      @Override public boolean check(String permission, List<String> shopids) {
        return qcDbManager.checkMuti(permission, shopids);
      }
    };
  }

  @Provides IStudentModel provideStudent() {
    return new StudentModel(qcrestRepository, gymWrapper, loginStatus);
  }

  @Provides IExportModel provideExportModel() {
    return exportModel;
  }

  @Provides ICardModel provideCardmodel() {
    return cardModel;
  }
}
