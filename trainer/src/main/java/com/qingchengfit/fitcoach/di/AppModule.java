package com.qingchengfit.fitcoach.di;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.student.bean.SourceBeans;
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import cn.qingchengfit.saasbase.student.network.body.AbsentceListWrap;
import cn.qingchengfit.saasbase.student.network.body.AddStdudentBody;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponseWrap;
import cn.qingchengfit.saasbase.student.network.body.AttendanceCharDataBean;
import cn.qingchengfit.saasbase.student.network.body.AttendanceListWrap;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.saasbase.student.network.body.StudentWithCoashListWrap;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.RestRepository;
import dagger.Module;
import dagger.Provides;
import java.util.HashMap;
import java.util.List;
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
 * Created by Paper on 2017/4/17.
 */
@Module public class AppModule {
    private LoginStatus loginStatus;
    private GymWrapper gymWrapper;
    private App app;
    private BaseRouter router;
    private RestRepository restRepository;
    private QcRestRepository qcRestRepository;

    private AppModule(Builder builder) {
        loginStatus = builder.loginStatus;
        gymWrapper = builder.gymWrapper;
        app = builder.app;
        router = builder.router;
        restRepository = builder.restRepository;
        qcRestRepository = new QcRestRepository(app, Configs.Server,app.getString(R.string.oem_tag));
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

    @Provides public BaseRouter provideRouter() {
        return router;
    }

    @Provides public RestRepository provideRepository() {
        return restRepository;
    }
    @Provides public QcRestRepository provideQcRepository() {
        return qcRestRepository;
    }

    public static final class Builder {
        private LoginStatus loginStatus;
        private GymWrapper gymWrapper;
        private App app;
        private BaseRouter router;
        private RestRepository restRepository;

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

        public Builder app(App val) {
            app = val;
            return this;
        }

        public Builder router(BaseRouter val) {
            router = val;
            return this;
        }

        public Builder restRepository(RestRepository val) {
            restRepository = val;
            return this;
        }

        public AppModule build() {
            return new AppModule(this);
        }
    }

    @Provides IStudentModel providerStudent(){
        return new IStudentModel() {
            @Override
            public Observable<QcDataResponse<StudentListWrapper>> getAllStudentNoPermission() {
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

            @Override
            public Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id,
              HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(String staff_id,
              HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(
              String staff_id, HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetStaffList(String staff_id,
              String type, HashMap<String, Object> params) {
                return null;
            }

            @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(
              String staff_id, HashMap<String, Object> params) {
                return null;
            }

            @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(
              String staff_id, String type, HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(
              String staff_id, HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id,
              String brandid, String shopid, String gymid, String model) {
                return null;
            }

            @Override public Observable<QcResponse> qcModifySellers(String staff_id,
              HashMap<String, Object> params, HashMap<String, Object> body) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(
              String staff_id, HashMap<String, Object> params) {
                return null;
            }

            @Override public Observable<QcResponse> qcAllocateCoach(String staff_id,
              HashMap<String, Object> body) {
                return null;
            }

            @Override public Observable<QcResponse> qcRemoveStudent(String staff_id,
              HashMap<String, Object> body) {
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

            @Override
            public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(
              String staff_id, String type, HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(
              String staff_id, HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(
              String staff_id, HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(
              String staff_id, HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(
              String id, HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(String id,
              HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(String id,
              HashMap<String, Object> params) {
                return null;
            }

            @Override public Observable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(
              String staffId, HashMap<String, Object> params) {
                return null;
            }

            @Override
            public Observable<QcDataResponse<SalerUserListWrap>> qcGetTrackStudentsRecommends(
              String staff_id, HashMap<String, Object> params) {
                return null;
            }

            @Override public Observable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(
              String staff_id, HashMap<String, Object> params) {
                return null;
            }
        };
    }
}
