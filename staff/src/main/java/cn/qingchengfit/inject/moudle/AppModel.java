package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.CardModel;
import cn.qingchengfit.model.ExportModel;
import cn.qingchengfit.model.SaasModelImpl;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.repository.IExportModel;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.repository.SaasModel;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.saasbase.routers.billImpl;
import cn.qingchengfit.saasbase.routers.commonImpl;
import cn.qingchengfit.saasbase.routers.courseImpl;
import cn.qingchengfit.saasbase.routers.exportImpl;
import cn.qingchengfit.saasbase.routers.gymImpl;
import cn.qingchengfit.saasbase.routers.staffImpl;
import cn.qingchengfit.saasbase.routers.studentImpl;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.card.StaffCardRouters;
import cn.qingchengfit.staffkit.model.db.QCDbManagerImpl;
import cn.qingchengfit.staffkit.repository.CourseModel;
import cn.qingchengfit.staffkit.repository.SerPermissionImpl;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import cn.qingchengfit.staffkit.staff.StaffModel;
import cn.qingchengfit.staffkit.student.network.StudentModel;
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
  private App app;
  private SerPermissionImpl serPermission;
  private LoginStatus loginStatus;
  private GymWrapper gymWrapper;
  private RestRepository restRepository;
  private BaseRouter router;
  private QcRestRepository qcrestRepository;
  private QcDbManager qcDbManager;
  private ICardModel cardModel;
  private IExportModel exportModel;
  private ICourseModel courseModel;
  private SaasbaseRouterCenter saasbaseRouterCenter;
  public AppModel() {
  }

  public AppModel(App app, SerPermissionImpl serPermission, LoginStatus loginStatus,
      GymWrapper gymWrapper, RestRepository restRepository, BaseRouter router,
      QcRestRepository qcrestRepository, QcDbManager qcDbManager) {
    this.app = app;
    this.serPermission = serPermission;
    this.loginStatus = loginStatus;
    this.gymWrapper = gymWrapper;
    this.restRepository = restRepository;
    this.router = router;
    this.qcrestRepository = qcrestRepository;
    this.qcDbManager = qcDbManager;
    cardModel = new CardModel(qcrestRepository,gymWrapper,loginStatus);
    exportModel = new ExportModel(qcrestRepository,gymWrapper,loginStatus);
    courseModel = new CourseModel(qcrestRepository,gymWrapper,loginStatus);
    this.saasbaseRouterCenter = new SaasbaseRouterCenter(new billImpl(),new StaffCardRouters(),new staffImpl(),new commonImpl(),new courseImpl(),new exportImpl(),new gymImpl(),new studentImpl());
  }

  @Provides App provideApplicationContext() {
    return app;
  }

  @Provides RestRepository provoideRepository() {
    return restRepository;
  }

  @Provides RestRepositoryV2 provoideRepositoryT() {
    return new RestRepositoryV2();
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

  @Provides QcDbManager providerQcDBManager(){
    return qcDbManager;
  }

  @Provides SaasModel providerSaasModel(){
    return new SaasModelImpl(qcrestRepository);
  }

  @Provides IStaffModel providerStaffModel(){
    return new StaffModel(qcrestRepository, gymWrapper, loginStatus);
  }

  @Provides SaasbaseRouterCenter provideRc(){
    return saasbaseRouterCenter;
  }

  @Provides ICourseModel provideCourseApi(){
    return courseModel;
  }

  @Provides public ICardModel provideCardModel(){
    return new CardModel(qcrestRepository, gymWrapper, loginStatus);
  }
  @Provides IExportModel provideExportModel(){return  exportModel;}
  @Provides public IPermissionModel providePermissModel(){
    return new IPermissionModel() {
      @Override public boolean check(String permission) {
        return true;
      }

      @Override public boolean check(String permission, List<String> shopids) {
        return true;
      }
    };
  }

  @Provides IStudentModel provideStudent(){
    return new StudentModel(qcrestRepository, gymWrapper, loginStatus);
  }



}
