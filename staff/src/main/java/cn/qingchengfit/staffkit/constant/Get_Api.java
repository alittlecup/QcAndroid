package cn.qingchengfit.staffkit.constant;

import android.support.annotation.IntRange;
import android.support.v4.util.ArrayMap;
import cn.qingchengfit.chat.model.RecordWrap;
import cn.qingchengfit.model.common.Absentces;
import cn.qingchengfit.model.common.Attendances;
import cn.qingchengfit.model.responese.AllLockers;
import cn.qingchengfit.model.responese.AllotSalePreViews;
import cn.qingchengfit.model.responese.AllotSaleStudents;
import cn.qingchengfit.model.responese.ArticleCommentListData;
import cn.qingchengfit.model.responese.AttendanceCharDataBean;
import cn.qingchengfit.model.responese.BalanceConfigs;
import cn.qingchengfit.model.responese.BalanceCount;
import cn.qingchengfit.model.responese.BalanceNotifyConfigs;
import cn.qingchengfit.model.responese.BodyTestMeasureData;
import cn.qingchengfit.model.responese.BodyTestPreviews;
import cn.qingchengfit.model.responese.BodyTestTemplateData;
import cn.qingchengfit.model.responese.BrandResponse;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.CacluScore;
import cn.qingchengfit.model.responese.CardBindStudents;
import cn.qingchengfit.model.responese.CardResponse;
import cn.qingchengfit.model.responese.CardTplResponse;
import cn.qingchengfit.model.responese.CardTpls;
import cn.qingchengfit.model.responese.Cards;
import cn.qingchengfit.model.responese.ChatFriendsData;
import cn.qingchengfit.model.responese.ClassRecords;
import cn.qingchengfit.model.responese.CoursePlans;
import cn.qingchengfit.model.responese.CourseReportDetail;
import cn.qingchengfit.model.responese.CourseSchedules;
import cn.qingchengfit.model.responese.CourseTeacherResponse;
import cn.qingchengfit.model.responese.CourseTypeResponse;
import cn.qingchengfit.model.responese.CourseTypeSamples;
import cn.qingchengfit.model.responese.CourseTypes;
import cn.qingchengfit.model.responese.DayOffs;
import cn.qingchengfit.model.responese.FollowRecords;
import cn.qingchengfit.model.responese.FollowUpConver;
import cn.qingchengfit.model.responese.FollowUpDataStatistic;
import cn.qingchengfit.model.responese.GroupCourseResponse;
import cn.qingchengfit.model.responese.GroupCourseScheduleDetail;
import cn.qingchengfit.model.responese.GymCardtpl;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.GymExtra;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.model.responese.GymSites;
import cn.qingchengfit.model.responese.HomeInfo;
import cn.qingchengfit.model.responese.LockerRegions;
import cn.qingchengfit.model.responese.Notification;
import cn.qingchengfit.model.responese.NotificationGlance;
import cn.qingchengfit.model.responese.NotityIsOpenConfigs;
import cn.qingchengfit.model.responese.QcResponToken;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseBtaches;
import cn.qingchengfit.model.responese.QcResponseCards;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponseJacket;
import cn.qingchengfit.model.responese.QcResponseOption;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.model.responese.QcResponsePostions;
import cn.qingchengfit.model.responese.QcResponsePrivateBatchDetail;
import cn.qingchengfit.model.responese.QcResponsePrivateCourse;
import cn.qingchengfit.model.responese.QcResponsePrivateDetail;
import cn.qingchengfit.model.responese.QcResponseRealcardHistory;
import cn.qingchengfit.model.responese.QcResponseRenewalHistory;
import cn.qingchengfit.model.responese.QcResponseSaleDetail;
import cn.qingchengfit.model.responese.QcResponseSchedulePhotos;
import cn.qingchengfit.model.responese.QcResponseServiceDetial;
import cn.qingchengfit.model.responese.QcResponseShopComment;
import cn.qingchengfit.model.responese.QcResponseSignInImg;
import cn.qingchengfit.model.responese.QcResponseStatementDetail;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.model.responese.QcResponseStudentInfo;
import cn.qingchengfit.model.responese.QcSchedulesResponse;
import cn.qingchengfit.model.responese.Referrers;
import cn.qingchengfit.model.responese.RenewalList;
import cn.qingchengfit.model.responese.ResponseService;
import cn.qingchengfit.model.responese.ResponseSu;
import cn.qingchengfit.model.responese.ScheduleActions;
import cn.qingchengfit.model.responese.ScheduleTemplete;
import cn.qingchengfit.model.responese.Score;
import cn.qingchengfit.model.responese.ScoreHistoryResponse;
import cn.qingchengfit.model.responese.ScoreRankResponse;
import cn.qingchengfit.model.responese.ScoreRuleAwardResponse;
import cn.qingchengfit.model.responese.ScoreRuleResponse;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.model.responese.Sellers;
import cn.qingchengfit.model.responese.ShopDetail;
import cn.qingchengfit.model.responese.Shops;
import cn.qingchengfit.model.responese.ShortMsgDetail;
import cn.qingchengfit.model.responese.ShortMsgList;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.model.responese.SignInDetail;
import cn.qingchengfit.model.responese.SignInSchdule;
import cn.qingchengfit.model.responese.SignInTasks;
import cn.qingchengfit.model.responese.SignInUrl;
import cn.qingchengfit.model.responese.SigninNoticeConfigs;
import cn.qingchengfit.model.responese.SigninReportDetail;
import cn.qingchengfit.model.responese.SigninValidCard;
import cn.qingchengfit.model.responese.SingleBatchData;
import cn.qingchengfit.model.responese.StaffResponse;
import cn.qingchengfit.model.responese.StaffShipResponse;
import cn.qingchengfit.model.responese.Staffs;
import cn.qingchengfit.model.responese.StatementGlanceResp;
import cn.qingchengfit.model.responese.StudentTrackPreview;
import cn.qingchengfit.model.responese.Students;
import cn.qingchengfit.model.responese.TrackFilterOrigins;
import cn.qingchengfit.model.responese.TrackSellers;
import cn.qingchengfit.model.responese.TrackStudents;
import cn.qingchengfit.staffkit.allocate.coach.model.AllocateStudentBean;
import cn.qingchengfit.staffkit.allocate.coach.model.CoachResponseList;
import cn.qingchengfit.staffkit.train.model.GroupListResponse;
import cn.qingchengfit.staffkit.train.model.SignRecord;
import cn.qingchengfit.staffkit.train.model.SignUpDetailResponse;
import cn.qingchengfit.staffkit.train.model.TeamDetailResponse;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
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
 * Created by Paper on 15/12/1 2015.
 */
public interface Get_Api {
    //获取token
    @GET("/api/csrftoken/") Call<QcResponToken> qcGetToken();

    /**
     * 品牌
     */
    @Deprecated @GET("/api/brands/") rx.Observable<BrandsResponse> qcGetBrands();

    @GET("/api/staffs/{staff_id}/brands/{id}/") rx.Observable<QcResponseData<BrandResponse>> qcGetBrand(@Path("staff_id") String staff_id,
        @Path("id") String id);

    @GET("/api/staffs/{id}/brands/") rx.Observable<QcResponseData<BrandsResponse>> qcGetBrands(@Path("id") String id);

    @GET("/api/staffs/{id}/services/") rx.Observable<QcResponseData<GymList>> qcGetCoachService(@Path("id") String id,
        @Query("brand_id") String brand_id);

    @GET("/api/staffs/{id}/services/{sid}/") rx.Observable<ResponseService> qcGetService(@Path("id") String id, @Path("sid") String sid,
        @Query("model") String model);

    @GET("/api/staffs/{id}/check/superuser/") rx.Observable<QcResponseData<ResponseSu>> qcCheckSu(@Path("id") String id,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/v2/staffs/{id}/gyms/welcome/") rx.Observable<QcResponseData<GymDetail>> qcGetGymDetail(@Path("id") String staffid,
        @Query("id") String id, @Query("model") String model);

    @GET("/api/v2/staffs/{staff_id}/gyms/pay/") rx.Observable<QcResponseData<RenewalList>> qcGetGymPay(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/v2/staffs/{staff_id}/gym-extra/") rx.Observable<QcResponseData<GymExtra>> qcGetGymExtra(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    //获取通知 分页和不分页接口 ,后者只为拿 未读
    @Deprecated @GET("/api/notifications/") rx.Observable<QcResponseData<Notification>> qcGetMessages(@Query("staff_id") String id,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/v2/notifications/?order_by=-created_at") rx.Observable<QcResponseData<Notification>> qcGetNotification(
        @QueryMap HashMap<String, Object> query);

    @GET("/api/v2/notifications/index/") rx.Observable<QcResponseData<List<NotificationGlance>>> qcGetNotificationIndex(
        @Query("type_json") String query);

    /**
     * @param id
     * @param date
     * @return
     */
    @GET("/api/staffs/{id}/schedules/") rx.Observable<QcSchedulesResponse> qcGetSchedules(@Path("id") String id, @Query("date") String date,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/staffs/{id}/{type}/{single_id}/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<SingleBatchData>> qcGetSingleBatch(@Path("id") String id,
        @Path("type") String type, @Path("single_id") String single_id, @QueryMap HashMap<String, Object> params);

    //工作人员详情
    @GET("/api/staffs/{id}/") rx.Observable<QcResponseData<StaffResponse>> qcGetSelfInfo(@Path("id") String id);

    // 连锁
    @GET("/api/v2/staffs/{id}/android/welcome/") rx.Observable<QcResponseData<HomeInfo>> qcWelcomeHome(@Path("id") String id,
        @Query("brand_id") String brandid);

    //工作人员下所有会员
    //    @GET("/api/staffs/{id}/users/all/?show_all=1")
    @GET("/api/staffs/{id}/users/?show_all=1") rx.Observable<QcResponseData<Students>> qcGetAllStudents(@Path("id") String id,
        @QueryMap HashMap<String, Object> params);

    //工作人员下某家健身房会员
    @GET("/api/staffs/{id}/users/") rx.Observable<QcResponseData<Students>> qcGetAllStudents(@Path("id") String id,
        @Query("id") String gymid, @Query("model") String model);

    //会员卡可绑定的会员列表
    @GET("/api/staffs/{staff_id}/method/users/?show_all=1") rx.Observable<QcResponseData<Students>> qcGetCardBundldStudents(
        @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

    /**
     * 缺勤列表
     *
     * @param params 缺勤<7： absence__lt=7，
     * 7-30天：absence__gte=7&absence__lte=30,
     * 缺勤>60天：absence__ge=60
     * @return "attendances": [{"user": {"username": "俞小西","gender": 1,"id": 2131,"avatar": "http://zoneke-img.b0.upaiyun.com/9360bb9fb982a95c915edf44f611678f.jpeg!120x120","phone":
     * "18611985427"},"absence": 390,"date_and_time": "2016-01-30 13:30-14:30","id": 5933,"title": "娜娜私教 杨娜娜"},]
     */
    @GET("/api/staffs/{staff_id}/users/absence/") rx.Observable<QcResponseData<Absentces>> qcGetUsersAbsences(@Path("staff_id") String id,
        @QueryMap HashMap<String, Object> params);

    //获取缺勤图表数据
    @GET("/api/staffs/{id}/users/attendance/glance/") rx.Observable<QcResponseData<AttendanceCharDataBean>> qcGetAttendanceChart(
        @Path("id") String id, @QueryMap HashMap<String, Object> params);

    /**
     * @param params 必传start, end，
     * 可选排序字段 加 “-”说明是倒序
     * order_by=
     * days      -days
     * group     -group
     * private     -private
     * checkin   -checkin
     * @return "attendances": [{"checkin": 0,"group": 139,"user": {"username": "孙正其","gender": 0,"id": 2219,"avatar":
     * "http://zoneke-img.b0.upaiyun.com/a15bec431224aa638a4b8eccb2e96955.jpg!120x120","phone": "18536668518"},"private_count": 8,"days":
     * 142},
     */
    @GET("/api/staffs/{staff_id}/users/attendance/") Observable<QcResponseData<Attendances>> qcGetUsersAttendances(
        @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

    /**
     * 日程安排
     */
    @GET("/api/staffs/{id}/actions/redirect/") rx.Observable<QcResponseData<ScheduleActions>> qcGetScheduleAciton(@Path("id") String id,
        @Query("action") String action, @QueryMap HashMap<String, Object> params);

    /**
     * 卡类型
     */
    //工作人员 卡类型
    @GET("/api/v2/staffs/{id}/cardtpls/all/?show_all=1&order_by=-id") rx.Observable<QcResponseData<CardTpls>> qcGetCardTpls(
        @Path("id") String id, @QueryMap HashMap<String, Object> params, @Query("type") String type, @Query("is_enable") String isEnable);

    // 卡类型
    @GET("/api/staffs/{id}/method/cardtpls/?show_all=1") rx.Observable<QcResponseData<CardTpls>> qcGetCardTplsPermission(
        @Path("id") String id, @QueryMap HashMap<String, Object> parasm);

    //工作人员 卡类型详情
    @GET("/api/staffs/{Staff}/cardtpls/{id}/") rx.Observable<QcResponseData<CardTplResponse>> qcGetCardTplsDetail(
        @Path("Staff") String staff, @Path("id") String id, @QueryMap HashMap<String, Object> parasm);

    // 工作人员 卡类型 规格
    @GET("/api/staffs/{staff_id}/cardtpls/{cardtps_id}/options/") rx.Observable<QcResponseOption> qcGetOptions(
        @Path("staff_id") String staff_id, @Path("cardtps_id") String cardtps_id, @Query("id") String gymid, @Query("model") String model,
        @Query("brand_id") String brand_id);

    //学员体测数据
    @GET("/api/staffs/{id}/users/{student_id}/measures/") rx.Observable<QcResponseData<BodyTestPreviews>> qcGetStuedntBodyTest(

        @Path("id") String staffid, @Path("student_id") String student_id, @QueryMap HashMap<String, Object> params);

    //体测模板接口
    @GET("/api/measures/tpl/") rx.Observable<QcResponseData<BodyTestTemplateData>> qcGetBodyTestModel(
        @QueryMap HashMap<String, Object> params);

    //获取体测数据
    @GET("/api/staffs/{id}/measures/{measure_id}/") rx.Observable<QcResponseData<BodyTestMeasureData>> qcGetBodyTest(
        @Path("id") String staffid, @Path("measure_id") String measure_id, @QueryMap HashMap<String, Object> params);

    //获取某个健身房的教练列表
    @GET("/api/staffs/{id}/coaches/") rx.Observable<QcResponseData<Staffs>> qcGetGymCoaches(@Path("id") String id,
        @Query("id") String gymid, @Query("model") String model, @Query("q") String keyword);

    //获取某个健身房的教练列表
    @GET("/api/staffs/{id}/method/coaches/") rx.Observable<QcResponseData<Staffs>> qcGetGymCoachesPermission(@Path("id") String id,
        @QueryMap HashMap<String, Object> params);

    //获取某个健身房的教练列表
    @GET("/api/staffs/{id}/coaches/") rx.Observable<QcResponseData<Staffs>> qcGetAllCoaches(@Path("id") String id,
        @Query("brand_id") String brandid, @Query("id") String gymid, @Query("model") String model);

    //获取某个健身房的场地列表
    @GET("/api/staffs/{id}/spaces/") rx.Observable<QcResponseData<GymSites>> qcGetGymSites(@Path("id") String id, @Query("id") String gymid,
        @Query("model") String model);

    //获取某个健身房的场地列表
    @GET("/api/staffs/{id}/method/spaces/") rx.Observable<QcResponseData<GymSites>> qcGetGymSitesPermisson(@Path("id") String id,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/staffs/{id}/shops/") rx.Observable<QcResponseData<Shops>> qcGetBrandShops(@Path("id") String id,
        @Query("brand_id") String brand_id);

    @GET("/api/staffs/{id}/shops/detail") rx.Observable<QcResponseData<ShopDetail>> qcGetShopDetail(@Path("id") String id,
        @QueryMap HashMap<String, Object> params);
    //某品牌下所有场馆 ?????

    @GET("/api/staffs/{id}/brands/{brand_id}/shops/") rx.Observable<QcResponseData<Shops>> qcGetBrandAllShops(@Path("id") String id,
        @Path("brand_id") String brand_id);

    //获取某个健身房的卡模板
    @GET("/api/staffs/{id}/cardtpls/?show_all=1") rx.Observable<QcResponseData<GymCardtpl>> qcGetGymCardtpl(@Path("id") String id,
        @Query("id") String gymid, @Query("model") String model, @Query("type") String type);

    //获取某个学员的基本信息
    @GET("/api/v2/staffs/{staff_id}/users/{id}/") rx.Observable<QcResponseStudentInfo> qcGetStudentInfo(@Path("staff_id") String staffid,
        @Path("id") String studentid, @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brand_id);

    //获取某个学员的课程
    @GET("/api/staffs/{staff_id}/users/attendance/records/") rx.Observable<QcResponseData<ClassRecords>> qcGetStudentClassRecords(
        @Path("staff_id") String staffid, @Query("user_id") String studentid, @QueryMap HashMap<String, Object> params);

    //获取某个学员的cardlist
    @GET("/api/staffs/{staff_id}/users/{id}/cards/?order_by=-id") rx.Observable<QcResponseStudentCards> qcGetStudentCards(
        @Path("staff_id") String staffid, @Path("id") String studentid, @Query("id") String gymid, @Query("model") String model,
        @Query("brand_id") String brand_id);

    //获取某个学员的cardlist
    @GET("/api/staffs/{staff_id}/users/{id}/cards/?order_by=-id") rx.Observable<QcResponseStudentCards> qcGetStudentCardsWithShopId(
        @Path("staff_id") String staffid, @Path("id") String studentid, @QueryMap HashMap<String, Object> params);
    //@GET("/api/staffs/{staff_id}/users/{id}/cards/?order_by=-id") rx.Observable<QcResponseStudentCards> qcGetStudentCardsWithShopId(
    //    @Path("staff_id") String staffid, @Path("id") String studentid, @Query("id") String gymid, @Query("model") String model,
    //    @Query("brand_id") String brand_id, @Query("shop_id") String shop_id);

    //获取某个学员的跟进记录
    @GET("/api/staffs/{id}/users/{student_id}/records/?format=app") rx.Observable<QcResponseData<FollowRecords>> qcGetStudentFollow(
        @Path("id") String staffid, @Path("student_id") String user_id, @Query("id") String gymid, @Query("model") String model,
        @Query("brand_id") String brand_id, @Query("page") int page);

    //学员签到照片
    @GET("/api/staffs/{staff_id}/user/photos/") rx.Observable<QcResponseSignInImg> qcGetSignInImages(@Path("staff_id") String staffid,
        @Query("user_id") String user_id, @QueryMap HashMap<String, Object> params);

    //获取消费记录
    @GET("/api/staffs/{id}/cards/{card_id}/histories/?order_by=-created_at") rx.Observable<QcResponseRealcardHistory> qcGetCardhistory(
        @Path("id") String staffid, @Path("card_id") String card_id, @Query("brand_id") String brand_id, @Query("id") String gymid,
        @Query("model") String gymodel, @Query("created_at__gte") String start, @Query("created_at__lte") String end,
        @Query("page") int page

    );

    /**
     * 会员卡
     */
    //获取真实卡列表
    @GET("/api/staffs/{id}/cards/?order_by=-id") rx.Observable<Cards> qcGetAllCard(@Path("id") String staffid);

    //获取会员卡
    @GET("/api/staffs/{id}/cards/all/?order_by=-id") rx.Observable<QcResponseData<Cards>> qcGetBrandCard(@Path("id") String staffid,
        @Query("brand_id") String brand_id, @Query("id") String gymid, @Query("model") String gymmodel, @Query("page") int page,
        @Query("q") String keyword, @QueryMap HashMap<String, Object> params);

    //获取会员卡
    @GET("api/staffs/{id}/balance/cards/") rx.Observable<QcResponseData<Cards>> qcGetBalanceCard(@Path("id") String staffid,
        @Query("brand_id") String brand_id, @Query("id") String gymid, @Query("model") String gymmodel, @Query("page") int page,
        @Query("q") String keyword, @QueryMap HashMap<String, Object> params);

    //获取卡详情
    @GET("/api/staffs/{id}/cards/{card_id}/") rx.Observable<QcResponseData<CardResponse>> qcGetCardDetail(@Path("id") String staff,
        @Path("card_id") String card_id, @QueryMap HashMap<String, Object> params);

    @GET("/api/staffs/{id}/cards/bind/users/") rx.Observable<QcResponseData<CardBindStudents>> qcGetBindStudent(@Path("id") String staff,
        @QueryMap HashMap<String, Object> params);

    //获取筛选列表
    @GET("/api/staffs/{id}/filter/cardtpls/?show_all=1") rx.Observable<QcResponseData<CardTpls>> qcGetCardFilterCondition(
        @Path("id") String staff, @QueryMap HashMap<String, Object> params);

    //设置余额条件
    @GET("api/v2/staffs/{id}/users/configs/") rx.Observable<QcResponseData<BalanceConfigs>> qcGetBalanceCondition(
        @Path("id") String staffId, @QueryMap HashMap<String, Object> params, @Query("keys") String permission);

    /**
     * 会员卡模板
     */
    @GET("/api/staffs/{id}/cardtpls/all/") rx.Observable<QcResponse> qcGetBrandCardtpl(@Path("id") String staffid,
        @Query("brand_id") String brand_id);

    //获取请假列表
    @GET("/api/staffs/{id}/leaves/?order_by=-created_at") rx.Observable<QcResponseData<DayOffs>> qcGetDayOff(@Path("id") String staffid,
        @Query("brand_id") String brandid, @Query("card_id") String card_id, @Query("id") String gymid, @Query("model") String model);

    @GET("/api/staffs/{id}/reports/schedules/glance/") rx.Observable<QcResponseData<StatementGlanceResp>> qcGetReportGlance(
        @Path("id") String id, @Query("brand_id") String brand_id, @Query("shop_id") String shop_id, @Query("id") String gymid,
        @Query("model") String model);

    @GET("/api/staffs/{id}/reports/sells/glance/") rx.Observable<QcResponseData<StatementGlanceResp>> qcGetSaleGlance(@Path("id") String id,
        @Query("brand_id") String brand_id, @Query("shop_id") String shop_id, @Query("id") String gymid, @Query("model") String model);

    /**
     * 签到报表预览
     * 连锁运营：http://127.0.0.1:9000/api/staffs/3281/reports/checkin/glance/?brand_id=2
     * 可选参数 shop_id=
     * 单店：http://127.0.0.1:9000/api/staffs/3281/reports/checkin/glance/?model=staff_gym&id=5370
     *
     * @return Observable
     */
    @GET("/api/staffs/{staff_id}/reports/checkin/glance/") rx.Observable<QcResponseData<StatementGlanceResp>> qcGetSigninGlance(
        @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

    /**
     * 签到报表详情
     * http://127.0.0.1:9000/api/staffs/3281/reports/checkin/?brand_id=2&start=2016-09-01&end=2016-09-21
     * 可选参数 shop_id
     * 单店：http://127.0.0.1:9000/api/staffs/3281/reports/checkin/?id=5370&model=staff_gym&start=2016-09-01&end=2016-09-21
     *
     * @return Observable
     */
    @GET("/api/staffs/{staff_id}/reports/checkin/") rx.Observable<QcResponseData<SigninReportDetail>> qcGetSigninReportDetail(
        @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

    @GET("/api/v3/staffs/{id}/reports/schedules/") rx.Observable<QcResponseStatementDetail> qcGetStatementDatail(@Path("id") String id,
        @Query("start") String start, @Query("end") String end, @Query("teacher_id") String teacher_id,
        @Query("course_id") String course_id, @Query("course_extra") String course_extra,

        @Query("shop_id") String shop_id, @Query("brand_id") String brand_id, @Query("id") String gymid, @Query("model") String gymmodel);

    @GET("/api/v2/staffs/{id}/reports/sells/") rx.Observable<QcResponseSaleDetail> qcGetSaleDatail(@Path("id") String id,

        @Query("start") String start, @Query("end") String end, @Query("shop_id") String system_id,

        @Query("card__card_tpl_id") String card_tpl_id, @Query("cards_extra") String cards_extra, @Query("seller_id") String seller_id,
        @Query("type") String type, @Query("charge_type") String chargetype, @QueryMap HashMap<String, Object> params);

    //获取教练课程
    @GET("/api/v1/services/detail/") rx.Observable<QcResponseServiceDetial> qcGetServiceDetail(@Query("model") String model,
        @Query("id") String id);

    // 未使用
    @GET("/api/v1/coaches/{id}/reports/sale/cardtpls/") rx.Observable<QcResponseCards> qcGetSaleCard(@Path("id") String id);

    //获取销售 卖卡  包含销售和教练
    @GET("/api/staffs/{staff_id}/sellers/") rx.Observable<QcResponseData<Sellers>> qcGetSalersAndCoach(@Path("staff_id") String staff_id,
        @Query("brand_id") String brandid, @Query("shop_id") String shopid, @Query("id") String gymid, @Query("model") String model);

    //获取销售 卖卡  包含销售 不包含教练
    @GET("/api/staffs/{staff_id}/sellers-without-coach/") rx.Observable<QcResponseData<Sellers>> qcGetSalers(
        @Path("staff_id") String staff_id, @Query("brand_id") String brandid, @Query("shop_id") String shopid, @Query("id") String gymid,
        @Query("model") String model);

    //获取教练排期
    @GET("/api/staffs/{id}/batches/?course__is_private=1") rx.Observable<QcResponseBtaches> qcGetTeacherBatches(@Path("id") String id,
        @Query("brand_id") String brand_id, @Query("teacher_id") String teacher_id);

    //获取工作人员列表
    @GET("/api/staffs/{id}/managers/?show_all=1") rx.Observable<QcResponseData<StaffShipResponse>> qcGetStaffs(@Path("id") String staff_id,
        @Query("id") String gym_id, @Query("model") String gym_model, @Query("brand_id") String brand_id, @Query("q") String keywork);

    //获取工作人员职位列表
    @GET("/api/staffs/{id}/positions/") rx.Observable<QcResponsePostions> qcGetPostions(@Path("id") String staff_id,
        @Query("id") String gym_id, @Query("model") String gym_model);

    @GET("/api/staffs/{id}/permissions/positions/") rx.Observable<QcResponsePostions> qcGetPermissionPostions(@Path("id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Query("key") String permission);

    /**
     * 排课
     */

    //获取团课排课
    @GET("/api/staffs/{id}/group/courses/") rx.Observable<QcResponseData<GroupCourseResponse>> qcGetGroupCourse(@Path("id") String staff_id,
        @Query("id") String gym_id, @Query("model") String gym_model, @Query("brand_id") String brand_id);

    //获取私教排课
    @GET("/api/staffs/{id}/private/coaches/") rx.Observable<QcResponsePrivateCourse> qcGetPrivateCrourse(@Path("id") String staff_id,
        @Query("id") String gym_id, @Query("model") String gym_model, @Query("brand_id") String brand_id);

    //获取教练排期
    @GET("/api/staffs/{id}/coaches/{coach_id}/batches/?course__is_private=1") rx.Observable<QcResponsePrivateDetail> qcGetPrivateCoaches(
        @Path("id") String staff_id, @Path("coach_id") String coach_id, @Query("id") String gym_id, @Query("model") String gym_model,
        @Query("brand_id") String brand_id);

    //获取团课排期
    @GET("/api/staffs/{id}/courses/{course_id}/batches/") rx.Observable<QcResponseData<GroupCourseScheduleDetail>> qcGetGroupCourses(
        @Path("id") String staff_id, @Path("course_id") String course_id, @Query("id") String gym_id, @Query("model") String gym_model,
        @Query("brand_id") String brand_id);

    //获取某个排期的详情
    @GET("/api/staffs/{id}/batches/{batch_id}/") rx.Observable<QcResponsePrivateBatchDetail> qcGetBatchDetail(@Path("id") String staff_id,
        @Path("batch_id") String batch_id, @Query("id") String gym_id, @Query("model") String gym_model,
        @Query("brand_id") String brand_id);

    //获取健身房课程
    @GET("/api/staffs/{id}/courses/?&show_all=1") rx.Observable<QcResponseData<CourseTypeSamples>> qcGetCourses(@Path("id") String staff_id,
        @Query("id") String gym_id, @Query("model") String gym_model, @Query("is_private") int is_private);

    //获取健身房课程权限
    @GET("/api/v2/staffs/{id}/method/courses/?&show_all=1") rx.Observable<QcResponseData<CourseTypeSamples>> qcGetCoursesPermission(
        @Path("id") String staff_id, @QueryMap HashMap<String, Object> params, @Query("is_private") int is_private);

    //获取健身房课程权限
    @GET("/api/v2/staffs/{id}/method/courses/?&show_all=1") rx.Observable<QcResponseData<CourseTypeSamples>> qcGetAllCoursesPermission(
        @Path("id") String staff_id, @QueryMap HashMap<String, Object> params);

    //获取健身房课程列表
    @GET("/api/v2/staffs/{id}/courses/?&show_all=1") rx.Observable<QcResponseData<CourseTypes>> qcGetCourses(@Path("id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Query("is_private") int is_private);

    @GET("/api/staffs/{id}/courses/?&show_all=1") rx.Observable<QcResponseData<CourseTypeSamples>> qcGetAllCourses(
        @Path("id") String staff_id, @Query("brand_id") String brandid, @Query("id") String gym_id, @Query("model") String gym_model);

    @GET("/api/staffs/{staff_id}/batches/{batch_id}/{type}/?order_by=start&show_all=1")
    rx.Observable<QcResponseData<CourseSchedules>> qcGetbatchSchedules(@Path("staff_id") String staff_id, @Path("batch_id") String batch_id,
        @Path("type") String type, @Query("brand_id") String brandid, @Query("id") String gymid, @Query("model") String gymmodel);

    //排课填充
    @GET("/api/staffs/{id}/{type}/arrange/template/") rx.Observable<QcResponseData<ScheduleTemplete>> qcGetBatchTemplate(
        @Path("id") String id, @Path("type") String type, @Query("id") String gymid, @Query("model") String gymmodel,
        @Query("teacher_id") String teacher_id, @Query("course_id") String course_id);

    @GET("/api/v2/staffs/{staff_id}/courses/photos/") rx.Observable<QcResponseData<QcResponseJacket>> qcGetJacket(
        @Path("staff_id") String id, @Query("course_id") String course_id, @QueryMap HashMap<String, Object> params);

    /**
     * 获取课程计划
     */
    @GET("/api/v2/staffs/{staff_id}/plantpls/?show_all=1") rx.Observable<QcResponseData<CoursePlans>> qcGetCoursePlan(
        @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

    /**
     * 获取课程下教练
     */
    @GET("/api/v2/staffs/{staff_id}/courses/teachers/") rx.Observable<QcResponseData<CourseTeacherResponse>> qcGetCourseTeacher(
        @Path("staff_id") String staff_id, @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

    /**
     * 课程下照片
     */
    @GET("/api/v2/staffs/{staff_id}/courses/schedules/photos/") rx.Observable<QcResponseSchedulePhotos> qcGetSchedulePhotos(
        @Path("staff_id") String staff_id, @Query("course_id") String id, @Query("page") int page,
        @QueryMap HashMap<String, Object> params);

    /**
     * 获取课程详情
     */
    @GET("/api/v2/staffs/{staff_id}/courses/") rx.Observable<QcResponseData<CourseTypeResponse>> qcGetCourseDetail(
        @Path("staff_id") String staff_id, @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

    /**
     * 分场馆评分
     */
    @GET("/api/v2/staffs/{staff_id}/courses/shops/score/") rx.Observable<QcResponseShopComment> qcGetShopComment(
        @Path("staff_id") String staff_id, @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

    @GET("/api/staffs/{id}/permissions/") rx.Observable<QcResponsePermission> qcPermission(@Path("id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 获取签到和更衣柜联动设置的id
     * {@link ShopConfigs}
     */
    @GET("/api/staffs/{staff_id}/shops/configs/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<SignInConfig.Data>> qcGetShopConfig(@Path("staff_id") String staff_id,
        @Query("keys") String key, @QueryMap HashMap<String, Object> params);

    /**
     * 签到扣费设置
     * http://192.168.1.7:8000/api/v2/staffs/3281/checkin/settings/?id=5370&model=staff_gym
     */
    @GET("/api/v2/staffs/{staff_id}/checkin/settings/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<SignInCardCostBean.Data>> qcGetSignInCostConfig(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 打开签到页面获取任务列表
     * http://192.168.1.106:8000/api/staffs/3281/checkin/tasklist/?id=5370&model=staff_gym
     */
    @GET("/api/v2/staffs/{staff_id}/checkin/tasklist/") rx.Observable<SignInTasks> qcGetSignInTasks(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 更衣柜
     */
    @GET("/api/v2/staffs/{staff_id}/lockers/?show_all=1") rx.Observable<QcResponseData<AllLockers>> qcGetAllLockers(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 获取更衣柜区域
     */
    @GET("/api/v2/staffs/{staff_id}/locker/regions/") rx.Observable<QcResponseData<LockerRegions>> qcGetAllRegion(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 签到详情
     * http://127.0.0.1:9000/api/v2/staffs/3281/checkin/?id=5370&model=staff_gym&checkin_id=69
     */
    @GET("/api/v2/staffs/{staff_id}/checkin/") rx.Observable<SignInDetail> qcGetCheckInDetail(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 3小时内签到记录
     * http://127.0.0.1:9000/api/v2/staffs/3281/checkin/latest/?id=5370&model=staff_gym&status=&page=
     */
    @GET("/api/v2/staffs/{staff_id}/checkin/latest/") rx.Observable<SignInTasks> qcGetSignInLog(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 获取学员当天预约课程
     * api/v2/staffs/3281/users/orders/?id=5227&model=staff_gym&user_id=1
     */
    @GET("/api/v2/staffs/{staff_id}/users/orders/") rx.Observable<SignInSchdule> qcGetStudentCourse(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 获取学员手动签出列表
     * http://192.168.1.106:8000/api/staffs/3281/checkin/?id=5370&model=staff_gym
     */
    @GET("/api/v2/staffs/{staff_id}/checkin/?order_by=-created_at") rx.Observable<SignInTasks> qcGetCheckInList(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 手动签到某个会员名下可用的会员卡/
     * api/v2/staffs/3288/checkin/cards/?id=5512&model=staff_gym&user_id=41
     */
    @GET("api/v2/staffs/{staff_id}/checkin/cards/") rx.Observable<SigninValidCard> qcGetStudentCards(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 签出签到的url
     * api/v2/staffs/%s/checkin/urls/
     */
    @GET("api/v2/staffs/{staff_id}/checkin/urls/") rx.Observable<SignInUrl> qcGetCheckinUrl(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 签到签出通知设置
     * /api/staffs/3281/checkin/notification/configs/?keys=app_checkin_notification 签到签出通知设置get接口
     */
    @GET("/api/staffs/{staff_id}/checkin/notification/configs/") rx.Observable<QcResponseData<SigninNoticeConfigs>> qcGetCheckinNotiConfigs(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    //余额不足是否提醒通知
    @GET("/api/staffs/{staff_id}/configs/") rx.Observable<QcResponseData<BalanceNotifyConfigs>> qcGetBalanceNotify(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 销售列表预览接口
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?brand_id=2&shop_id=1
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?id=5370&model=staff_gym
     */
    @GET("/api/v2/staffs/{staff_id}/sellers/preview/") rx.Observable<QcResponseData<AllotSalePreViews>> qcGetAllotSalesPreView(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 销售名下会员列表接口(无销售的会员列表不需要传seller_id)
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=2&seller_id=1
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym&seller_id=1
     */
    @GET("/api/v2/staffs/{staff_id}/sellers/users/") rx.Observable<QcResponseData<AllotSaleStudents>> qcGetAllotSaleOwenUsers(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 添加名下会员页:有权限查看的会员列表接口(名下会员+无销售会员)
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/all/?brand_id=2&shop_id=2&seller_id=1
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/all/?id=5370&model=staff_gym&seller_id=1
     */
    @GET("/api/v2/staffs/{staff_id}/sellers/users/all/") rx.Observable<QcResponseData<AllotSaleStudents>> qcGetAllotSaleUsersOfAddStudents(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 续费记录接口
     */
    @GET("/api/v2/staffs/{staff_id}/gyms/renews/") rx.Observable<QcResponseRenewalHistory> qcGetRenewalHistorys(
        @Path("staff_id") String staffid, @QueryMap HashMap<String, Object> params);

    /**
     * 会员跟进预览页
     * /api/staffs/:staff_id/users/tracking/?brand_id=&shop_id= 或者 id=&model=
     */
    @GET("/api/staffs/{staff_id}/users/track/glance/") rx.Observable<QcResponseData<StudentTrackPreview>> qcGetTrackStudentPreview(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 会员分状态列表
     * /api/staffs/:staff_id/status/users/?brand_id=&shop_id= 或者 id=&model=
     * GET参数:status, [star][end][seller_id(无销售seller_id=0)][recommend_user_id][字符串origin]
     */
    @Deprecated @GET("/api/staffs/{staff_id}/status/users/?show_all=1")
    rx.Observable<QcResponseData<TrackStudents>> qcGetTrackStudentsWithStatus(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 新增注册
     */
    @GET("/api/staffs/{staff_id}/users/new/create/") rx.Observable<QcResponseData<TrackStudents>> qcGetTrackStudentCreate(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 新增跟进
     */
    @GET("/api/staffs/{staff_id}/users/new/follow/") rx.Observable<QcResponseData<TrackStudents>> qcGetTrackStudentFollow(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 新增学员
     */
    @GET("/api/staffs/{staff_id}/users/new/member/") rx.Observable<QcResponseData<TrackStudents>> qcGetTrackStudentMember(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 今日新增详细列表
     * /api/staffs/:staff_id/status/users/new/?brand_id=&shop_id= 或者 id=&model=
     * GET参数:status, [star][end][seller_id(无销售seller_id=0)][recommend_user_id][字符串origin]
     */
    @Deprecated @GET("/api/staffs/{staff_id}/status/users/new/?show_all=1")
    rx.Observable<QcResponseData<TrackStudents>> qcGetTrackStudentsWithStatusToday(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 具有名下会员的销售列表
     * /api/staffs/:staff_id/filter/sellers/?brand_id=&shop_id= 或者 id=&model=
     */
    @GET("/api/staffs/{staff_id}/filter/sellers/?show_all=1") rx.Observable<QcResponseData<TrackSellers>> qcGetTrackStudentsFilterSalers(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 来源列表
     * /api/v2/staffs/:staff_id/users/origins/?brand_id=&shop_id= 或者 id=&model=
     */
    @GET("/api/staffs/{staff_id}/filter/origins/?show_all=1") rx.Observable<QcResponseData<TrackFilterOrigins>> qcGetTrackStudentsOrigins(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    //给会员添加的时候使用
    @GET("/api/v2/staffs/{staff_id}/users/origins/?show_all=1") rx.Observable<QcResponseData<TrackFilterOrigins>> qcGetUsersStudentsOrigins(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 推荐人列表
     * /api/staffs/:staff_id/users/recommends/?brand_id=&shop_id= 或者 id=&model=
     */
    @GET("/api/staffs/{staff_id}/users/recommends/?show_all=1") rx.Observable<QcResponseData<Referrers>> qcGetTrackStudentsRecommends(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 搜索推荐人
     * api/staffs/:staff_id/users/recommends/select/?brand_id=&shop_id= 或者 id=&model=
     * 参数:q={phone}{姓名}
     */
    @GET("/api/staffs/{staff_id}/users/recommends/select/?show_all=1")
    rx.Observable<QcResponseData<Referrers>> qcGetTrackStudentsRecommendsSelect(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 转换率
     * /api/staffs/:staff_id/users/conver/stat/?brand_id=&shop_id= 或者 id=&model=
     * GET参数:[start] [end] [seller_id(无销售seller_id=0)]
     */
    @GET("/api/staffs/{staff_id}/users/conver/stat/") rx.Observable<QcResponseData<FollowUpConver>> qcGetTrackStudentsConver(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 数据统计
     * /api/staffs/:staff_id/users/stat/?brand_id=&shop_id= 或者 id=&model=
     * GET参数:status,[start][end][seller_id(无销售seller_id=0)]
     */
    @GET("/api/staffs/{staff_id}/users/stat/") rx.Observable<QcResponseData<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 会员积分开关
     * /api/v2/staffs/:id/modules/
     */
    @GET("/api/v2/staffs/{staff_id}/modules/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<ScoreStatus>> qcGetStudentScoreStatus(@Path("staff_id") String staff_id,
        @QueryMap ArrayMap<String, String> params);

    /**
     * Staff - 模块-积分排行榜-查看列表
     * /api/v2/staffs/:id/scores/rank/
     */
    @GET("/api/v2/staffs/{staff_id}/scores/rank/?show_all=1")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<ScoreRankResponse>> qcGetStudentScoresRank(
        @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params);

    /**
     * Staff - 模块-积分规则-查看
     * /api/v2/staffs/:id/scores/rules/
     */
    @GET("/api/v2/staffs/{staff_id}/scores/rules/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<ScoreRuleResponse>> qcGetStudentScoreRules(
        @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params);

    /**
     * Staff - 模块-积分优惠奖励-查看列表
     * /api/v2/staffs/:id/scores/favors/
     */
    @GET("/api/v2/staffs/{staff_id}/scores/favors/?show_all=1")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<ScoreRuleAwardResponse>> qcGetStudentScoreAward(
        @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params);

    /**
     * Staff - 模块-积分历史-查看列表
     * /api/v2/staffs/:id/users/:user_id/scores/histories/
     */
    @GET("/api/v2/staffs/{staff_id}/users/{user_id}/scores/histories/?show_all=1&order_by=-created_at")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<ScoreHistoryResponse>> qcGetStudentScoreHistory(
        @Path("staff_id") String staff_id, @Path("user_id") String user_id, @QueryMap ArrayMap<String, String> params);

    /**
     * Staff - 模块-积分@用户-查看
     * /api/v2/staffs/:id/users/:user_id/scores/
     */
    @GET("/api/v2/staffs/{staff_id}/users/{user_id}/scores/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<Score>> qcGetStudentScore(@Path("staff_id") String staff_id,
        @Path("user_id") String user_id, @QueryMap ArrayMap<String, String> params);

    /**
     * Staff - 购卡后结算积分查询
     * /api/v2/staffs/:id/scores/calu/
     */

    @GET("/api/v2/staffs/{staff_id}/scores/calu/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<CacluScore>> qcGetScoreCalu(@Path("staff_id") String staff_id,
        @Query("type") String type, @Query("number") String money, @QueryMap ArrayMap<String, String> params);

    //获取余额不足会员卡总数
    @GET("api/staffs/{id}/balance/cards/count/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<BalanceCount>> qcGetCardCount(@Path("id") String staffId,
        @QueryMap HashMap<String, Object> params);

    //获取自动提醒配置
    @GET("api/staffs/{id}/shops/configs/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<NotityIsOpenConfigs>> qcGetNotifySetting(@Path("id") String staffId,
        @QueryMap HashMap<String, Object> params);

    /**
     * 短信部分
     */
    @GET("/api/staffs/{staffid}/group/messages/?order_by=-created_at") rx.Observable<QcResponseData<ShortMsgList>> qcQueryShortMsgList(
        @Path("staffid") String id, @IntRange(from = 1, to = 2) @Query("status") Integer status, @QueryMap HashMap<String, Object> params);

    @GET("/api/staffs/{staffid}/group/message/detail/") rx.Observable<QcResponseData<ShortMsgDetail>> qcQueryShortMsgDetail(
        @Path("staffid") String id, @Query("message_id") String messageid, @QueryMap HashMap<String, Object> params);

    /**
     * 聊天相关
     */
    @GET("/api/im/gym/contacts/") rx.Observable<QcResponseData<ChatFriendsData>> qcQueryChatFriends();

    /*
     *
      *  评论相关
     */
    @GET("/api/news/{news_id}/comment/") rx.Observable<QcResponseData<ArticleCommentListData>> qcQueryComments(@Path("news_id") String id,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/my/news/replies/") rx.Observable<QcResponseData<ArticleCommentListData>> qcQueryReplies(
        @QueryMap HashMap<String, Object> params);

    /**
     * 报名表个人列表
     * gym_id
     * competition_id
     */
    @GET("api/staffs/competitions/members/orders/?show_all=1")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<SignRecord>> qcGetSignPersonal(@QueryMap HashMap<String, Object> params,
        @Query("q") String keyword);

    /**
     * 个人报名表详情
     */
    @GET("api/staffs/competitions/members/orders/{orders}/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<SignUpDetailResponse>> qcGetSignDetail(@Path("orders") String orderId);

    /**
     * 报名分组列表
     * gym_id
     * competition_id
     */

    @GET("api/staffs/competitions/teams/?show_all=1")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<GroupListResponse>> qcGetGroupList(
        @QueryMap HashMap<String, Object> params, @Query("q") String keyword);

    /**
     * 小组详情
     */
    @GET("api/staffs/competitions/teams/{team_id}/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<TeamDetailResponse>> qcGetGroupDetail(@Path("team_id") String teamId);

    /**
     * 课程报表详情
     */
    @GET("/api/staffs/{staff_id}/reports/schedules/{schedule_id}/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<CourseReportDetail>> qcGetCourseReportDetail(
        @Path("staff_id") String staffId, @Path("schedule_id") String scheduleId, @QueryMap HashMap<String, Object> params);

    /**
     * 教练分配
     */
    @GET("api/v2/staffs/{staff_id}/coaches/preview/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<CoachResponseList>> qcGetCoachList(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    @GET("api/v2/staffs/{staff_id}/coaches/users/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<AllocateStudentBean>> qcGetCoachStudentDetail(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    @GET("/api/v2/staffs/{staff_id}/coaches/users/") rx.Observable<QcResponseData<AllocateStudentBean>> qcGetAllocateCoachStudents(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    @GET("/api/staffs/{staff_id}/coaches/") rx.Observable<cn.qingchengfit.network.response.QcResponseData<Staffs>> qcGetAllAllocateCoaches(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    //获取消息首页求职招聘信息列表
    @GET("/api/user/job/records/")
    rx.Observable<cn.qingchengfit.network.response.QcResponseData<RecordWrap>> qcGetRecruitMessageList();

}
