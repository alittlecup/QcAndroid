package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.SaasModelImpl;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.course.batch.bean.ScheduleTemplete;
import cn.qingchengfit.saasbase.course.batch.network.body.ArrangeBatchBody;
import cn.qingchengfit.saasbase.course.batch.network.body.DelBatchScheduleBody;
import cn.qingchengfit.saasbase.course.batch.network.body.SingleBatchBody;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchCoachListWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchCourseListWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchDetailWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchSchedulesWrap;
import cn.qingchengfit.saasbase.course.batch.network.response.GroupCourseScheduleDetail;
import cn.qingchengfit.saasbase.course.batch.network.response.QcResponsePrivateDetail;
import cn.qingchengfit.saasbase.course.batch.network.response.SingleBatchWrap;
import cn.qingchengfit.saasbase.course.course.network.response.CourseLisWrap;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.repository.SaasModel;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.PostionListWrap;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.model.db.QCDbManagerImpl;
import cn.qingchengfit.staffkit.repository.SerPermissionImpl;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
import dagger.Module;
import dagger.Provides;
import retrofit2.http.Body;
import retrofit2.http.Path;
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

  @Provides IStaffModel providerStaff(){
    return new IStaffModel() {
      @Override public Observable<QcDataResponse<UserWrap>> getCurUser() {
        return null;
      }

      @Override public Observable<QcDataResponse<SalerListWrap>> getSalers() {
        return null;
      }

      @Override public Observable<QcDataResponse<SalerListWrap>> getStaffList(String id) {
        return null;
      }

      @Override public Observable<QcDataResponse> addStaff(ManagerBody body) {
        return null;
      }

      @Override public Observable<QcDataResponse> delStaff(String id) {
        return null;
      }

      @Override public Observable<QcDataResponse> editStaff(String id, ManagerBody body) {
        return null;
      }

      @Override public Observable<QcDataResponse<PostionListWrap>> getPositions() {
        return null;
      }
    };
  }

  @Provides ICourseModel provideCourseApi(){
    return new ICourseModel() {
      @Override public Observable<QcDataResponse<BatchCourseListWrap>> qcGetGroupCourse() {
        return null;
      }

      @Override public Observable<QcDataResponse<BatchCoachListWrap>> qcGetPrivateCrourse() {
        return null;
      }

      @Override public Observable<QcDataResponse<CourseLisWrap>> qcGetCourses(boolean is_private) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<CourseLisWrap>> qcGetCoursesPermission(boolean is_private) {
        return null;
      }

      @Override public Observable<QcResponsePrivateDetail> qcGetPrivateCoaches(String coach_id) {
        return null;
      }

      @Override public Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(
          @Path("course_id") String course_id) {
        return null;
      }

      @Override public Observable<QcDataResponse<BatchDetailWrap>> qcGetBatchDetail(
          @Path("batch_id") String batch_id) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<BatchSchedulesWrap>> qcGetbatchSchedules(String batch_id,
          boolean isPrivate) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(boolean isPrivate,
          String teacher_id, String course_id) {
        return null;
      }

      @Override
      public Observable<QcResponse> qcCheckBatch(boolean isPrivate, ArrangeBatchBody body) {
        return null;
      }

      @Override public Observable<QcResponse> qcArrangeBatch(ArrangeBatchBody body) {
        return null;
      }

      @Override public Observable<QcResponse> qcUpdateBatch(String batchid, ArrangeBatchBody body) {
        return null;
      }

      @Override public Observable<QcResponse> qcDelBatchSchedule(boolean isPrivate,
          @Body DelBatchScheduleBody body) {
        return null;
      }

      @Override
      public Observable<QcDataResponse<SingleBatchWrap>> qcGetSingleBatch(boolean isPrivate,
          String single_id) {
        return null;
      }

      @Override public Observable<QcResponse> delBatch(String batch_id) {
        return null;
      }

      @Override
      public Observable<QcResponse> qcUpdateBatchSchedule(boolean isPirvate, String scheduleid,
          SingleBatchBody body) {
        return null;
      }
    };
  }

}
