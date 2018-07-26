package com.qingchengfit.fitcoach.fragment.card;

import cn.qingchengfit.model.responese.Cards;
import cn.qingchengfit.model.responese.QcResponseOption;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import com.google.gson.JsonObject;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface CardTrainerApi {


  // 工作人员 卡类型 规格
  // 工作人员 卡类型 规格
  @GET("/api/coaches/{staff_id}/cardtpls/{cardtps_id}/options/")
  rx.Observable<QcDataResponse<CardTplOptionListWrap>> qcGetOptions(
      @Path("staff_id") String staff_id, @Path("cardtps_id") String cardtps_id,
      @QueryMap HashMap<String, Object> params);


  //获取会员卡
  @GET("/api/coaches/{id}/cards/?order_by=-id")
  rx.Observable<QcDataResponse<CardListWrap>> getAllCards(@Path("id") String staffid,
      @QueryMap HashMap<String, Object> params);



  //购卡
  @POST("/api/coaches/{id}/cards/create/")
  rx.Observable<QcDataResponse<JsonObject>> qcCreateRealcard(@Path("id") String staffid,
      @Body CardBuyBody body, @QueryMap HashMap<String, Object> params);


  //充值扣费
  @POST("/api/coaches/{coach_id}/cards/{card_id}/charge/v2/")
  rx.Observable<QcDataResponse<JsonObject>> qcCardCharge(@Path("coach_id") String coach_id,
      @Path("card_id") String cardid, @QueryMap HashMap<String, Object> params,
      @Body CardBuyBody body);

  //工作人员 卡类型
  @GET("/api/coaches/{id}/cardtpls/list/?show_all=1&order_by=-id")
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(@Path("id") String id,
      @QueryMap HashMap<String, Object> params, @Query("type") String type,
      @Query("is_enable") String isEnable);
}
