package cn.qingchengfit.staffkit.constant;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.login.bean.Login;
import cn.qingchengfit.login.bean.LoginBody;
import cn.qingchengfit.login.views.CheckProtocolModel;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User_Student;
import cn.qingchengfit.model.body.AddDayOffBody;
import cn.qingchengfit.model.body.AddFollowRecordBody;
import cn.qingchengfit.model.body.AddLockerBody;
import cn.qingchengfit.model.body.AheadOffDayBody;
import cn.qingchengfit.model.body.ArrangeBatchBody;
import cn.qingchengfit.model.body.BodyTestBody;
import cn.qingchengfit.model.body.BrandBody;
import cn.qingchengfit.model.body.CardBalanceNotifyBody;
import cn.qingchengfit.model.body.CardtplBody;
import cn.qingchengfit.model.body.ChangeBrandCreatorBody;
import cn.qingchengfit.model.body.ChangeSuBody;
import cn.qingchengfit.model.body.ChargeBody;
import cn.qingchengfit.model.body.ClearNotiBody;
import cn.qingchengfit.model.body.CourseBody;
import cn.qingchengfit.model.body.CreateCardBody;
import cn.qingchengfit.model.body.DelBatchScheduleBody;
import cn.qingchengfit.model.body.EditJacketBody;
import cn.qingchengfit.model.body.EditWardrobeBody;
import cn.qingchengfit.model.body.FixCard;
import cn.qingchengfit.model.body.GymBody;
import cn.qingchengfit.model.body.HireWardrobeBody;
import cn.qingchengfit.model.body.ManagerBody;
import cn.qingchengfit.model.body.OptionBody;
import cn.qingchengfit.model.body.PushBody;
import cn.qingchengfit.model.body.RenewBody;
import cn.qingchengfit.model.body.ReturnWardrobeBody;
import cn.qingchengfit.model.body.ScanBody;
import cn.qingchengfit.model.body.SignInBody;
import cn.qingchengfit.model.body.SignInCostBody;
import cn.qingchengfit.model.body.SignInIgnorBody;
import cn.qingchengfit.model.body.SignInManualBody;
import cn.qingchengfit.model.body.SignInNoticeConfigBody;
import cn.qingchengfit.model.body.SignOutBody;
import cn.qingchengfit.model.body.SingleBatchBody;
import cn.qingchengfit.model.body.UpdateCardValidBody;
import cn.qingchengfit.model.body.UpdateModule;
import cn.qingchengfit.model.common.Absentces;
import cn.qingchengfit.model.common.Attendances;
import cn.qingchengfit.model.common.Shop;
import cn.qingchengfit.model.responese.AllLockers;
import cn.qingchengfit.model.responese.AllotSalePreViews;
import cn.qingchengfit.model.responese.AllotSaleStudents;
import cn.qingchengfit.model.responese.AttendanceCharDataBean;
import cn.qingchengfit.model.responese.BalanceConfigs;
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
import cn.qingchengfit.model.responese.ClassRecords;
import cn.qingchengfit.model.responese.CoachResponse;
import cn.qingchengfit.model.responese.CoursePlans;
import cn.qingchengfit.model.responese.CourseReportDetail;
import cn.qingchengfit.model.responese.CourseSchedules;
import cn.qingchengfit.model.responese.CourseTeacherResponse;
import cn.qingchengfit.model.responese.CourseTypeResponse;
import cn.qingchengfit.model.responese.CourseTypeSamples;
import cn.qingchengfit.model.responese.CourseTypes;
import cn.qingchengfit.model.responese.CreatBrand;
import cn.qingchengfit.model.responese.DayOffs;
import cn.qingchengfit.model.responese.FollowRecords;
import cn.qingchengfit.model.responese.FollowUpConver;
import cn.qingchengfit.model.responese.GroupCourseResponse;
import cn.qingchengfit.model.responese.GroupCourseScheduleDetail;
import cn.qingchengfit.model.responese.GymCardtpl;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.GymExtra;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.model.responese.GymSites;
import cn.qingchengfit.model.responese.HomeInfo;
import cn.qingchengfit.model.responese.LockerRegions;
import cn.qingchengfit.model.responese.LockerWrapper;
import cn.qingchengfit.model.responese.Notification;
import cn.qingchengfit.model.responese.NotificationGlance;
import cn.qingchengfit.model.responese.NotityIsOpenConfigs;
import cn.qingchengfit.model.responese.QcResponToken;
import cn.qingchengfit.model.responese.QcResponseBtaches;
import cn.qingchengfit.model.responese.QcResponseCards;
import cn.qingchengfit.model.responese.QcResponseJacket;
import cn.qingchengfit.model.responese.QcResponseOption;
import cn.qingchengfit.model.responese.QcResponsePayWx;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.model.responese.QcResponsePostions;
import cn.qingchengfit.model.responese.QcResponsePrivateBatchDetail;
import cn.qingchengfit.model.responese.QcResponsePrivateCourse;
import cn.qingchengfit.model.responese.QcResponsePrivateDetail;
import cn.qingchengfit.model.responese.QcResponseRenew;
import cn.qingchengfit.model.responese.QcResponseRenewalHistory;
import cn.qingchengfit.model.responese.QcResponseSaleDetail;
import cn.qingchengfit.model.responese.QcResponseSchedulePhotos;
import cn.qingchengfit.model.responese.QcResponseServiceDetial;
import cn.qingchengfit.model.responese.QcResponseShopComment;
import cn.qingchengfit.model.responese.QcResponseSignInImg;
import cn.qingchengfit.model.responese.QcResponseStatementDetail;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.model.responese.QcResponseStudentInfo;
import cn.qingchengfit.model.responese.QcResponseSystenInit;
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
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.model.responese.StaffResponse;
import cn.qingchengfit.model.responese.StaffShipResponse;
import cn.qingchengfit.model.responese.Staffs;
import cn.qingchengfit.model.responese.StatementGlanceResp;
import cn.qingchengfit.model.responese.StudentTrackPreview;
import cn.qingchengfit.model.responese.Students;
import cn.qingchengfit.model.responese.TrackFilterOrigins;
import cn.qingchengfit.model.responese.TrackSellers;
import cn.qingchengfit.model.responese.TrackStudents;
import cn.qingchengfit.network.response.NotiSmsCountListWrap;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.network.response.ShopOrdersWrap;
import cn.qingchengfit.network.response.SmsListWrap;
import cn.qingchengfit.network.response.WxAuthorWrap;
import cn.qingchengfit.saasbase.cards.bean.BalanceCount;
import cn.qingchengfit.saasbase.cards.bean.QcResponseRealcardHistory;
import cn.qingchengfit.saasbase.cards.network.body.ShopsBody;
import cn.qingchengfit.saasbase.gymconfig.network.response.ShopConfigBody;
import cn.qingchengfit.saasbase.student.network.body.AddStudentBody;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.saasbase.turnovers.TurFilterResponse;
import cn.qingchengfit.saasbase.turnovers.TurOrderListDataWrapper;
import cn.qingchengfit.saasbase.turnovers.TurOrderListResponse;
import cn.qingchengfit.saasbase.turnovers.TurOrderSellerHistoryWrapper;
import cn.qingchengfit.saasbase.turnovers.TurnoversChartStatDataResponse;
import cn.qingchengfit.saascommon.model.FollowUpDataStatistic;
import cn.qingchengfit.staffkit.allocate.coach.model.AllocateStudentBean;
import cn.qingchengfit.staffkit.allocate.coach.model.CoachResponseList;
import cn.qingchengfit.staffkit.dianping.vo.DianPingGymInfo;
import cn.qingchengfit.staffkit.dianping.vo.DianPingShopContainer;
import cn.qingchengfit.staffkit.dianping.vo.GymFacilitiesList;
import cn.qingchengfit.staffkit.dianping.vo.GymTags;
import cn.qingchengfit.staffkit.train.model.CreateGroupBody;
import cn.qingchengfit.staffkit.train.model.GroupListResponse;
import cn.qingchengfit.staffkit.train.model.OperationMemberBody;
import cn.qingchengfit.staffkit.train.model.SignRecord;
import cn.qingchengfit.staffkit.train.model.SignUpDetailResponse;
import cn.qingchengfit.staffkit.train.model.TeamDetailResponse;
import cn.qingchengfit.staffkit.usecase.bean.CreatBrandBody;
import cn.qingchengfit.staffkit.usecase.bean.FeedBackBody;
import cn.qingchengfit.staffkit.usecase.bean.OutExcelBody;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.export.model.ExportRecordWrapper;
import cn.qingchengfit.staffkit.views.signin.zq.model.AccessBody;
import cn.qingchengfit.staffkit.views.signin.zq.model.GuardWrapper;
import cn.qingchengfit.staffkit.views.student.attendance.model.NotSignStudent;
import cn.qingchengfit.writeoff.vo.impl.SimpleSuccessResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface StaffAllApi {
  //获取token
  @GET("/api/csrftoken/") Call<QcResponToken> qcGetToken();

  /**
   * 品牌
   */
  @Deprecated @GET("/api/brands/") rx.Observable<BrandsResponse> qcGetBrands();

  @GET("/api/staffs/{staff_id}/brands/{id}/")
  rx.Observable<QcDataResponse<BrandResponse>> qcGetBrand(@Path("staff_id") String staff_id,
      @Path("id") String id);

  @GET("/api/staffs/{id}/brands/") rx.Observable<QcDataResponse<BrandsResponse>> qcGetBrands(
      @Path("id") String id);

  @GET("/api/staffs/{id}/services/") rx.Observable<QcDataResponse<GymList>> qcGetCoachService(
      @Path("id") String id, @Query("brand_id") String brand_id);

  @GET("/api/staffs/{id}/services/{sid}/") rx.Observable<ResponseService> qcGetService(
      @Path("id") String id, @Path("sid") String sid, @Query("model") String model);

  @GET("/api/staffs/{id}/check/superuser/") rx.Observable<QcDataResponse<ResponseSu>> qcCheckSu(
      @Path("id") String id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/staffs/{id}/gyms/welcome/") rx.Observable<QcDataResponse<GymDetail>> qcGetGymDetail(
      @Path("id") String staffid, @Query("id") String id, @Query("model") String model);

  @GET("/api/v2/staffs/{staff_id}/gyms/pay/")
  rx.Observable<QcDataResponse<RenewalList>> qcGetGymPay(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/staffs/{staff_id}/gym-extra/")
  rx.Observable<QcDataResponse<GymExtra>> qcGetGymExtra(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  //获取通知 分页和不分页接口 ,后者只为拿 未读
  @Deprecated @GET("/api/notifications/") rx.Observable<QcDataResponse<Notification>> qcGetMessages(
      @Query("staff_id") String id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/notifications/?order_by=-created_at")
  rx.Observable<QcDataResponse<Notification>> qcGetNotification(
      @QueryMap HashMap<String, Object> query);

  @GET("/api/v2/notifications/index/")
  rx.Observable<QcDataResponse<List<NotificationGlance>>> qcGetNotificationIndex(
      @Query("type_json") String query);

  /**
   * @param id
   * @param date
   * @return
   */
  @GET("/api/staffs/{id}/schedules/") rx.Observable<QcSchedulesResponse> qcGetSchedules(
      @Path("id") String id, @Query("date") String date, @QueryMap HashMap<String, Object> params);

  @GET("/api/staffs/{id}/{type}/{single_id}/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<SingleBatchData>> qcGetSingleBatch(
      @Path("id") String id, @Path("type") String type, @Path("single_id") String single_id,
      @QueryMap HashMap<String, Object> params);

  //工作人员详情
  @GET("/api/staffs/{id}/") rx.Observable<QcDataResponse<StaffResponse>> qcGetSelfInfo(
      @Path("id") String id);

  // 连锁
  @GET("/api/v2/staffs/{id}/android/welcome/")
  rx.Observable<QcDataResponse<HomeInfo>> qcWelcomeHome(@Path("id") String id,
      @Query("brand_id") String brandid);

  //工作人员下所有会员
  //    @GET("/api/staffs/{id}/users/all/?show_all=1")
  @GET("/api/staffs/{id}/users/?show_all=1")
  rx.Observable<QcDataResponse<Students>> qcGetAllStudents(@Path("id") String id,
      @QueryMap HashMap<String, Object> params);

  //工作人员下某家健身房会员
  @GET("/api/staffs/{id}/users/") rx.Observable<QcDataResponse<Students>> qcGetAllStudents(
      @Path("id") String id, @Query("id") String gymid, @Query("model") String model);

  //会员卡可绑定的会员列表
  @GET("/api/staffs/{staff_id}/method/users/?show_all=1")
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(
      @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

  /**
   * 缺勤列表
   *
   * @param params 缺勤<7： absence__lt=7，
   * 7-30天：absence__gte=7&absence__lte=30,
   * 缺勤>60天：absence__ge=60
   * @return "attendances": [{"user": {"username": "俞小西","gender": 1,"id": 2131,"avatar":
   * "http://zoneke-img.b0.upaiyun.com/9360bb9fb982a95c915edf44f611678f.jpeg!120x120","phone":
   * "18611985427"},"absence": 390,"date_and_time": "2016-01-30 13:30-14:30","id": 5933,"title":
   * "娜娜私教 杨娜娜"},]
   */
  @GET("/api/staffs/{staff_id}/users/absence/")
  rx.Observable<QcDataResponse<Absentces>> qcGetUsersAbsences(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  //获取缺勤图表数据
  @GET("/api/staffs/{id}/users/attendance/glance/")
  rx.Observable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(@Path("id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * @param params 必传start, end，
   * 可选排序字段 加 “-”说明是倒序
   * order_by=
   * days      -days
   * group     -group
   * private     -private
   * checkin   -checkin
   * @return "attendances": [{"checkin": 0,"group": 139,"user": {"username": "孙正其","gender": 0,"id":
   * 2219,"avatar":
   * "http://zoneke-img.b0.upaiyun.com/a15bec431224aa638a4b8eccb2e96955.jpg!120x120","phone":
   * "18536668518"},"private_count": 8,"days":
   * 142},
   */
  @GET("/api/staffs/{staff_id}/users/attendance/")
  Observable<QcDataResponse<Attendances>> qcGetUsersAttendances(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 日程安排
   */
  @GET("/api/staffs/{id}/actions/redirect/")
  rx.Observable<QcDataResponse<ScheduleActions>> qcGetScheduleAciton(@Path("id") String id,
      @Query("action") String action, @QueryMap HashMap<String, Object> params);

  /**
   * 卡类型
   */
  //工作人员 卡类型
  @GET("/api/v2/staffs/{id}/cardtpls/all/?show_all=1&order_by=-id")
  rx.Observable<QcDataResponse<CardTpls>> qcGetCardTpls(@Path("id") String id,
      @QueryMap HashMap<String, Object> params, @Query("type") String type,
      @Query("is_enable") String isEnable);

  // 卡类型
  @GET("/api/staffs/{id}/method/cardtpls/?show_all=1")
  rx.Observable<QcDataResponse<CardTpls>> qcGetCardTplsPermission(@Path("id") String id,
      @QueryMap HashMap<String, Object> parasm);

  //工作人员 卡类型详情
  @GET("/api/staffs/{Staff}/cardtpls/{id}/")
  rx.Observable<QcDataResponse<CardTplResponse>> qcGetCardTplsDetail(@Path("Staff") String staff,
      @Path("id") String id, @QueryMap HashMap<String, Object> parasm);

  // 工作人员 卡类型 规格
  @GET("/api/staffs/{staff_id}/cardtpls/{cardtps_id}/options/")
  rx.Observable<QcResponseOption> qcGetOptions(@Path("staff_id") String staff_id,
      @Path("cardtps_id") String cardtps_id, @Query("id") String gymid,
      @Query("model") String model, @Query("brand_id") String brand_id);

  //学员体测数据
  @GET("/api/staffs/{id}/users/{student_id}/measures/")
  rx.Observable<QcDataResponse<BodyTestPreviews>> qcGetStuedntBodyTest(

      @Path("id") String staffid, @Path("student_id") String student_id,
      @QueryMap HashMap<String, Object> params);

  //体测模板接口
  @GET("/api/measures/tpl/") rx.Observable<QcDataResponse<BodyTestTemplateData>> qcGetBodyTestModel(
      @QueryMap HashMap<String, Object> params);

  //获取体测数据
  @GET("/api/staffs/{id}/measures/{measure_id}/")
  rx.Observable<QcDataResponse<BodyTestMeasureData>> qcGetBodyTest(@Path("id") String staffid,
      @Path("measure_id") String measure_id, @QueryMap HashMap<String, Object> params);

  //获取某个健身房的教练列表
  @GET("/api/staffs/{id}/method/coaches/")
  rx.Observable<QcDataResponse<Staffs>> qcGetGymCoachesPermission(@Path("id") String id,
      @QueryMap HashMap<String, Object> params);

  //获取某个健身房的场地列表
  @GET("/api/staffs/{id}/spaces/") rx.Observable<QcDataResponse<GymSites>> qcGetGymSites(
      @Path("id") String id, @Query("id") String gymid, @Query("model") String model);

  //获取某个健身房的场地列表
  @GET("/api/staffs/{id}/method/spaces/")
  rx.Observable<QcDataResponse<GymSites>> qcGetGymSitesPermisson(@Path("id") String id,
      @QueryMap HashMap<String, Object> params);

  @GET("/api/staffs/{id}/shops/") rx.Observable<QcDataResponse<Shops>> qcGetBrandShops(
      @Path("id") String id, @Query("brand_id") String brand_id);

  @GET("/api/staffs/{id}/shops/detail") rx.Observable<QcDataResponse<ShopDetail>> qcGetShopDetail(
      @Path("id") String id, @QueryMap HashMap<String, Object> params);
  //某品牌下所有场馆 ?????

  @GET("/api/staffs/{id}/brands/{brand_id}/shops/")
  rx.Observable<QcDataResponse<Shops>> qcGetBrandAllShops(@Path("id") String id,
      @Path("brand_id") String brand_id);

  //获取某个健身房的卡模板
  @GET("/api/staffs/{id}/cardtpls/?show_all=1")
  rx.Observable<QcDataResponse<GymCardtpl>> qcGetGymCardtpl(@Path("id") String id,
      @Query("id") String gymid, @Query("model") String model, @Query("type") String type);

  //获取某个学员的基本信息
  @GET("/api/v2/staffs/{staff_id}/users/{id}/")
  rx.Observable<QcResponseStudentInfo> qcGetStudentInfo(@Path("staff_id") String staffid,
      @Path("id") String studentid, @Query("id") String gymid, @Query("model") String model,
      @Query("brand_id") String brand_id);

  //获取某个学员的课程
  @GET("/api/staffs/{staff_id}/users/attendance/records/")
  rx.Observable<QcDataResponse<ClassRecords>> qcGetStudentClassRecords(
      @Path("staff_id") String staffid, @Query("user_id") String studentid,
      @QueryMap HashMap<String, Object> params);

  //获取某个学员的cardlist
  @GET("/api/staffs/{staff_id}/users/{id}/cards/?order_by=-id")
  rx.Observable<QcResponseStudentCards> qcGetStudentCards(@Path("staff_id") String staffid,
      @Path("id") String studentid, @Query("id") String gymid, @Query("model") String model,
      @Query("brand_id") String brand_id);

  //获取某个学员的cardlist
  @GET("/api/staffs/{staff_id}/users/{id}/cards/?order_by=-id")
  rx.Observable<QcResponseStudentCards> qcGetStudentCardsWithShopId(
      @Path("staff_id") String staffid, @Path("id") String studentid,
      @QueryMap HashMap<String, Object> params);
  //@GET("/api/staffs/{staff_id}/users/{id}/cards/?order_by=-id") rx.Observable<QcResponseStudentCards> qcGetStudentCardsWithShopId(
  //    @Path("staff_id") String staffid, @Path("id") String studentid, @Query("id") String gymid, @Query("model") String model,
  //    @Query("brand_id") String brand_id, @Query("shop_id") String shop_id);

  //获取某个学员的跟进记录
  @GET("/api/staffs/{id}/users/{student_id}/records/?format=app")
  rx.Observable<QcDataResponse<FollowRecords>> qcGetStudentFollow(@Path("id") String staffid,
      @Path("student_id") String user_id, @Query("id") String gymid, @Query("model") String model,
      @Query("brand_id") String brand_id, @Query("page") int page);

  //学员签到照片
  @GET("/api/staffs/{staff_id}/user/photos/") rx.Observable<QcResponseSignInImg> qcGetSignInImages(
      @Path("staff_id") String staffid, @Query("user_id") String user_id,
      @QueryMap HashMap<String, Object> params);

  //获取消费记录
  @GET("/api/staffs/{id}/cards/{card_id}/histories/?order_by=-created_at")
  rx.Observable<QcResponseRealcardHistory> qcGetCardhistory(@Path("id") String staffid,
      @Path("card_id") String card_id, @Query("brand_id") String brand_id,
      @Query("id") String gymid, @Query("model") String gymodel,
      @Query("created_at__gte") String start, @Query("created_at__lte") String end,
      @Query("page") int page

  );

  /**
   * 会员卡
   */
  //获取真实卡列表
  @GET("/api/staffs/{id}/cards/?order_by=-id") rx.Observable<Cards> qcGetAllCard(
      @Path("id") String staffid);

  //获取会员卡
  @GET("/api/staffs/{id}/cards/all/?order_by=-id")
  rx.Observable<QcDataResponse<Cards>> qcGetBrandCard(@Path("id") String staffid,
      @Query("brand_id") String brand_id, @Query("id") String gymid,
      @Query("model") String gymmodel, @Query("page") int page, @Query("q") String keyword,
      @QueryMap HashMap<String, Object> params);

  //获取会员卡
  @GET("api/staffs/{id}/balance/cards/") rx.Observable<QcDataResponse<Cards>> qcGetBalanceCard(
      @Path("id") String staffid, @Query("brand_id") String brand_id, @Query("id") String gymid,
      @Query("model") String gymmodel, @Query("page") int page, @Query("q") String keyword,
      @QueryMap HashMap<String, Object> params);

  //获取卡详情
  @GET("/api/staffs/{id}/cards/{card_id}/")
  rx.Observable<QcDataResponse<CardResponse>> qcGetCardDetail(@Path("id") String staff,
      @Path("card_id") String card_id, @QueryMap HashMap<String, Object> params);

  @GET("/api/staffs/{id}/cards/bind/users/")
  rx.Observable<QcDataResponse<CardBindStudents>> qcGetBindStudent(@Path("id") String staff,
      @QueryMap HashMap<String, Object> params);

  //获取筛选列表
  @GET("/api/staffs/{id}/filter/cardtpls/?show_all=1")
  rx.Observable<QcDataResponse<CardTpls>> qcGetCardFilterCondition(@Path("id") String staff,
      @QueryMap HashMap<String, Object> params);

  //设置余额条件
  @GET("api/v2/staffs/{id}/users/configs/")
  rx.Observable<QcDataResponse<BalanceConfigs>> qcGetBalanceCondition(@Path("id") String staffId,
      @QueryMap HashMap<String, Object> params, @Query("keys") String permission);

  /**
   * 会员卡模板
   */
  @GET("/api/staffs/{id}/cardtpls/all/")
  rx.Observable<QcDataResponse<GymCardtpl>> qcGetBrandCardtpl(@Path("id") String staffid,
      @Query("brand_id") String brand_id);

  //获取请假列表
  @GET("/api/staffs/{id}/leaves/?order_by=-created_at")
  rx.Observable<QcDataResponse<DayOffs>> qcGetDayOff(@Path("id") String staffid,
      @Query("brand_id") String brandid, @Query("card_id") String card_id,
      @Query("id") String gymid, @Query("model") String model);

  @GET("/api/staffs/{id}/reports/schedules/glance/")
  rx.Observable<QcDataResponse<StatementGlanceResp>> qcGetReportGlance(@Path("id") String id,
      @Query("brand_id") String brand_id, @Query("shop_id") String shop_id,
      @Query("id") String gymid, @Query("model") String model);

  @GET("/api/staffs/{id}/reports/sells/glance/")
  rx.Observable<QcDataResponse<StatementGlanceResp>> qcGetSaleGlance(@Path("id") String id,
      @Query("brand_id") String brand_id, @Query("shop_id") String shop_id,
      @Query("id") String gymid, @Query("model") String model);

  /**
   * 签到报表预览
   * 连锁运营：http://127.0.0.1:9000/api/staffs/3281/reports/checkin/glance/?brand_id=2
   * 可选参数 shop_id=
   * 单店：http://127.0.0.1:9000/api/staffs/3281/reports/checkin/glance/?model=staff_gym&id=5370
   *
   * @return Observable
   */
  @GET("/api/staffs/{staff_id}/reports/checkin/glance/")
  rx.Observable<QcDataResponse<StatementGlanceResp>> qcGetSigninGlance(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 签到报表详情
   * http://127.0.0.1:9000/api/staffs/3281/reports/checkin/?brand_id=2&start=2016-09-01&end=2016-09-21
   * 可选参数 shop_id
   * 单店：http://127.0.0.1:9000/api/staffs/3281/reports/checkin/?id=5370&model=staff_gym&start=2016-09-01&end=2016-09-21
   *
   * @return Observable
   */
  @GET("/api/staffs/{staff_id}/reports/checkin/")
  rx.Observable<QcDataResponse<SigninReportDetail>> qcGetSigninReportDetail(
      @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v3/staffs/{id}/reports/schedules/")
  rx.Observable<QcResponseStatementDetail> qcGetStatementDatail(@Path("id") String id,
      @Query("start") String start, @Query("end") String end,
      @Query("teacher_id") String teacher_id, @Query("course_id") String course_id,
      @Query("course_extra") String course_extra,

      @Query("shop_id") String shop_id, @Query("brand_id") String brand_id,
      @Query("id") String gymid, @Query("model") String gymmodel);

  @GET("/api/v2/staffs/{id}/reports/sells/") rx.Observable<QcResponseSaleDetail> qcGetSaleDatail(
      @Path("id") String id,

      @Query("start") String start, @Query("end") String end, @Query("shop_id") String system_id,

      @Query("card__card_tpl_id") String card_tpl_id, @Query("cards_extra") String cards_extra,
      @Query("seller_id") String seller_id, @Query("type") String type,
      @Query("charge_type") String chargetype, @QueryMap HashMap<String, Object> params);

  //获取教练课程
  @GET("/api/v1/services/detail/") rx.Observable<QcResponseServiceDetial> qcGetServiceDetail(
      @Query("model") String model, @Query("id") String id);

  // 未使用
  @GET("/api/v1/coaches/{id}/reports/sale/cardtpls/") rx.Observable<QcResponseCards> qcGetSaleCard(
      @Path("id") String id);

  //获取销售 卖卡  包含销售和教练
  @GET("/api/staffs/{staff_id}/sellers/")
  rx.Observable<QcDataResponse<Sellers>> qcGetSalersAndCoach(@Path("staff_id") String staff_id,
      @Query("brand_id") String brandid, @Query("shop_id") String shopid, @Query("id") String gymid,
      @Query("model") String model);

  //获取销售 卖卡  包含销售 不包含教练
  @GET("/api/staffs/{staff_id}/sellers-without-coach/")
  rx.Observable<QcDataResponse<Sellers>> qcGetSalers(@Path("staff_id") String staff_id,
      @Query("brand_id") String brandid, @Query("shop_id") String shopid, @Query("id") String gymid,
      @Query("model") String model);

  //获取教练排期
  @GET("/api/staffs/{id}/batches/?course__is_private=1")
  rx.Observable<QcResponseBtaches> qcGetTeacherBatches(@Path("id") String id,
      @Query("brand_id") String brand_id, @Query("teacher_id") String teacher_id);

  //获取工作人员列表
  @GET("/api/staffs/{id}/managers/?show_all=1")
  rx.Observable<QcDataResponse<StaffShipResponse>> qcGetStaffs(@Path("id") String staff_id,
      @Query("id") String gym_id, @Query("model") String gym_model,
      @Query("brand_id") String brand_id, @Query("q") String keywork);

  //获取工作人员职位列表
  @GET("/api/staffs/{id}/positions/") rx.Observable<QcResponsePostions> qcGetPostions(
      @Path("id") String staff_id, @Query("id") String gym_id, @Query("model") String gym_model);

  @GET("/api/staffs/{id}/permissions/positions/")
  rx.Observable<QcResponsePostions> qcGetPermissionPostions(@Path("id") String staff_id,
      @QueryMap HashMap<String, Object> params, @Query("key") String permission);

  /**
   * 排课
   */

  //获取团课排课
  @GET("/api/staffs/{id}/group/courses/")
  rx.Observable<QcDataResponse<GroupCourseResponse>> qcGetGroupCourse(@Path("id") String staff_id,
      @Query("id") String gym_id, @Query("model") String gym_model,
      @Query("brand_id") String brand_id);

  //获取私教排课
  @GET("/api/staffs/{id}/private/coaches/")
  rx.Observable<QcResponsePrivateCourse> qcGetPrivateCrourse(@Path("id") String staff_id,
      @Query("id") String gym_id, @Query("model") String gym_model,
      @Query("brand_id") String brand_id);

  //获取教练排期
  @GET("/api/staffs/{id}/coaches/{coach_id}/batches/?course__is_private=1")
  rx.Observable<QcResponsePrivateDetail> qcGetPrivateCoaches(@Path("id") String staff_id,
      @Path("coach_id") String coach_id, @Query("id") String gym_id,
      @Query("model") String gym_model, @Query("brand_id") String brand_id);

  //获取团课排期
  @GET("/api/staffs/{id}/courses/{course_id}/batches/")
  rx.Observable<QcDataResponse<GroupCourseScheduleDetail>> qcGetGroupCourses(
      @Path("id") String staff_id, @Path("course_id") String course_id, @Query("id") String gym_id,
      @Query("model") String gym_model, @Query("brand_id") String brand_id);

  //获取某个排期的详情
  @GET("/api/staffs/{id}/batches/{batch_id}/")
  rx.Observable<QcResponsePrivateBatchDetail> qcGetBatchDetail(@Path("id") String staff_id,
      @Path("batch_id") String batch_id, @Query("id") String gym_id,
      @Query("model") String gym_model, @Query("brand_id") String brand_id);

  //获取健身房课程
  @GET("/api/staffs/{id}/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetCourses(@Path("id") String staff_id,
      @Query("id") String gym_id, @Query("model") String gym_model,
      @Query("is_private") int is_private);

  //获取健身房课程权限
  @GET("/api/v2/staffs/{id}/method/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetCoursesPermission(
      @Path("id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Query("is_private") int is_private);

  //获取健身房课程权限
  @GET("/api/v2/staffs/{id}/method/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetAllCoursesPermission(
      @Path("id") String staff_id, @QueryMap HashMap<String, Object> params);

  //获取健身房课程列表
  @GET("/api/v2/staffs/{id}/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseTypes>> qcGetCourses(@Path("id") String staff_id,
      @QueryMap HashMap<String, Object> params, @Query("is_private") int is_private);

  @GET("/api/staffs/{id}/courses/?&show_all=1")
  rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetAllCourses(@Path("id") String staff_id,
      @Query("brand_id") String brandid, @Query("id") String gym_id,
      @Query("model") String gym_model);

  @GET("/api/staffs/{staff_id}/batches/{batch_id}/{type}/?order_by=start&show_all=1")
  rx.Observable<QcDataResponse<CourseSchedules>> qcGetbatchSchedules(
      @Path("staff_id") String staff_id, @Path("batch_id") String batch_id,
      @Path("type") String type, @Query("brand_id") String brandid, @Query("id") String gymid,
      @Query("model") String gymmodel);

  //排课填充
  @GET("/api/staffs/{id}/{type}/arrange/template/")
  rx.Observable<QcDataResponse<ScheduleTemplete>> qcGetBatchTemplate(@Path("id") String id,
      @Path("type") String type, @Query("id") String gymid, @Query("model") String gymmodel,
      @Query("teacher_id") String teacher_id, @Query("course_id") String course_id);

  @GET("/api/v2/staffs/{staff_id}/courses/photos/")
  rx.Observable<QcDataResponse<QcResponseJacket>> qcGetJacket(@Path("staff_id") String id,
      @Query("course_id") String course_id, @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程计划
   */
  @GET("/api/v2/staffs/{staff_id}/plantpls/?show_all=1")
  rx.Observable<QcDataResponse<CoursePlans>> qcGetCoursePlan(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程下教练
   */
  @GET("/api/v2/staffs/{staff_id}/courses/teachers/")
  rx.Observable<QcDataResponse<CourseTeacherResponse>> qcGetCourseTeacher(
      @Path("staff_id") String staff_id, @Query("course_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 课程下照片
   */
  @GET("/api/v2/staffs/{staff_id}/courses/schedules/photos/")
  rx.Observable<QcResponseSchedulePhotos> qcGetSchedulePhotos(@Path("staff_id") String staff_id,
      @Query("course_id") String id, @Query("page") int page,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取课程详情
   */
  @GET("/api/v2/staffs/{staff_id}/courses/")
  rx.Observable<QcDataResponse<CourseTypeResponse>> qcGetCourseDetail(
      @Path("staff_id") String staff_id, @Query("course_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 分场馆评分
   */
  @GET("/api/v2/staffs/{staff_id}/courses/shops/score/")
  rx.Observable<QcResponseShopComment> qcGetShopComment(@Path("staff_id") String staff_id,
      @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

  @GET("/api/staffs/{id}/permissions/") rx.Observable<QcResponsePermission> qcPermission(
      @Path("id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 获取签到和更衣柜联动设置的id
   * {@link ShopConfigs}
   */
  @GET("/api/staffs/{staff_id}/shops/configs/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<SignInConfig.Data>> qcGetShopConfig(
      @Path("staff_id") String staff_id, @Query("keys") String key,
      @QueryMap HashMap<String, Object> params);

  /**
   * 签到扣费设置
   * http://192.168.1.7:8000/api/v2/staffs/3281/checkin/settings/?id=5370&model=staff_gym
   */
  @GET("/api/v2/staffs/{staff_id}/checkin/settings/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<SignInCardCostBean.Data>> qcGetSignInCostConfig(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 打开签到页面获取任务列表
   * http://192.168.1.106:8000/api/staffs/3281/checkin/tasklist/?id=5370&model=staff_gym
   */
  @GET("/api/v2/staffs/{staff_id}/checkin/tasklist/") rx.Observable<SignInTasks> qcGetSignInTasks(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 更衣柜
   */
  @GET("/api/v2/staffs/{staff_id}/lockers/?show_all=1")
  rx.Observable<QcDataResponse<AllLockers>> qcGetAllLockers(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取更衣柜区域
   */
  @GET("/api/v2/staffs/{staff_id}/locker/regions/")
  rx.Observable<QcDataResponse<LockerRegions>> qcGetAllRegion(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 签到详情
   * http://127.0.0.1:9000/api/v2/staffs/3281/checkin/?id=5370&model=staff_gym&checkin_id=69
   */
  @GET("/api/v2/staffs/{staff_id}/checkin/") rx.Observable<SignInDetail> qcGetCheckInDetail(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 3小时内签到记录
   * http://127.0.0.1:9000/api/v2/staffs/3281/checkin/latest/?id=5370&model=staff_gym&status=&page=
   */
  @GET("/api/v2/staffs/{staff_id}/checkin/latest/") rx.Observable<SignInTasks> qcGetSignInLog(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 获取学员当天预约课程
   * api/v2/staffs/3281/users/orders/?id=5227&model=staff_gym&user_id=1
   */
  @GET("/api/v2/staffs/{staff_id}/users/orders/") rx.Observable<SignInSchdule> qcGetStudentCourse(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 获取学员手动签出列表
   * http://192.168.1.106:8000/api/staffs/3281/checkin/?id=5370&model=staff_gym
   */
  @GET("/api/v2/staffs/{staff_id}/checkin/?order_by=-created_at")
  rx.Observable<SignInTasks> qcGetCheckInList(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 手动签到某个会员名下可用的会员卡/
   * api/v2/staffs/3288/checkin/cards/?id=5512&model=staff_gym&user_id=41
   */
  @GET("api/v2/staffs/{staff_id}/checkin/cards/") rx.Observable<SigninValidCard> qcGetStudentCards(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 签出签到的url
   * api/v2/staffs/%s/checkin/urls/
   */
  @GET("api/v2/staffs/{staff_id}/checkin/urls/") rx.Observable<SignInUrl> qcGetCheckinUrl(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 签到签出通知设置
   * /api/staffs/3281/checkin/notification/configs/?keys=app_checkin_notification 签到签出通知设置get接口
   */
  @GET("/api/staffs/{staff_id}/checkin/notification/configs/")
  rx.Observable<QcDataResponse<SigninNoticeConfigs>> qcGetCheckinNotiConfigs(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //余额不足是否提醒通知
  @GET("/api/staffs/{staff_id}/configs/")
  rx.Observable<QcDataResponse<BalanceNotifyConfigs>> qcGetBalanceNotify(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 销售列表预览接口
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?brand_id=2&shop_id=1
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?id=5370&model=staff_gym
   */
  @GET("/api/v2/staffs/{staff_id}/sellers/preview/")
  rx.Observable<QcDataResponse<AllotSalePreViews>> qcGetAllotSalesPreView(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 销售名下会员列表接口(无销售的会员列表不需要传seller_id)
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=2&seller_id=1
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym&seller_id=1
   */
  @GET("/api/v2/staffs/{staff_id}/sellers/users/")
  rx.Observable<QcDataResponse<AllotSaleStudents>> qcGetAllotSaleOwenUsers(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 添加名下会员页:有权限查看的会员列表接口(名下会员+无销售会员)
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/all/?brand_id=2&shop_id=2&seller_id=1
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/all/?id=5370&model=staff_gym&seller_id=1
   */
  @GET("/api/v2/staffs/{staff_id}/sellers/users/all/")
  rx.Observable<QcDataResponse<AllotSaleStudents>> qcGetAllotSaleUsersOfAddStudents(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 续费记录接口
   */
  @GET("/api/v2/staffs/{staff_id}/gyms/renews/")
  rx.Observable<QcResponseRenewalHistory> qcGetRenewalHistorys(@Path("staff_id") String staffid,
      @QueryMap HashMap<String, Object> params);

  /**
   * 会员跟进预览页
   * /api/staffs/:staff_id/users/tracking/?brand_id=&shop_id= 或者 id=&model=
   */
  @GET("/api/staffs/{staff_id}/users/track/glance/")
  rx.Observable<QcDataResponse<StudentTrackPreview>> qcGetTrackStudentPreview(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 会员分状态列表
   * /api/staffs/:staff_id/status/users/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:status, [star][end][seller_id(无销售seller_id=0)][recommend_user_id][字符串origin]
   */
  @Deprecated @GET("/api/staffs/{staff_id}/status/users/?show_all=1")
  rx.Observable<QcDataResponse<TrackStudents>> qcGetTrackStudentsWithStatus(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 新增注册
   */
  @GET("/api/staffs/{staff_id}/users/new/create/")
  rx.Observable<QcDataResponse<TrackStudents>> qcGetTrackStudentCreate(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 新增跟进
   */
  @GET("/api/staffs/{staff_id}/users/new/follow/")
  rx.Observable<QcDataResponse<TrackStudents>> qcGetTrackStudentFollow(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 新增学员
   */
  @GET("/api/staffs/{staff_id}/users/new/member/")
  rx.Observable<QcDataResponse<TrackStudents>> qcGetTrackStudentMember(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 今日新增详细列表
   * /api/staffs/:staff_id/status/users/new/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:status, [star][end][seller_id(无销售seller_id=0)][recommend_user_id][字符串origin]
   */
  @Deprecated @GET("/api/staffs/{staff_id}/status/users/new/?show_all=1")
  rx.Observable<QcDataResponse<TrackStudents>> qcGetTrackStudentsWithStatusToday(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 具有名下会员的销售列表
   * /api/staffs/:staff_id/filter/sellers/?brand_id=&shop_id= 或者 id=&model=
   */
  @GET("/api/staffs/{staff_id}/filter/sellers/?show_all=1")
  rx.Observable<QcDataResponse<TrackSellers>> qcGetTrackStudentsFilterSalers(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 来源列表
   * /api/v2/staffs/:staff_id/users/origins/?brand_id=&shop_id= 或者 id=&model=
   */
  @GET("/api/staffs/{staff_id}/filter/origins/?show_all=1")
  rx.Observable<QcDataResponse<TrackFilterOrigins>> qcGetTrackStudentsOrigins(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //给会员添加的时候使用
  @GET("/api/v2/staffs/{staff_id}/users/origins/?show_all=1")
  rx.Observable<QcDataResponse<TrackFilterOrigins>> qcGetUsersStudentsOrigins(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 推荐人列表
   * /api/staffs/:staff_id/users/recommends/?brand_id=&shop_id= 或者 id=&model=
   */
  @GET("/api/staffs/{staff_id}/users/recommends/?show_all=1")
  rx.Observable<QcDataResponse<Referrers>> qcGetTrackStudentsRecommends(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 搜索推荐人
   * api/staffs/:staff_id/users/recommends/select/?brand_id=&shop_id= 或者 id=&model=
   * 参数:q={phone}{姓名}
   */
  @GET("/api/staffs/{staff_id}/users/recommends/select/?show_all=1")
  rx.Observable<QcDataResponse<Referrers>> qcGetTrackStudentsRecommendsSelect(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 转换率
   * /api/staffs/:staff_id/users/conver/stat/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:[start] [end] [seller_id(无销售seller_id=0)]
   */
  @GET("/api/staffs/{staff_id}/users/conver/stat/")
  rx.Observable<QcDataResponse<FollowUpConver>> qcGetTrackStudentsConver(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 数据统计
   * /api/staffs/:staff_id/users/stat/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:status,[start][end][seller_id(无销售seller_id=0)]
   */
  @GET("/api/staffs/{staff_id}/users/stat/")
  rx.Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 会员积分开关
   * /api/v2/staffs/:id/modules/
   */
  @GET("/api/v2/staffs/{staff_id}/modules/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<ScoreStatus>> qcGetStudentScoreStatus(
      @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params);

  /**
   * Staff - 模块-积分排行榜-查看列表
   * /api/v2/staffs/:id/scores/rank/
   */
  @GET("/api/v2/staffs/{staff_id}/scores/rank/?show_all=1")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<ScoreRankResponse>> qcGetStudentScoresRank(
      @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params);

  /**
   * Staff - 模块-积分规则-查看
   * /api/v2/staffs/:id/scores/rules/
   */
  @GET("/api/v2/staffs/{staff_id}/scores/rules/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<ScoreRuleResponse>> qcGetStudentScoreRules(
      @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params);

  /**
   * Staff - 模块-积分优惠奖励-查看列表
   * /api/v2/staffs/:id/scores/favors/
   */
  @GET("/api/v2/staffs/{staff_id}/scores/favors/?show_all=1")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<ScoreRuleAwardResponse>> qcGetStudentScoreAward(
      @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params);

  /**
   * Staff - 模块-积分历史-查看列表
   * /api/v2/staffs/:id/users/:user_id/scores/histories/
   */
  @GET("/api/v2/staffs/{staff_id}/users/{user_id}/scores/histories/?show_all=1&order_by=-created_at")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<ScoreHistoryResponse>> qcGetStudentScoreHistory(
      @Path("staff_id") String staff_id, @Path("user_id") String user_id,
      @QueryMap ArrayMap<String, String> params);

  /**
   * Staff - 模块-积分@用户-查看
   * /api/v2/staffs/:id/users/:user_id/scores/
   */
  @GET("/api/v2/staffs/{staff_id}/users/{user_id}/scores/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<Score>> qcGetStudentScore(
      @Path("staff_id") String staff_id, @Path("user_id") String user_id,
      @QueryMap ArrayMap<String, String> params);

  /**
   * Staff - 购卡后结算积分查询
   * /api/v2/staffs/:id/scores/calu/
   */

  @GET("/api/v2/staffs/{staff_id}/scores/calu/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<CacluScore>> qcGetScoreCalu(
      @Path("staff_id") String staff_id, @Query("type") String type, @Query("number") String money,
      @QueryMap ArrayMap<String, String> params);

  //获取余额不足会员卡总数
  @GET("api/staffs/{id}/balance/cards/count/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<BalanceCount>> qcGetCardCount(
      @Path("id") String staffId, @QueryMap HashMap<String, Object> params);

  //获取自动提醒配置
  @GET("api/staffs/{id}/shops/configs/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<NotityIsOpenConfigs>> qcGetNotifySetting(
      @Path("id") String staffId, @QueryMap HashMap<String, Object> params);

  /**
   * 报名表个人列表
   * gym_id
   * competition_id
   */
  @GET("api/staffs/competitions/members/orders/?show_all=1")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<SignRecord>> qcGetSignPersonal(
      @QueryMap HashMap<String, Object> params, @Query("q") String keyword);

  /**
   * 个人报名表详情
   */
  @GET("api/staffs/competitions/members/orders/{orders}/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<SignUpDetailResponse>> qcGetSignDetail(
      @Path("orders") String orderId);

  /**
   * 报名分组列表
   * gym_id
   * competition_id
   */

  @GET("api/staffs/competitions/teams/?show_all=1")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<GroupListResponse>> qcGetGroupList(
      @QueryMap HashMap<String, Object> params, @Query("q") String keyword);

  /**
   * 小组详情
   */
  @GET("api/staffs/competitions/teams/{team_id}/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<TeamDetailResponse>> qcGetGroupDetail(
      @Path("team_id") String teamId);

  /**
   * 课程报表详情
   */
  @GET("/api/staffs/{staff_id}/reports/schedules/{schedule_id}/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<CourseReportDetail>> qcGetCourseReportDetail(
      @Path("staff_id") String staffId, @Path("schedule_id") String scheduleId,
      @QueryMap HashMap<String, Object> params);

  /**
   * 教练分配
   */
  @GET("api/v2/staffs/{staff_id}/coaches/preview/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<CoachResponseList>> qcGetCoachList(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  @GET("api/v2/staffs/{staff_id}/coaches/users/")
  rx.Observable<QcDataResponse<AllocateStudentBean>> qcGetCoachStudentDetail(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/staffs/{staff_id}/coaches/users/")
  rx.Observable<QcDataResponse<AllocateStudentBean>> qcGetAllocateCoachStudents(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/staffs/{staff_id}/coaches/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<Staffs>> qcGetAllAllocateCoaches(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //导出记录
  @GET("/api/staffs/{staff_id}/export/records/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<ExportRecordWrapper>> qcGetExportRecord(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/staffs/{staff_id}/messages/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<SmsListWrap>> qcGetNotiSMSs(
      @Path("staff_id") String staffid, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/staffs/{staff_id}/messages/count/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<NotiSmsCountListWrap>> qcGetNotiSmsCount(
      @Path("staff_id") String staffid, @QueryMap HashMap<String, Object> params);

  /**
   * 查看是否授权微信公众号
   */
  @GET("/api/v2/staffs/{staff_id}/gyms/wechat-open/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<WxAuthorWrap>> qcGetWxAuthor(
      @Path("staff_id") String staffid, @QueryMap HashMap<String, Object> params);

  /**
   * 场馆订单接口
   */
  @GET("/api/gyms/orders/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<ShopOrdersWrap>> qcGetGymOrders(
      @QueryMap HashMap<String, Object> params);

  //判断是否同意用户协议
  @GET(" /api/user/check/read_agreement/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<CheckProtocolModel>> qcCheckProtocol(
      @QueryMap HashMap<String, Object> params);

  //获取未签课的学员
  @GET("/api/staffs/{staff_id}/users/checkin/records/")
  rx.Observable<QcDataResponse<List<NotSignStudent>>> qcGetNotSignStudent(
      @Path("staff_id") String staffId, @QueryMap HashMap<String, Object> params);

  //获取门禁
  @GET("/api/staffs/{staff_id}/guards/") rx.Observable<QcDataResponse<GuardWrapper>> qcGetAccess(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  @GET("/api/staffs/{staff_id}/user/{user_id}/permission/")
  rx.Observable<QcDataResponse<JSONObject>> qcCheckSellerStudentPermission(
      @Path("staff_id") String staff_id, @Path("user_id") String user_id,
      @QueryMap HashMap<String, Object> params);

  /**
   * <----------------------POST---------------------------->
   */

  /**
   * 品牌操作
   */

  @POST("/api/brands/{id}/change_user/") Observable<QcResponse> qcChangeBrandUser(
      @Path("id") String brand_id, @Body ChangeBrandCreatorBody body);

  @PUT("api/brands/{id}/") Observable<QcResponse> qcEditBrand(@Path("id") String brand_id,
      @Body BrandBody body);

  @DELETE("/api/brands/{id}/") Observable<QcResponse> qcDelbrand(@Path("id") String id);

  /**
   * 删除场馆
   */
  @DELETE("/api/gym/{id}/") Observable<QcResponse> qcDelGym(@Path("id") String id);

  /**
   * 个人操作
   */

  @POST("/api/staffs/login/") Call<Login> qcLoginTest(@Body LoginBody loginBody);

  /**
   * 场馆续费
   */
  @POST("/api/gyms/orders/") rx.Observable<QcResponseRenew> qcCharge(@Body RenewBody body);

  /**
   * 场馆试用
   */
  @POST("/api/v2/staffs/{staff_id}/gyms/trial/")
  rx.Observable<cn.qingchengfit.network.response.QcResponse> qcGymTrial(
      @Path("staff_id") String staffid, @QueryMap HashMap<String, Object> params);

  /**
   * 修改场馆
   */
  @PUT("/api/staffs/{id}/services/{sid}/") rx.Observable<ResponseService> qcPutService(
      @Path("id") String id, @Path("sid") String sid, @Body Shop shop);

  //修改信息
  @PUT("/api/staffs/{id}/") rx.Observable<QcResponse> qcModifyStaffs(@Path("id") String id,
      @Body Staff staffBean);

  //发送意见
  @POST("/api/feedback/") rx.Observable<QcResponse> qcFeedBack(@Body FeedBackBody bean);

  //百度pushid绑定
  @POST("/api/staffs/{id}/push/update/") rx.Observable<QcResponse> qcPostPushId(
      @Path("id") String id, @Body PushBody body);

  @PUT("/api/notifications/clear/") rx.Observable<QcResponse> qcClearAllNoti(
      @Query("staff_id") String staffid, @QueryMap HashMap<String, Object> paras);

  @PUT("/api/notifications/clear/") rx.Observable<QcResponse> qcClearMutiNoti(
      @Query("staff_id") String staffid, @Body HashMap<String, List<Long>> paras);

  @PUT("/api/v2/notifications/") rx.Observable<QcResponse> qcClearTypeNoti(
      @Body ClearNotiBody body);

  /**
   * 健身房操作
   */

  @POST("/api/brands/") rx.Observable<QcDataResponse<CreatBrand>> qcCreatBrand(
      @Body CreatBrandBody body);

  @PUT("/api/staffs/{id}/") rx.Observable<QcResponse> qcFixSelfInfo(@Path("id") String id,
      @Body CreatBrandBody body);

  @POST("/api/systems/initial/") rx.Observable<QcResponseSystenInit> qcSystemInit(
      @Body SystemInitBody body);

  @POST("/api/staffs/{id}/shops/") rx.Observable<QcResponse> qcCreateGym(@Path("id") String id,
      @Query("brand_id") String brand_id, @Body GymBody body);

  //修改体测
  @PUT("/api/staffs/{id}/measures/{measure_id}/") rx.Observable<QcResponse> qcUpdateBodyTest(
      @Path("id") String staff_id, @Path("measure_id") String id,
      @QueryMap HashMap<String, Object> params, @Body BodyTestBody addBodyTestBean);

  //新建体测
  @POST("/api/staffs/{id}/measures/") rx.Observable<QcResponse> qcAddBodyTest(
      @Path("id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body BodyTestBody addBodyTestBean);

  @DELETE("/api/measures/{measure_id}/") rx.Observable<QcResponse> qcDelBodyTest(
      @Path("measure_id") String id, @Path("measure_id") String measure_id,
      @QueryMap HashMap<String, Object> params);

  //新增请假
  @POST("/api/staffs/{id}/leaves/") rx.Observable<QcResponse> qcAddDayOff(
      @Path("id") String staffid, @Query("brand_id") String brand_id, @Query("id") String gymid,
      @Query("model") String model, @Body AddDayOffBody body);

  //取消请假
  @DELETE("/api/staffs/{id}/leaves/{leave_id}/") rx.Observable<QcResponse> qcDelDayOff(
      @Path("id") String staffid, @Path("leave_id") String leave_id,
      @Query("brand_id") String brandid, @Query("id") String gymid, @Query("model") String model);

  //提前销假
  @PUT("/api/staffs/{staffid}/leaves/{leave_id}/") rx.Observable<QcResponse> qcAheadDayOff(
      @Path("staffid") String staffid, @Path("leave_id") String leave_id,
      @QueryMap HashMap<String, Object> params, @Body AheadOffDayBody body);

  /**
   * 场馆自定义 模块
   */
  @PUT("/api/v2/staffs/{staff_id}/gym-module-custom/") rx.Observable<QcResponse> qcUpdateModule(
      @Path("staff_id") String staffid, @Body UpdateModule module_custom,
      @QueryMap HashMap<String, Object> params);

  /**
   * 具体卡操作
   */
  //充值扣费
  @POST("/api/staffs/{staff_id}/cards/{card_id}/charge/")
  rx.Observable<QcResponsePayWx> qcCardCharge(@Path("staff_id") String staff_id,
      @Path("card_id") String cardid, @Query("brand_id") String brand_id,
      @Query("shop_id") String shop_id, @Query("id") String id, @Query("model") String model,
      @Body ChargeBody body);

  @POST("/api/staffs/{staff_id}/cards/orders/") rx.Observable<QcResponsePayWx> qcCardChargeWechat(
      @Path("staff_id") String staff_id
      //, @Path("card_id") String cardid
      , @Query("brand_id") String brand_id, @Query("shop_id") String shop_id,
      @Query("id") String id, @Query("model") String model, @Body ChargeBody body);

  //购卡
  @POST("/api/staffs/{id}/cards/create/") rx.Observable<QcResponsePayWx> qcCreateRealcard(
      @Path("id") String staffid, @Body CreateCardBody body, @Query("brand_id") String brand_id,
      @Query("shop_id") String shop_id, @Query("id") String id, @Query("model") String model);

  //销卡
  @DELETE("/api/staffs/{id}/cards/{card_id}/") rx.Observable<QcResponse> qcUnregisteCard(
      @Path("id") String staffid, @Path("card_id") String cardid,
      @Query("brand_id") String brand_id, @Query("id") String id, @Query("model") String model);

  //恢复卡
  @POST("/api/staffs/{id}/cards/{card_id}/recovery/") rx.Observable<QcResponse> qcResumeCard(
      @Path("id") String staffid, @Path("card_id") String cardid,
      @Query("brand_id") String brand_id, @Query("id") String id, @Query("model") String model);

  //卡修改
  @PUT("/api/staffs/{id}/cards/{card_id}/") rx.Observable<QcResponse> qcUndateCard(
      @Path("id") String staffid, @Path("card_id") String cardid,
      @Query("brand_id") String brand_id, @Query("id") String id, @Query("model") String model,
      @Body FixCard card);

  //卡修改有效期
  @PUT("/api/staffs/{id}/cards/{card_id}/change-date/") rx.Observable<QcResponse> qcUndateCardValid(
      @Path("id") String staffid, @Path("card_id") String cardid,
      @QueryMap HashMap<String, Object> params, @Body UpdateCardValidBody body);

  /**
   * 卡类型
   */
  @POST("/api/staffs/{staff_id}/cardtpls/")
  //新建卡模板
  rx.Observable<QcResponse> qcCreateCardtpl(@Path("staff_id") String staffid,
      @Body CardtplBody body, @QueryMap HashMap<String, Object> params);

  @PUT("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/") rx.Observable<QcResponse> qcUpdateCardtpl(
      @Path("staff_id") String staffid, @Path("card_tpl_id") String card_tpl_id,
      @Body CardtplBody body, @QueryMap HashMap<String, Object> params);

  /**
   * 停用会员卡种类
   */
  @DELETE("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/") rx.Observable<QcResponse> qcDelCardtpl(
      @Path("staff_id") String staffid, @Path("card_tpl_id") String card_tpl_id,
      @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brand_id);

  /**
   * 恢复会员卡种类
   */
  @POST("/api/v2/staffs/{staff_id}/cardtpls/{card_tpl_id}/recovery/")
  rx.Observable<QcResponse> qcResumeCardtpl(@Path("staff_id") String staffid,
      @Path("card_tpl_id") String card_tpl_id, @QueryMap HashMap<String, Object> params);

  @PUT("/api/staffs/{staff_id}/cardtpls/{cardtpl_id}/shops/") rx.Observable<QcResponse> qcFixGyms(
      @Path("staff_id") String staffid, @Path("cardtpl_id") String card_tpl, @Body ShopsBody body,
      @QueryMap HashMap<String, Object> params);

  /**
   * 卡规格操作
   */
  @DELETE("/api/staffs/{staff_id}/options/{option_id}/")
  rx.Observable<QcResponse> qcDelCardStandard(@Path("staff_id") String staffid,
      @Path("option_id") String option_id, @Query("id") String gymid, @Query("model") String model,
      @Query("brand_id") String brand_id);

  @PUT("/api/staffs/{staff_id}/options/{option_id}/")
  rx.Observable<QcResponse> qcUpdateCardStandard(@Path("staff_id") String staffid,
      @Path("option_id") String option_id, @Query("id") String gymid, @Query("model") String model,
      @Query("brand_id") String brand_id, @Body OptionBody body);

  @POST("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/options/")
  rx.Observable<QcResponse> qcCreateStandard(@Path("staff_id") String staffid,
      @Path("card_tpl_id") String card_tpl_id, @Query("id") String gymid,
      @Query("model") String model, @Query("brand_id") String brand_id, @Body OptionBody body);

  /**
   * 学员操作
   */
  @POST("/api/staffs/{id}/users/") rx.Observable<QcDataResponse> qcCreateStudent(
      @Path("id") String id, @QueryMap HashMap<String, Object> params, @Body User_Student student);

  /**
   * 学员操作
   */
  @POST("/api/staffs/{id}/users/") rx.Observable<QcDataResponse> qcAddStudent(@Path("id") String id,
      @QueryMap HashMap<String, Object> params, @Body AddStudentBody student);

  @PUT("/api/v2/staffs/{staff_id}/users/{id}/") rx.Observable<QcDataResponse> qcUpdateStudent(
      @Path("staff_id") String staff_id, @Path("id") String id, @Query("id") String gymid,
      @Query("model") String model, @Query("brand_id") String brandid

      , @Body User_Student student);

  @DELETE("/api/staffs/{staff_id}/users/{id}/") rx.Observable<QcResponse> qcDelStudent(
      @Path("staff_id") String staff_id, @Path("id") String id, @Query("id") String gymid,
      @Query("model") String model, @Query("brand_id") String brandid,
      @Query("shop_ids") String shop_ids);

  @POST("/api/staffs/{id}/users/{user_id}/records/") rx.Observable<QcResponse> qcAddFollowRecord(
      @Path("id") String id, @Path("user_id") String user_id, @Body AddFollowRecordBody body,
      @Query("brand_id") String brandid, @Query("id") String gymid, @Query("model") String model);

  @POST("/api/staffs/{staff_id}/user/photos/") rx.Observable<QcResponse> qcUploadImg(
      @Path("id") String id, @Path("user_id") String user_id, HashMap<String, Object> params);

  /**
   * 课程操作
   */
  /**
   * 创建课程
   */
  @POST("/api/v2/staffs/{id}/courses/") rx.Observable<QcResponse> qcCreateCourse(
      @Path("id") String staffid, @Body CourseBody courseBody,
      @QueryMap HashMap<String, Object> params);

  //修改课程
  @PUT("/api/v2/staffs/{id}/courses/{course_id}/") rx.Observable<QcResponse> qcUpdateCourse(
      @Path("id") String staffid, @Path("course_id") String course_id,
      @QueryMap HashMap<String, Object> params, @Body CourseBody courseBody);

  //删除课程
  @DELETE("/api/v2/staffs/{id}/courses/{course_id}/") rx.Observable<QcResponse> qcDelCourse(
      @Path("id") String staffid, @Path("course_id") String course_id,
      @QueryMap HashMap<String, Object> params);

  //修改封面
  @POST("/api/v2/staffs/{id}/courses/photos/") rx.Observable<QcResponse> qcEditJacket(
      @Path("id") String staffid, @Query("course_id") String course_id,
      @QueryMap HashMap<String, Object> params, @Body EditJacketBody body);

  //修改课程适用场馆
  @PUT("/api/v2/staffs/{staff_id}/courses/{course_id}/shops/")
  rx.Observable<QcResponse> qcEditCourseShops(@Path("staff_id") String staffid,
      @Path("course_id") String course_id, @Body HashMap<String, Object> params);

  /**
   * 场地操作
   */
  //新建场地
  @POST("/api/staffs/{staff_id}/spaces/") rx.Observable<QcResponse> qcCreateSpace(
      @Path("staff_id") String staff_id, @Query("id") String gymid, @Query("model") String model,
      @Query("brand_id") String brand_id, @Body Space space);

  //删除场地
  @DELETE("/api/staffs/{id}/spaces/{space_id}/") rx.Observable<QcResponse> qcDelSpace(
      @Path("id") String id, @Path("space_id") String space_id, @Query("id") String gymid,
      @Query("model") String model, @Query("brand_id") String brand_id);

  @PUT("/api/staffs/{id}/spaces/{space_id}/") rx.Observable<QcResponse> qcUpdateSpace(
      @Path("id") String staff_id, @Path("space_id") String space_id, @Query("id") String gymid,
      @Query("model") String model, @Query("brand_id") String brand_id, @Body Space space);

  /**
   * 排期
   */
  @POST("/api/staffs/{id}/arrange/batches/") rx.Observable<QcResponse> qcArrangeBatch(
      @Path("id") String staff_id, @Query("id") String gymid, @Query("model") String model,
      @Body ArrangeBatchBody body);

  @PUT("/api/staffs/{id}/batches/{batchid}/") rx.Observable<QcResponse> qcUpdateBatch(
      @Path("id") String staff_id, @Path("batchid") String batchid, @Query("id") String gymid,
      @Query("model") String model, @Body ArrangeBatchBody body);

  @POST("/api/staffs/{id}/{type}/arrange/check/") rx.Observable<QcResponse> qcCheckBatch(
      @Path("id") String staff_id, @Path("type") String type, @Query("id") String gymid,
      @Query("model") String model, @Body ArrangeBatchBody body);

  //删除排期
  @POST("api/staffs/{id}/{type}/bulk/delete/") rx.Observable<QcResponse> qcDelBatchSchedule(
      @Path("id") String staff_id, @Path("type") String type, @Body DelBatchScheduleBody body);

  @DELETE("/api/staffs/{id}/batches/{batch_id}/") rx.Observable<QcResponse> delBatch(
      @Path("id") String staff_id, @Path("batch_id") String batch_id,
      @QueryMap HashMap<String, Object> paras);

  @PUT("api/staffs/{id}/{type}/{scheduleid}/") rx.Observable<QcResponse> qcUpdateBatchSchedule(
      @Path("id") String staff_id, @Path("type") String type, @Path("scheduleid") String scheduleid,
      @Body SingleBatchBody body);

  /**
   * 教练
   */
  @POST("/api/staffs/{id}/coaches/") rx.Observable<QcDataResponse<CoachResponse>> qcAddCoach(
      @Path("id") String id, @Query("id") String gymid, @Query("model") String model,
      @Body Staff coach);

  @PUT("/api/staffs/{id}/coaches/{cid}/") rx.Observable<QcResponse> qcFixCoach(
      @Path("id") String id, @Path("cid") String cid, @Query("id") String gymid,
      @Query("model") String model, @Body Staff coach);

  @DELETE("/api/staffs/{id}/coaches/{cid}/") rx.Observable<QcResponse> qcDelCoach(
      @Path("id") String id, @Path("cid") String cid, @Query("id") String gymid,
      @Query("model") String model);

  /**
   * 工作人员
   */
  @POST("/api/staffs/{id}/users/") rx.Observable<QcResponse> qcCreateManager(@Path("id") String id,
      @Query("id") String gymid, @Query("model") String model, @Body ManagerBody body);

  @PUT("/api/staffs/{id}/users/{mid}/") rx.Observable<QcResponse> qcUpdateManager(
      @Path("id") String id, @Path("mid") String cid, @Query("id") String gymid,
      @Query("model") String model, @Body ManagerBody body);

  @DELETE("/api/staffs/{id}/managers/{mid}/") rx.Observable<QcResponse> qcDelManager(
      @Path("id") String id, @Path("mid") String cid, @Query("id") String gymid,
      @Query("model") String model);

  @PUT("/api/staffs/{staff_id}/superuser/") rx.Observable<QcResponse> qcChangeSu(
      @Path("staff_id") String staffid, @QueryMap HashMap<String, Object> params,
      @Body ChangeSuBody body);

  /**
   * 报表操作
   */
  @POST("/api/staffs/{staff_id}/excel/tasks/") rx.Observable<QcResponse> qcOutExcel(
      @Path("staff_id") String staff_id, @Body OutExcelBody body);

  /**
   *
   */
  @PUT("/api/scans/{uuid}/") rx.Observable<QcResponse> qcScans(@Path("uuid") String uuid,
      @Body ScanBody body);

  /**
   * 健身房所有设置
   * http://192.168.1.7:8000/api/staffs/3281/shops/configs/?id=5370&model=staff_gym
   * {@link ShopConfigs}
   */
  @PUT("/api/staffs/{staff_id}/shops/configs/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcShopConfigs(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body ShopConfigBody body);

  /**
   * 签到扣费设置
   * http://192.168.1.7:8000/api/v2/staffs/3281/checkin/settings/?id=5370&model=staff_gym
   */
  @PUT("/api/v2/staffs/{staff_id}/checkin/settings/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPutSignInCostConfig(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body SignInCostBody body);

  /**
   * 确认签到
   * http://192.168.1.106:8000/api/staffs/3281/doublecheckin/?id=5370&model=staff_gym
   */
  @PUT("/api/v2/staffs/{staff_id}/doublecheckin/") rx.Observable<QcResponse> qcPutDoubleCheckin(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body SignInBody body);

  /**
   * 确认签出
   * http://192.168.1.106:8000/api/staffs/3281/doublecheckout/?id=5370&model=staff_gym
   */
  @PUT("/api/v2/staffs/{staff_id}/doublecheckout/") rx.Observable<QcResponse> qcPutDoubleCheckout(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body SignOutBody body);

  /**
   * 手动签到
   * http://192.168.1.106:8000/api/staffs/3281/checkin/?id=5370&model=staff_gym
   * POST
   */
  @POST("/api/v2/staffs/{staff_id}/checkin/") rx.Observable<QcResponse> qcPostCheckInMaual(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body SignInManualBody body);

  /**
   * 手动签出
   * http://192.168.1.106:8000/api/staffs/3281/checkin/?id=5370&model=staff_gym
   * PUT
   */
  @PUT("/api/v2/staffs/{staff_id}/checkin/") rx.Observable<QcResponse> qcPutCheckOutMaual(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body SignOutBody body);

  /**
   * 工作人员忽略签到/签出
   * http://192.168.1.106:8000/api/v2/staffs/3281/checkin/ignore?id=5370&model=staff_gym
   */
  @POST("/api/v2/staffs/{staff_id}/checkin/ignore/") rx.Observable<QcResponse> qcPostIgnore(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body SignInIgnorBody body);

  /**
   * 签到撤销
   * http://192.168.1.106:8000/api/staffs/3281/checkin/?id=5370&model=staff_gym
   * DELETE
   */
  @DELETE("/api/v2/staffs/{staff_id}/checkin/") rx.Observable<QcResponse> qcDeleteCheckin(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 签到照片
   * http://192.168.1.106:8000/api/staffs/3281/user/photos/?id=5370&model=staff_gym
   * POST:{"user_id":162, "photo": "照片url"}
   */
  @POST("/api/staffs/{staff_id}/user/photos/") rx.Observable<QcResponse> qcUploadStuImg(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body HashMap<String, Object> body);

  /**
   * 更衣柜
   */
  //增加
  @POST("/api/v2/staffs/{staff_id}/lockers/") rx.Observable<QcResponse> qcAddLocker(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body AddLockerBody body);

  //删除
  @DELETE("/api/v2/staffs/{staff_id}/lockers/{locker_id}/") rx.Observable<QcResponse> qcDelLocker(
      @Path("staff_id") String staff_id, @Path("locker_id") String lockerid,
      @QueryMap HashMap<String, Object> params);

  //修改
  @PUT("/api/v2/staffs/{staff_id}/lockers/{locker_id}/")
  rx.Observable<QcDataResponse<LockerWrapper>> qcEditLocker(@Path("staff_id") String staff_id,
      @Path("locker_id") String lockerid, @QueryMap HashMap<String, Object> params,
      @Body EditWardrobeBody body);

  //归还
  @PUT("/api/v2/staffs/{staff_id}/lockers/return/") rx.Observable<QcResponse> qcReturnLockers(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body ReturnWardrobeBody body);

  //租用
  @PUT("/api/v2/staffs/{staff_id}/lockers/borrow/") rx.Observable<QcResponse> qcHireLocker(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body HireWardrobeBody body);

  /**
   * 更衣柜区域
   */
  //新增
  @POST("/api/v2/staffs/{staff_id}/locker/regions/") rx.Observable<QcResponse> qcAddLockerRegion(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body HashMap<String, Object> body);

  //修改
  @PUT("/api/v2/staffs/{staff_id}/locker/regions/") rx.Observable<QcResponse> qcEditLockerRegion(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body HashMap<String, Object> body);

  //删除
  @DELETE("/api/v2/staffs/{staff_id}/locker/regions/") rx.Observable<QcResponse> qcDelLockerRegion(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //续租
  @PUT("/api/v2/staffs/{staff_id}/lockers/long/delay/") rx.Observable<QcResponse> qcContinueLocker(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body HashMap<String, Object> body);

  /**
   * 签到签出通知设置
   * /api/staffs/3281/checkin/notification/configs/
   */
  @PUT("/api/staffs/{staff_id}/checkin/notification/configs/")
  rx.Observable<QcResponse> qcPutCheckinNoticeCOnfig(@Path("staff_id") String staff_id,
      @Body SignInNoticeConfigBody body);

  /**
   * 移除名下单个会员
   * DELETE http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1&user_id=1&seller_id=1
   * DELETE http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym?user_id=2&seller_id=1
   */
  @DELETE("/api/v2/staffs/{staff_id}/sellers/users/") rx.Observable<QcResponse> qcDeleteStudent(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 批量移除某个销售名下会员:
   * POST http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/remove/?brand_id=2&shop_id=1
   * POST http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/remove/?id=5370&model=staff_gym
   * POST {"user_ids":"1,3,2", "seller_id":5}
   */
  @POST("/api/v2/staffs/{staff_id}/sellers/users/remove/")
  rx.Observable<QcResponse> qcDeleteStudents(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

  /**
   * /**
   * 批量变更销售
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym
   * PUT {"user_ids":"1,2,3,4,5", "seller_ids":"10,11"}
   *
   * @param body user_ids  seller_ids
   */
  @PUT("/api/v2/staffs/{staff_id}/sellers/users/") rx.Observable<QcResponse> qcModifySellers(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body HashMap<String, Object> body);

  /**
   * 批量添加名下会员:
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym
   * POST {"user_ids":"1,2,3,4,5", "seller_id":10}
   */
  @POST("/api/v2/staffs/{staff_id}/sellers/users/") rx.Observable<QcResponse> qcAddStudents(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body HashMap<String, Object> body);

  /**
   * POST 新增来源
   * /api/v2/staffs/:staff_id/users/origins/?brand_id=&shop_id= 或者 id=&model=
   */
  @POST("/api/v2/staffs/{staff_id}/users/origins/") rx.Observable<QcResponse> qcAddOrigin(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body HashMap<String, Object> body);

  @POST("/api/v2/staffs/{staff_id}/dimission/") rx.Observable<QcResponse> qcQuitGym(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * Staff - 模块-积分-开关-修改
   * PUT /api/v2/staffs/:id/modules/
   * score  Boolean  是否开启积分功能
   */
  @PUT("/api/v2/staffs/{staff_id}/modules/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPutScoreStatus(
      @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params,
      @Body ArrayMap<String, Object> body);

  /**
   * Staff - 模块-积分规则-修改
   * /api/v2/staffs/:id/scores/rules/
   */
  @PUT("/api/v2/staffs/{staff_id}/scores/rules/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPutScoreRules(
      @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params,
      @Body ArrayMap<String, Object> body);

  /**
   * Staff - 模块-积分优惠奖励-新增
   * POST /api/v2/staffs/:id/scores/favors/
   */
  @POST("/api/v2/staffs/{staff_id}/scores/favors/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPostScoreRulesAward(
      @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params,
      @Body ArrayMap<String, Object> body);

  /**
   * Staff - 模块-积分优惠奖励-修改
   * PUT /api/v2/staffs/:id/scores/favors/:favor_id/
   */
  @PUT("/api/v2/staffs/{staff_id}/scores/favors/{favor_id}/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPutScoreRulesAward(
      @Path("staff_id") String staff_id, @Path("favor_id") String favor_id,
      @QueryMap ArrayMap<String, String> params, @Body ArrayMap<String, Object> body);

  /**
   * Staff - 模块-积分历史-新增
   * POST /api/v2/staffs/:id/users/:user_id/scores/histories/
   */
  @POST("/api/v2/staffs/{staff_id}/users/{user_id}/scores/histories/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPostScoreHistory(
      @Path("staff_id") String staff_id, @Path("user_id") String user_id,
      @QueryMap ArrayMap<String, String> params, @Body ArrayMap<String, Object> body);

  //修改余额不足提醒规则
  @PUT("api/v2/staffs/{staff_id}/users/configs/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPostBalanceCondition(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body CardBalanceNotifyBody body);

  //修改自动提醒短信规则
  @PUT("api/staffs/{staff_id}/shops/configs/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcChangeAutoNotify(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body CardBalanceNotifyBody body);

  //修改余额不足提醒
  @PUT("/api/staffs/{staff_id}/configs/") rx.Observable<QcResponse> qcPostBalanceNotify(
      @Path("staff_id") String staff_id, @Body CardBalanceNotifyBody body);

  //新建分组
  @POST("api/staffs/competitions/teams/") rx.Observable<QcResponse> qcPostCreateGroup(
      @Body CreateGroupBody createGroupBody);

  //删除报名表分组
  @DELETE("/api/staffs/competitions/teams/{team_id}/") rx.Observable<QcResponse> qcDeleteGroup(
      @Path("team_id") String team_id);

  //添加&删除分组成员
  @PUT("/api/staffs/competitions/teams/{team_id}/") rx.Observable<QcResponse> qcPostMemberOperation(
      @Path("team_id") String team_id, @Body OperationMemberBody body);

  //分配教练
  @PUT("/api/v2/staffs/{staff_id}/coaches/users/") rx.Observable<QcResponse> qcAllocateCoach(
      @Path("staff_id") String staff_id, @Body HashMap<String, Object> body);

  //分配教练
  @POST("/api/v2/staffs/{staff_id}/coaches/users/remove/")
  rx.Observable<QcResponse> qcRemoveStudent(@Path("staff_id") String staff_id,
      @Body HashMap<String, Object> body);

  //导入导出
  @POST("/api/staffs/{staff_id}/export/do/") rx.Observable<QcResponse> qcDataImport(
      @Path("staff_id") String staff_id, @Body HashMap<String, Object> body);

  //发送邮件
  @POST("/api/staffs/{staff_id}/export/mail/") rx.Observable<QcResponse> qcSendMail(
      @Path("staff_id") String staff_id, @Body HashMap<String, Object> body);

  //修改门禁状态
  @PUT("/api/staffs/{staff_id}/guards/{guard_id}/") rx.Observable<QcResponse> qcChangeAccessStatus(
      @Path("staff_id") String staff_id, @Path("guard_id") String guard_id,
      @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

  //删除门禁
  @DELETE("/api/staffs/{staff_id}/guards/{guard_id}/") rx.Observable<QcResponse> qcDeleteAccess(
      @Path("staff_id") String staff_id, @Path("guard_id") String guard_id,
      @QueryMap HashMap<String, Object> params);

  //添加门禁
  @POST("/api/staffs/{staff_id}/guards/") rx.Observable<QcResponse> qcAddAccess(
      @Path("staff_id") String staff_id, @Body AccessBody body);

  //暂存添加会员卡种类信息
  @POST("api/staffs/{staff_id}/cache/") rx.Observable<QcDataResponse> qcStashNewCardTpl(
      @Path("staff_id") String staff_id, @Body CardtplBody body,
      @QueryMap HashMap<String, Object> params);

  //编辑门禁
  @POST("/api/staffs/{staff_id}/guards/{guard_id}/") rx.Observable<QcResponse> qcEditAccess(
      @Path("staff_id") String staff_id, @Path("guard_id") String guard_id,
      @QueryMap HashMap<String, Object> params, @Body AccessBody body);

  //------------------------新增场馆相关接口------------------//

  @GET("/api/v2/staffs/{staff_id}/gym/")
  rx.Observable<QcDataResponse<DianPingShopContainer>> qcGetGymInfo(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  @PUT("/api/v2/staffs/{staff_id}/gym/")
  rx.Observable<QcDataResponse<DianPingShopContainer>> qcPutGymInfo(@Path("staff_id") String id,
      @Body Map<String, Object> body, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/staffs/{staff_id}/gym/all/services/?show_all=1")
  rx.Observable<QcDataResponse<GymFacilitiesList>> qcGetGymFacilities(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  @GET("/api/tags/?is_gym=1") rx.Observable<QcDataResponse<GymTags>> qcGetGymTags();

  @POST("/api/partner/meituan/gyms/{gym_id}/link/")
  rx.Observable<QcDataResponse<SimpleSuccessResponse>> qcPostDianPingAccount(@Path("gym_id") String id,
      @Body Map<String, Object> params);

  @GET("/api/partner/meituan/gyms/{gym_id}/link/")
  rx.Observable<QcDataResponse<DianPingGymInfo>> qcGetDianPingGymInfo(@Path("gym_id") String id,
      @QueryMap Map<String, Object> params);

  //------------------------营业流水相关接口------------------//

  @GET("api/v2/staffs/{staff_id}/turnovers/query/items/")
  rx.Observable<QcDataResponse<TurFilterResponse>> qcGetTurnoversFilterItems(
      @Path("staff_id") String id, @QueryMap Map<String, Object> params);

  @GET("api/v2/staffs/{staff_id}/turnovers/?show_all=1")
  rx.Observable<QcDataResponse<TurOrderListResponse>> qcGetTurnoversOrderList(
      @Path("staff_id") String id, @QueryMap Map<String, Object> params);

  @GET("api/v2/staffs/{staff_id}/turnovers/stat/")
  rx.Observable<QcDataResponse<TurnoversChartStatDataResponse>> qcGetTurnoversChartStat(
      @Path("staff_id") String id, @QueryMap Map<String, Object> params);

  @GET("api/v2/staffs/{staff_id}/turnovers/{turnover_id}/")
  rx.Observable<QcDataResponse<TurOrderListDataWrapper>> qcGetTurnoverOrderDetail(
      @Path("staff_id") String id, @Path("turnover_id") String tur_id,
      @QueryMap Map<String, Object> params);

  @PUT("api/v2/staffs/{staff_id}/turnovers/{turnover_id}/")
  rx.Observable<QcDataResponse<TurOrderListDataWrapper>> qcPutTurnoverOrderDetail(
      @Path("staff_id") String id, @Path("turnover_id") String tur_id,
      @Body Map<String, Object> seller_id, @QueryMap Map<String, Object> params);

  @GET("api/v2/staffs/{staff_id}/turnovers/{turnover_id}/seller/history/?show_all=1")
  rx.Observable<QcDataResponse<TurOrderSellerHistoryWrapper>> qcGetOrderHistorty(
      @Path("staff_id") String id, @Path("turnover_id") String tur_id,
      @QueryMap Map<String, Object> params);
}
