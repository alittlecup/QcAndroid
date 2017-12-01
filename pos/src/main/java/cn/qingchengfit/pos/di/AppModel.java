package cn.qingchengfit.pos.di;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.pos.PosApp;
import cn.qingchengfit.pos.models.BillModel;
import cn.qingchengfit.pos.models.CardModel;
import cn.qingchengfit.pos.models.PermissionModel;
import cn.qingchengfit.pos.models.PosDbModel;
import cn.qingchengfit.pos.models.StaffModel;
import cn.qingchengfit.pos.models.StudentModel;
import cn.qingchengfit.pos.routers.BillRouters;
import cn.qingchengfit.pos.routers.CardRouters;
import cn.qingchengfit.pos.routers.PosRouterCenter;
import cn.qingchengfit.pos.routers.StaffRouters;
import cn.qingchengfit.pos.routers.StudentRouters;
import cn.qingchengfit.pos.routers.deskImpl;
import cn.qingchengfit.pos.routers.exchangeImpl;
import cn.qingchengfit.pos.routers.settingImpl;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import cn.qingchengfit.saasbase.repository.IBillModel;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.saasbase.routers.commonImpl;
import cn.qingchengfit.saasbase.routers.courseImpl;
import cn.qingchengfit.saasbase.routers.exportImpl;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import dagger.Module;
import dagger.Provides;

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
 * Created by Paper on 2017/9/25.
 */
@Module
public class AppModel {
  private PosApp app;
  private QcRestRepository qcrestRepository;
  private GymWrapper gymWrapper;
  private LoginStatus loginStatus;

  private AppModel(Builder builder) {
    app = builder.app;
    qcrestRepository = builder.qcrestRepository;
    gymWrapper = builder.gymWrapper;
    loginStatus = builder.loginStatus;
  }

  @Provides SaasbaseRouterCenter providerRouterCenter(){
    return new SaasbaseRouterCenter(new BillRouters(),new CardRouters(),new commonImpl(),new courseImpl(),new exportImpl(),new StaffRouters(),new StudentRouters());
  }
  @Provides PosRouterCenter providerPosRouterCenter(){
    return new PosRouterCenter(new deskImpl(),new settingImpl(),new exchangeImpl());
  }

  @Provides PosApp provideApplicationContext() {
    return app;
  }

  @Provides QcRestRepository providerQcRepository() {
    return qcrestRepository;
  }

  @Provides LoginStatus providerLoginStatus() {
    return loginStatus;
  }

  @Provides GymWrapper provideGym() {
    return gymWrapper;
  }

  @Provides IStudentModel providerStudentModel(){return  new StudentModel(qcrestRepository,gymWrapper,loginStatus);}

  @Provides ICardModel providerCardModel() {
    return new CardModel(qcrestRepository,gymWrapper,loginStatus);
  }

  @Provides IBillModel provideBillModel(){
    return new BillModel(qcrestRepository,gymWrapper,loginStatus);
  }

  @Provides IStaffModel providerStaffModel(){
    return new StaffModel(qcrestRepository,gymWrapper,loginStatus);
  }

  @Provides IPermissionModel providerPermission(){
    return new PermissionModel();
  }

  @Provides QcDbManager providerDb(){
    return new PosDbModel();
  }


  //@Provides ICourseModel provideCourseModel(){
  //  return new ICourseModel() {
  //    @Override public Observable<QcDataResponse<BatchCourseListWrap>> qcGetGroupBatch() {
  //      return null;
  //    }
  //
  //    @Override public Observable<QcDataResponse<BatchCoachListWrap>> qcGetPrivateBatch() {
  //      return null;
  //    }
  //
  //    @Override public Observable<QcDataResponse<CourseLisWrap>> qcGetCourses(boolean is_private) {
  //      return null;
  //    }
  //
  //    @Override
  //    public Observable<QcDataResponse<CourseLisWrap>> qcGetCoursesPermission(boolean is_private) {
  //      return null;
  //    }
  //
  //    @Override public Observable<QcDataResponse<QcResponsePrivateDetail>> qcGetPrivateCoaches(
  //      String coach_id) {
  //      return null;
  //    }
  //
  //    @Override public Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(
  //        @Path("course_id") String course_id) {
  //      return null;
  //    }
  //
  //    @Override public Observable<QcDataResponse<BatchDetailWrap>> qcGetBatchDetail(
  //        @Path("batch_id") String batch_id) {
  //      return null;
  //    }
  //
  //    @Override
  //    public Observable<QcDataResponse<BatchSchedulesWrap>> qcGetbatchSchedules(String batch_id,
  //        boolean isPrivate) {
  //      return null;
  //    }
  //
  //    @Override
  //    public Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(boolean isPrivate,
  //        String teacher_id, String course_id) {
  //      return null;
  //    }
  //
  //    @Override
  //    public Observable<QcResponse> qcCheckBatch(boolean isPrivate, ArrangeBatchBody body) {
  //      return null;
  //    }
  //
  //    @Override public Observable<QcResponse> qcArrangeBatch(ArrangeBatchBody body) {
  //      return null;
  //    }
  //
  //    @Override public Observable<QcResponse> qcUpdateBatch(String batchid, ArrangeBatchBody body) {
  //      return null;
  //    }
  //
  //    @Override public Observable<QcResponse> qcDelBatchSchedule(boolean isPrivate,
  //        @Body DelBatchScheduleBody body) {
  //      return null;
  //    }
  //
  //    @Override
  //    public Observable<QcDataResponse<SingleBatchWrap>> qcGetSingleBatch(boolean isPrivate,
  //        String single_id) {
  //      return null;
  //    }
  //
  //    @Override public Observable<QcResponse> delBatch(String batch_id) {
  //      return null;
  //    }
  //
  //    @Override
  //    public Observable<QcResponse> qcUpdateBatchSchedule(boolean isPirvate, String scheduleid,
  //        SingleBatchBody body) {
  //      return null;
  //    }
  //  };
  //}


  public static final class Builder {
    private PosApp app;
    private QcRestRepository qcrestRepository;
    private PosGymWrapper gymWrapper;
    private LoginStatus loginStatus;

    public Builder() {
    }

    public Builder app(PosApp val) {
      app = val;
      return this;
    }

    public Builder qcrestRepository(QcRestRepository val) {
      qcrestRepository = val;
      return this;
    }

    public Builder gymWrapper(PosGymWrapper val) {
      gymWrapper = val;
      return this;
    }

    public Builder loginStatus(LoginStatus val) {
      loginStatus = val;
      return this;
    }

    public AppModel build() {
      return new AppModel(this);
    }
  }


}
