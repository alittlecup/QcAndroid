package cn.qingchengfit.card.network;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.model.body.ChargeBody;
import cn.qingchengfit.model.body.CreateCardBody;
import cn.qingchengfit.model.responese.CacluScore;
import cn.qingchengfit.model.responese.QcResponsePayWx;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.model.responese.Sellers;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface CardApi {

  //获取销售 卖卡  包含销售和教练
  @GET("/api/staffs/{staff_id}/sellers/") rx.Observable<QcDataResponse<Sellers>> qcGetSalersAndCoach(@Path("staff_id") String staff_id,
      @Query("brand_id") String brandid, @Query("shop_id") String shopid, @Query("id") String gymid, @Query("model") String model);
  //充值扣费
  @POST("/api/staffs/{staff_id}/cards/{card_id}/charge/") rx.Observable<QcResponsePayWx> qcCardCharge(@Path("staff_id") String staff_id,
      @Path("card_id") String cardid, @Query("brand_id") String brand_id, @Query("shop_id") String shop_id, @Query("id") String id,
      @Query("model") String model, @Body ChargeBody body);

  @POST("/api/staffs/{staff_id}/cards/orders/") rx.Observable<QcResponsePayWx> qcCardChargeWechat(@Path("staff_id") String staff_id
      //, @Path("card_id") String cardid
      , @Query("brand_id") String brand_id, @Query("shop_id") String shop_id, @Query("id") String id, @Query("model") String model,
      @Body ChargeBody body);


  //购卡
  @POST("/api/staffs/{id}/cards/create/") rx.Observable<QcResponsePayWx> qcCreateRealcard(@Path("id") String staffid,
      @Body CreateCardBody body, @Query("brand_id") String brand_id, @Query("shop_id") String shop_id, @Query("id") String id,
      @Query("model") String model);

  /**
   * Staff - 购卡后结算积分查询
   * /api/v2/staffs/:id/scores/calu/
   */

  @GET("/api/v2/staffs/{staff_id}/scores/calu/")
  rx.Observable<QcDataResponse<CacluScore>> qcGetScoreCalu(@Path("staff_id") String staff_id,
      @Query("type") String type, @Query("number") String money, @QueryMap
      ArrayMap<String, String> params);

  //工作人员 卡类型
  @GET("/api/v2/staffs/{id}/cardtpls/all/?show_all=1&order_by=-id")
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(@Path("id") String id,
      @QueryMap HashMap<String, Object> params, @Query("type") String type,
      @Query("is_enable") String isEnable);

  //获取某个学员的cardlist
  @GET("/api/staffs/{staff_id}/users/{id}/cards/?order_by=-id")
  rx.Observable<QcResponseStudentCards> qcGetStudentCards(@Path("staff_id") String staffid,
      @Path("id") String studentid, @Query("id") String gymid, @Query("model") String model,
      @Query("brand_id") String brand_id);
}
