package cn.qingchengfit.pos.di;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.pos.PosApp;
import cn.qingchengfit.pos.models.CardModel;
import cn.qingchengfit.pos.models.StaffModel;
import cn.qingchengfit.pos.models.StudentModel;
import cn.qingchengfit.pos.routers.CardRouters;
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
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.routers.Icard;
import cn.qingchengfit.saasbase.routers.Istaff;
import cn.qingchengfit.saasbase.routers.Istudent;
import cn.qingchengfit.saasbase.routers.RouterCenter;
import cn.qingchengfit.saasbase.routers.commonImpl;
import cn.qingchengfit.saasbase.routers.courseImpl;
import cn.qingchengfit.saasbase.routers.staffImpl;
import cn.qingchengfit.saasbase.routers.studentImpl;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import dagger.Module;
import dagger.Provides;
import retrofit2.http.Body;
import retrofit2.http.Path;
import rx.Observable;

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

  @Provides RouterCenter providerRouterCenter(){
    return new RouterCenter(new CardRouters(),new commonImpl(),new courseImpl(),new staffImpl(),new studentImpl());
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

  @Provides Istudent provideStudent(){
    return new studentImpl();
  }

  @Provides IStudentModel providerStudentModel(){return  new StudentModel(qcrestRepository,gymWrapper,loginStatus);}

  @Provides ICardModel providerCardModel() {
    return new CardModel(qcrestRepository,gymWrapper,loginStatus);
  }
  @Provides Istaff prividerStaffRouter(){
    return new staffImpl();
  }

  @Provides Icard providerICards(){
    return new CardRouters();
  }

  //@Provides Ibill providerIBill(){return new billImpl();}

  @Provides IStaffModel providerStaffModel(){
    return new StaffModel(qcrestRepository,gymWrapper,loginStatus);
  }


  @Provides ICourseModel provideCourseModel(){
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


  public static final class Builder {
    private PosApp app;
    private QcRestRepository qcrestRepository;
    private GymWrapper gymWrapper;
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

    public Builder gymWrapper(GymWrapper val) {
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
