package cn.qingchengfit.staffkit.constant;

import cn.qingchengfit.gym.gymconfig.ShopConfigs;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.GymCardtpl;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.model.responese.Notification;
import cn.qingchengfit.model.responese.Sellers;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.model.responese.TrackStudents;
import cn.qingchengfit.network.response.NotiSmsCountListWrap;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.ShopOrdersWrap;
import cn.qingchengfit.network.response.SmsListWrap;
import cn.qingchengfit.network.response.WxAuthorWrap;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.staffkit.allocate.coach.model.AllocateStudentBean;
import cn.qingchengfit.staffkit.views.export.model.ExportRecordWrapper;
import cn.qingchengfit.staffkit.views.signin.zq.model.GuardWrapper;
import cn.qingchengfit.staffkit.views.student.attendance.model.NotSignStudent;
import java.util.HashMap;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

  /**
   * 品牌
   */
  @Deprecated @GET("/api/brands/") rx.Observable<BrandsResponse> qcGetBrands();

  @GET("/api/staffs/{id}/brands/") rx.Observable<QcDataResponse<BrandsResponse>> qcGetBrands(
      @Path("id") String id);

  @GET("/api/staffs/{id}/services/") rx.Observable<QcDataResponse<GymList>> qcGetCoachService(
      @Path("id") String id, @Query("brand_id") String brand_id);

  //获取通知 分页和不分页接口 ,后者只为拿 未读
  @Deprecated @GET("/api/notifications/") rx.Observable<QcDataResponse<Notification>> qcGetMessages(
      @Query("staff_id") String id, @QueryMap HashMap<String, Object> params);

  //会员卡可绑定的会员列表
  @GET("/api/staffs/{staff_id}/method/users/?show_all=1")
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(
      @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

  /**
   * 会员卡模板
   */
  @GET("/api/staffs/{id}/cardtpls/all/")
  rx.Observable<QcDataResponse<GymCardtpl>> qcGetBrandCardtpl(@Path("id") String staffid,
      @Query("brand_id") String brand_id);

  //获取销售 卖卡  包含销售 不包含教练
  @GET("/api/staffs/{staff_id}/sellers-without-coach/")
  rx.Observable<QcDataResponse<Sellers>> qcGetSalers(@Path("staff_id") String staff_id,
      @Query("brand_id") String brandid, @Query("shop_id") String shopid, @Query("id") String gymid,
      @Query("model") String model);

  /**
   * 获取签到和更衣柜联动设置的id
   * {@link ShopConfigs}
   */
  @GET("/api/staffs/{staff_id}/shops/configs/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<SignInConfig.Data>> qcGetShopConfig(
      @Path("staff_id") String staff_id, @Query("keys") String key,
      @QueryMap HashMap<String, Object> params);

  /**
   * 会员分状态列表
   * /api/staffs/:staff_id/status/users/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:status, [star][end][seller_id(无销售seller_id=0)][recommend_user_id][字符串origin]
   */
  @Deprecated @GET("/api/staffs/{staff_id}/status/users/?show_all=1")
  rx.Observable<QcDataResponse<TrackStudents>> qcGetTrackStudentsWithStatus(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 今日新增详细列表
   * /api/staffs/:staff_id/status/users/new/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:status, [star][end][seller_id(无销售seller_id=0)][recommend_user_id][字符串origin]
   */
  @Deprecated @GET("/api/staffs/{staff_id}/status/users/new/?show_all=1")
  rx.Observable<QcDataResponse<TrackStudents>> qcGetTrackStudentsWithStatusToday(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  @GET("api/v2/staffs/{staff_id}/coaches/users/")
  rx.Observable<QcDataResponse<AllocateStudentBean>> qcGetCoachStudentDetail(
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

  //获取未签课的学员
  @GET("/api/staffs/{staff_id}/users/checkin/records/")
  rx.Observable<QcDataResponse<List<NotSignStudent>>> qcGetNotSignStudent(
      @Path("staff_id") String staffId, @QueryMap HashMap<String, Object> params);

  //获取门禁
  @GET("/api/staffs/{staff_id}/guards/") rx.Observable<QcDataResponse<GuardWrapper>> qcGetAccess(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);
}
