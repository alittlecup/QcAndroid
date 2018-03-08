package com.qingchengfit.fitcoach.di;

import cn.qingchengfit.bean.CurentPermissions;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.LoginModel;
import cn.qingchengfit.model.UserModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.cards.bean.BalanceCount;
import cn.qingchengfit.saasbase.cards.bean.DayOffs;
import cn.qingchengfit.saasbase.cards.bean.QcResponseRealcardHistory;
import cn.qingchengfit.saasbase.cards.bean.UUIDModel;
import cn.qingchengfit.saasbase.cards.network.body.AddDayOffBody;
import cn.qingchengfit.saasbase.cards.network.body.AheadOffDayBody;
import cn.qingchengfit.saasbase.cards.network.body.CardBalanceNotifyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.cards.network.body.ShopsBody;
import cn.qingchengfit.saasbase.cards.network.body.UpdateCardValidBody;
import cn.qingchengfit.saasbase.cards.network.response.BalanceConfigs;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
import cn.qingchengfit.saasbase.cards.network.response.NotityIsOpenConfigs;
import cn.qingchengfit.saasbase.cards.network.response.Shops;
import cn.qingchengfit.model.CourseModel;
import cn.qingchengfit.model.GymConfigModel;
import cn.qingchengfit.saasbase.gymconfig.IGymConfigModel;
import cn.qingchengfit.saasbase.login.ILoginModel;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.saasbase.routers.billImpl;
import cn.qingchengfit.saasbase.routers.commonImpl;
import cn.qingchengfit.saasbase.routers.exportImpl;
import cn.qingchengfit.saasbase.routers.gymImpl;
import cn.qingchengfit.saasbase.routers.staffImpl;
import cn.qingchengfit.saasbase.routers.userImpl;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.student.bean.SourceBeans;
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import cn.qingchengfit.saasbase.student.network.body.AbsentceListWrap;
import cn.qingchengfit.saasbase.student.network.body.AddStudentBody;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponseWrap;
import cn.qingchengfit.saasbase.student.network.body.AttendanceCharDataBean;
import cn.qingchengfit.saasbase.student.network.body.AttendanceListWrap;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.saasbase.student.network.body.StudentWithCoashListWrap;
import cn.qingchengfit.saasbase.user.IUserModel;
import com.google.gson.JsonObject;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.routers.CourseRouter;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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
    private final IWXAPI api;
    private final UserModel userModel;
    private LoginStatus loginStatus;
    private GymWrapper gymWrapper;
    private App app;
    private BaseRouter router;
    private RestRepository restRepository;
    private QcRestRepository qcRestRepository;
    private ICourseModel courseModel;
    private IGymConfigModel gymConfigModel;
    private ILoginModel loginModel;
    private QcDbManager db;
    private SaasbaseRouterCenter saasbaseRouterCenter = new SaasbaseRouterCenter()
      .registe(new exportImpl())
      .registe(new gymImpl())
      .registe(new staffImpl())
      .registe(new commonImpl())
      .registe(new CourseRouter())
      .registe(new userImpl())
      .registe(new billImpl());

    private IPermissionModel permissionModel = new IPermissionModel() {
        @Override public boolean check(String permission) {
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
        restRepository = builder.restRepository;
        qcRestRepository = new QcRestRepository(app, Configs.Server,app.getString(R.string.oem_tag));
        courseModel = new CourseModel(qcRestRepository,gymWrapper,loginStatus);
        gymConfigModel = new GymConfigModel(gymWrapper,loginStatus,qcRestRepository);
        loginModel = new LoginModel(gymWrapper,loginStatus,qcRestRepository);
        userModel = new UserModel(gymWrapper,loginStatus,qcRestRepository);
        api = WXAPIFactory.createWXAPI(app, app.getString(R.string.wechat_code));
    }

    @Provides IWXAPI provideWx(){
        return api;
    }
    @Provides QcDbManager provideDb(){
        return db;
    }
    @Provides ILoginModel provideLoginModel(){return  loginModel;}
    @Provides IUserModel provideUserModel(){return  userModel;}
    @Provides IGymConfigModel provideGymConfig(){return  gymConfigModel;}
    @Provides ICourseModel provideCourseModel(){
        return courseModel;
    }
    @Provides ICardModel provideCardModel(){
        return cardModel;
    }
    @Provides IPermissionModel providerPermission(){
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

    @Provides public BaseRouter provideRouter() {
        return router;
    }

    @Provides public RestRepository provideRepository() {
        return restRepository;
    }
    @Provides public QcRestRepository provideQcRepository() {
        return qcRestRepository;
    }
    @Provides SaasbaseRouterCenter provideRc(){
        return saasbaseRouterCenter;
    }

    public static final class Builder {
        private LoginStatus loginStatus;
        private GymWrapper gymWrapper;
        private App app;
        private BaseRouter router;
        private RestRepository restRepository;
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

            @Override public Observable<QcDataResponse> addStudent(AddStudentBody body) {
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

    private ICardModel  cardModel = new ICardModel(){

        @Override public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type,
          String isEnable) {
            return null;
        }

        @Override public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTplsPermission() {
            return null;
        }

        @Override
        public Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(String cardid) {
            return null;
        }

        @Override
        public Observable<QcDataResponse<CardTplOptionListWrap>> qcGetOptions(String cardtps_id) {
            return null;
        }

        @Override
        public Observable<QcDataResponse<CardTplListWrap>> qcGetCardFilterTpls(boolean is_active) {
            return null;
        }

        @Override public Observable<QcDataResponse<CardWrap>> qcGetCardDetail(String card_id) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcCreateCardtpl(CardtplBody body) {
            return null;
        }

        @Override
        public Observable<QcDataResponse> qcUpdateCardtpl(String card_tpl_id, CardtplBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcDelCardtpl(String card_tpl_id) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcResumeCardtpl(String card_tpl_id) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcDelCardStandard(String option_id) {
            return null;
        }

        @Override
        public Observable<QcDataResponse> qcUpdateCardStandard(String option_id, OptionBody body) {
            return null;
        }

        @Override
        public Observable<QcDataResponse> qcCreateStandard(String card_tpl_id, OptionBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse<JsonObject>> qcChargeCard(String cardId,
          CardBuyBody chargeBody) {
            return null;
        }

        @Override public Observable<QcDataResponse<JsonObject>> buyCard(CardBuyBody body) {
            return null;
        }

        @Override
        public Observable<QcDataResponse<CardListWrap>> qcGetAllCard(HashMap<String, Object> params) {
            return null;
        }

        @Override
        public Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard(HashMap<String, Object> params) {
            return null;
        }

        @Override public Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard() {
            return null;
        }

        @Override
        public Observable<QcDataResponse<StudentListWrapper>> qcGetBindStudent(String cardid) {
            return null;
        }

        @Override
        public Observable<QcDataResponse> editCardInfo(String cardid, HashMap<String, Object> p) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcChangeAutoNotify(CardBalanceNotifyBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse<NotityIsOpenConfigs>> qcGetNotifySetting(
          HashMap<String, Object> params) {
            return null;
        }

        @Override public Observable<QcDataResponse<BalanceConfigs>> qcGetBalanceCondition(
          HashMap<String, Object> params, String keys) {
            return null;
        }

        @Override
        public Observable<QcDataResponse> qcPostBalanceCondition(CardBalanceNotifyBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse<Shops>> qcGetBrandShops(String brand_id) {
            return null;
        }

        @Override public Observable<QcDataResponse<UUIDModel>> qcStashNewCardTpl(CardtplBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse<JsonObject>> qcChargeRefund(String cardId,
          ChargeBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcAddDayOff(AddDayOffBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcDelDayOff(String leaveId) {
            return null;
        }

        @Override public Observable<QcDataResponse<DayOffs>> qcGetDayOffList(String cardId) {
            return null;
        }

        @Override
        public Observable<QcDataResponse> qcAheadOffDay(String leaveId, AheadOffDayBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcStopCard(String cardId) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcModifyValidate(String cardId,
          UpdateCardValidBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcResumeCard(String cardId) {
            return null;
        }

        @Override
        public Observable<QcDataResponse<QcResponseRealcardHistory>> qcConsumeRecord(String cardId,
          int page, String start, String end) {
            return null;
        }

        @Override public Observable<QcDataResponse> qcFixGyms(String cardId, ShopsBody body) {
            return null;
        }

        @Override public Observable<QcDataResponse<BalanceCount>> qcGetBalanceCount() {
            return null;
        }
    };
}
