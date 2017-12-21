package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.CardModel;
import cn.qingchengfit.model.ExportModel;
import cn.qingchengfit.model.SaasModelImpl;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.routers.gymImpl;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.student.bean.SourceBeans;
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import cn.qingchengfit.saasbase.student.network.body.AbsentceListWrap;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponseWrap;
import cn.qingchengfit.saasbase.student.network.body.AttendanceCharDataBean;
import cn.qingchengfit.saasbase.student.network.body.AttendanceListWrap;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.saasbase.student.network.body.StudentWithCoashListWrap;
import cn.qingchengfit.staffkit.repository.CourseModel;
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
import cn.qingchengfit.saasbase.routers.cardImpl;
import cn.qingchengfit.saasbase.routers.commonImpl;
import cn.qingchengfit.saasbase.routers.courseImpl;
import cn.qingchengfit.saasbase.routers.exportImpl;
import cn.qingchengfit.saasbase.routers.staffImpl;
import cn.qingchengfit.saasbase.routers.studentImpl;
import cn.qingchengfit.saasbase.student.network.body.AddStdudentBody;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.model.db.QCDbManagerImpl;
import cn.qingchengfit.staffkit.repository.SerPermissionImpl;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import dagger.Module;
import dagger.Provides;
import java.util.HashMap;
import java.util.List;
import rx.Observable;

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
    this.saasbaseRouterCenter = new SaasbaseRouterCenter(new billImpl(),new cardImpl(),new staffImpl(),new commonImpl(),new courseImpl(),new exportImpl(),new gymImpl(),new studentImpl());
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


  @Provides SaasbaseRouterCenter provideRc(){
    return saasbaseRouterCenter;
  }

  @Provides ICourseModel provideCourseApi(){
    return courseModel;
  }

  @Provides public ICardModel provideCardModel(){
    return cardModel;
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
    return new IStudentModel() {
      @Override public Observable<QcDataResponse<StudentListWrapper>> getAllStudentNoPermission() {
        return null;
      }

      @Override public Observable<QcDataResponse> addStudent(AddStdudentBody body) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(String id,
        HashMap<String, Object> params) {
        return null;
      }

      @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id,
        HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(String staff_id,
        HashMap<String, Object> params) {
        return null;
      }

      @Override public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetStaffList(String staff_id,
        String type, HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(String staff_id,
        HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(String staff_id,
        String type, HashMap<String, Object> params) {
        return null;
      }

      @Override public Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override public Observable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id,
        String brandid, String shopid, String gymid, String model) {
        return null;
      }

      @Override
      public Observable<QcResponse> qcModifySellers(String staff_id, HashMap<String, Object> params,
        HashMap<String, Object> body) {
        return null;
      }

      @Override public Observable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcResponse> qcAllocateCoach(String staff_id, HashMap<String, Object> body) {
        return null;
      }

      @Override
      public Observable<QcResponse> qcRemoveStudent(String staff_id, HashMap<String, Object> body) {
        return null;
      }

      @Override public Observable<QcResponse> qcDeleteStudents(String staff_id,
        HashMap<String, Object> body) {
        return null;
      }

      @Override public Observable<QcResponse> qcRemoveStaff(String staff_id, String type,
        HashMap<String, Object> body) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentCreate(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(
        String staff_id, String type, HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override public Observable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override public Observable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(String id,
        HashMap<String, Object> params) {
        return null;
      }

      @Override public Observable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(String id,
        HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(String id,
        HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(String staffId,
        HashMap<String, Object> params) {
        return null;
      }

      @Override public Observable<QcDataResponse<SalerUserListWrap>> qcGetTrackStudentsRecommends(
        String staff_id, HashMap<String, Object> params) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(String staff_id,
        HashMap<String, Object> params) {
        return null;
      }
    };
  }



}
