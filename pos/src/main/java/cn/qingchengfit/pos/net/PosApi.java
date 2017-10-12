package cn.qingchengfit.pos.net;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.login.model.Login;
import cn.qingchengfit.pos.login.model.LoginBody;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
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

public interface PosApi {
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

  //会员卡可绑定的会员列表
  @GET("/api/staffs/{staff_id}/users/?show_all=1")
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(
      @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

  //工作人员下所有会员
  //    @GET("/api/staffs/{id}/users/all/?show_all=1")
  @GET("/api/staffs/{id}/users/?show_all=1")
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(@Path("id") String id,
      @QueryMap HashMap<String, Object> params);

  //获取会员卡
  @GET("/api/staffs/{id}/cards/all/?order_by=-id")
  rx.Observable<QcDataResponse<CardListWrap>> getAllCards(@Path("id") String staffid,
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
  @GET("/api/staffs/{id}/cards/{card_id}/") rx.Observable<QcDataResponse<CardWrap>> qcGetCardDetail(
      @Path("id") String staff, @Path("card_id") String card_id,
      @QueryMap HashMap<String, Object> params);

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

  @PUT("/api/staffs/{staff_id}/options/{option_id}/")
  rx.Observable<QcDataResponse> qcUpdateCardtplOption(@Path("staff_id") String staffid,
      @Path("option_id") String option_id, @QueryMap HashMap<String, Object> params,
      @Body OptionBody body);

  @POST("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/options/")
  rx.Observable<QcDataResponse> qcCreateCardtplOption(@Path("staff_id") String staffid,
      @Path("card_tpl_id") String card_tpl_id, @QueryMap HashMap<String, Object> params,
      @Body OptionBody body);

  /**
   * 卡类型
   */
  @POST("/api/staffs/{staff_id}/cardtpls/") rx.Observable<QcDataResponse> qcCreateCardtpl(
      @Path("staff_id") String staffid, @Body CardtplBody body,
      @QueryMap HashMap<String, Object> params);

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

  /**
   * 登录
   */
  @POST("/api/staffs/login/") Observable<QcDataResponse<Login>> qcLogin(@Body LoginBody loginBody);

  //充值扣费
  @POST("/api/staffs/{staff_id}/cards/{card_id}/charge/") rx.Observable<QcDataResponse> qcCardCharge(@Path("staff_id") String staff_id,
      @Path("card_id") String cardid,@QueryMap HashMap<String, Object> params , @Body ChargeBody body);
  //购卡
  @POST("/api/staffs/{id}/cards/create/") rx.Observable<QcDataResponse> qcCreateRealcard(@Path("id") String staffid,
      @Body CardBuyBody body, @QueryMap HashMap<String, Object> params);

  //获取销售 卖卡  包含销售和教练
  @GET("/api/staffs/{staff_id}/sellers/") rx.Observable<QcDataResponse<SalerListWrap>> qcGetSalers(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);
}
