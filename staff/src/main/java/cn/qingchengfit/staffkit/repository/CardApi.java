package cn.qingchengfit.staffkit.repository;

import cn.qingchengfit.network.response.QcDataResponse;
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
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
import cn.qingchengfit.saasbase.cards.network.response.NotityIsOpenConfigs;
import cn.qingchengfit.saasbase.cards.network.response.Shops;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import com.google.gson.JsonObject;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
 * Created by Paper on 2017/11/24.
 */

public interface CardApi {

  /**
   * 修改卡信息
   */
  @PUT("/api/staffs/{id}/cards/{card_id}/") rx.Observable<QcDataResponse> editCardInfo(
    @Path("id") String gymid, @Path("card_id") String card_id,
    @QueryMap HashMap<String, Object> params);

  //获取会员卡
  @GET("/api/staffs/{id}/cards/all/?order_by=-id")
  rx.Observable<QcDataResponse<CardListWrap>> getAllCards(@Path("id") String staffid,
    @QueryMap HashMap<String, Object> params);

  @GET("api/staffs/{id}/balance/cards/") rx.Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard(@Path("id") String staffid,
    @QueryMap HashMap<String, Object> params);

  //获取筛选列表
  @GET("/api/staffs/{id}/filter/cardtpls/?show_all=1")
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardFilterCondition(@Path("id") String staff,
    @QueryMap HashMap<String, Object> params);

  /**
   * 会员卡详情
   *
   * @param card_id 卡id
   */
  @GET("/api/staffs/{id}/cards/{card_id}/")
  rx.Observable<QcDataResponse<CardWrap>> qcGetCardDetail(@Path("id") String staff,
    @Path("card_id") String card_id, @QueryMap HashMap<String, Object> params);

  /**
   * 会员卡绑定会员
   */
  @GET("/api/staffs/{id}/cards/bind/users/")
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetBindStudent(@Path("id") String staff,
    @QueryMap HashMap<String, Object> params);

  /**
   * 卡规格操作
   */
  @DELETE("/api/staffs/{staff_id}/options/{option_id}/")
  rx.Observable<QcDataResponse> qcDelCardtplOption(@Path("staff_id") String staffid,
    @Path("option_id") String option_id, @QueryMap HashMap<String, Object> params);

  @PUT("/api/staffs/{id}/options/{option_id}/")
  rx.Observable<QcDataResponse> qcUpdateCardtplOption(@Path("id") String staffid,
    @Path("option_id") String option_id, @QueryMap HashMap<String, Object> params,
    @Body OptionBody body);

  @POST("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/options/")
  rx.Observable<QcDataResponse> qcCreateCardtplOption(@Path("staff_id") String staffid,
    @Path("card_tpl_id") String card_tpl_id, @QueryMap HashMap<String, Object> params,
    @Body OptionBody body);

  /**
   * 卡类型 todo 这里应有新接口 带着卡规格一起
   */
  @POST("/api/staffs/{staff_id}/cardtpls/")
  rx.Observable<QcDataResponse> qcCreateCardtpl(@Path("staff_id") String staffid,
    @Body CardtplBody body, @QueryMap HashMap<String, Object> params);

  @PUT("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/")
  rx.Observable<QcDataResponse> qcUpdateCardtpl(@Path("staff_id") String staffid,
    @Path("card_tpl_id") String card_tpl_id, @Body CardtplBody body,
    @QueryMap HashMap<String, Object> params);

  /**
   * 停用会员卡种类
   */
  @DELETE("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/")
  rx.Observable<QcDataResponse> qcDelCardtpl(@Path("staff_id") String staffid,
    @Path("card_tpl_id") String card_tpl_id, @QueryMap HashMap<String, Object> params);

  /**
   * 恢复会员卡种类
   */
  @POST("/api/v2/staffs/{staff_id}/cardtpls/{card_tpl_id}/recovery/")
  rx.Observable<QcDataResponse> qcResumeCardtpl(@Path("staff_id") String staffid,
    @Path("card_tpl_id") String card_tpl_id, @QueryMap HashMap<String, Object> params);

  @PUT("/api/staffs/{staff_id}/cardtpls/{cardtpl_id}/shops/") rx.Observable<QcDataResponse> qcFixGyms(@Path("staff_id") String staffid,
    @Path("cardtpl_id") String card_tpl, @Body ShopsBody body, @QueryMap HashMap<String, Object> params);

  //充值扣费
  @POST("/api/staffs/{staff_id}/cards/{card_id}/charge/v2/") rx.Observable<QcDataResponse<JsonObject>> qcCardCharge(@Path("staff_id") String staff_id,
    @Path("card_id") String cardid,@QueryMap HashMap<String, Object> params , @Body CardBuyBody body);

  //扣费
  @POST("/api/staffs/{staff_id}/cards/{card_id}/charge/") rx.Observable<QcDataResponse<JsonObject>> qcMinusMoney(@Path("staff_id") String staff_id,
      @Path("card_id") String cardid,@QueryMap HashMap<String, Object> params , @Body ChargeBody body);

  //购卡
  @POST("/api/staffs/{id}/cards/create/") rx.Observable<QcDataResponse<JsonObject>> qcCreateRealcard(@Path("id") String staffid,
    @Body CardBuyBody body, @QueryMap HashMap<String, Object> params);

  //工作人员 卡类型
  @GET("/api/v2/staffs/{id}/cardtpls/all/?show_all=1&order_by=-id")
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(@Path("id") String id,
    @QueryMap HashMap<String, Object> params, @Query("type") String type,
    @Query("is_enable") String isEnable);

  //工作人员 卡类型详情
  @GET("/api/staffs/{Staff}/cardtpls/{id}/")
  rx.Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(@Path("Staff") String staff,
    @Path("id") String id, @QueryMap HashMap<String, Object> parasm);

  // 工作人员 卡类型 规格
  @GET("/api/staffs/{staff_id}/cardtpls/{cardtps_id}/options/")
  rx.Observable<QcDataResponse<CardTplOptionListWrap>> qcGetOptions(
    @Path("staff_id") String staff_id, @Path("cardtps_id") String cardtps_id,
    @QueryMap HashMap<String, Object> params);


  //余额条件
  @GET("/api/v2/staffs/{id}/users/configs/") rx.Observable<QcDataResponse<cn.qingchengfit.saasbase.cards.network.response.BalanceConfigs>> qcGetBalanceCondition(
    @Path("id") String staffId, @QueryMap HashMap<String, Object> params, @Query("keys") String permission);
  //修改余额不足提醒规则
  @PUT("api/v2/staffs/{staff_id}/users/configs/") rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPostBalanceCondition(
    @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params, @Body
    CardBalanceNotifyBody body);

  //获取自动提醒配置
  @GET("api/staffs/{id}/shops/configs/")
  rx.Observable<cn.qingchengfit.network.response.QcDataResponse<NotityIsOpenConfigs>> qcGetNotifySetting(@Path("id") String staffId,
    @QueryMap HashMap<String, Object> params);
  //修改自动提醒短信规则
  @PUT("api/staffs/{staff_id}/shops/configs/") rx.Observable<QcDataResponse> qcChangeAutoNotify(
    @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params, @Body
    CardBalanceNotifyBody body);

  @GET("/api/staffs/{id}/shops/") rx.Observable<QcDataResponse<Shops>> qcGetBrandShops(@Path("id") String id,
      @Query("brand_id") String brand_id);

  //暂存添加会员卡种类信息
  @POST("api/staffs/{staff_id}/cache/")
  rx.Observable<QcDataResponse<UUIDModel>> qcStashNewCardTpl(@Path("staff_id") String staff_id,
      @Body CardtplBody body, @QueryMap HashMap<String, Object> params);

  //新增请假
  @POST("/api/staffs/{id}/leaves/") rx.Observable<QcDataResponse> qcAddDayOff(@Path("id") String staffid, @Query("brand_id") String brand_id,
      @Query("id") String gymid, @Query("model") String model, @Body AddDayOffBody body);

  //取消请假
  @DELETE("/api/staffs/{id}/leaves/{leave_id}/") rx.Observable<QcDataResponse> qcDelDayOff(@Path("id") String staffid,
      @Path("leave_id") String leave_id, @Query("brand_id") String brandid, @Query("id") String gymid, @Query("model") String model);

  //提前销假
  @PUT("/api/staffs/{staffid}/leaves/{leave_id}/") rx.Observable<QcDataResponse> qcAheadDayOff(@Path("staffid") String staffid,
      @Path("leave_id") String leave_id, @QueryMap HashMap<String, Object> params, @Body
      AheadOffDayBody body);

  //获取请假列表
  @GET("/api/staffs/{id}/leaves/?order_by=-created_at") rx.Observable<QcDataResponse<DayOffs>> qcGetDayOff(@Path("id") String staffid,
      @Query("brand_id") String brandid, @Query("card_id") String card_id, @Query("id") String gymid, @Query("model") String model);

  //销卡
  @DELETE("/api/staffs/{id}/cards/{card_id}/") rx.Observable<QcDataResponse> qcUnregisteCard(@Path("id") String staffid,
      @Path("card_id") String cardid, @Query("brand_id") String brand_id, @Query("id") String id, @Query("model") String model);

  //卡修改有效期
  @PUT("/api/staffs/{id}/cards/{card_id}/change-date/") rx.Observable<QcDataResponse> qcUndateCardValid(@Path("id") String staffid,
      @Path("card_id") String cardid, @QueryMap HashMap<String, Object> params, @Body
      UpdateCardValidBody body);

  //恢复卡
  @POST("/api/staffs/{id}/cards/{card_id}/recovery/") rx.Observable<QcDataResponse> qcResumeCard(@Path("id") String staffid,
      @Path("card_id") String cardid, @Query("brand_id") String brand_id, @Query("id") String id, @Query("model") String model);

  //获取消费记录
  @GET("/api/staffs/{id}/cards/{card_id}/histories/?order_by=-created_at")
  rx.Observable<QcDataResponse<QcResponseRealcardHistory>> qcGetCardhistory(@Path("id") String staffid,
      @Path("card_id") String card_id, @QueryMap HashMap<String, Object> params,
      @Query("created_at__gte") String start, @Query("created_at__lte") String end,
      @Query("page") int page);

}
