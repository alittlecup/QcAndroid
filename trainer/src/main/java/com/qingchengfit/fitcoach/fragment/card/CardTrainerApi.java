package com.qingchengfit.fitcoach.fragment.card;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.model.responese.CacluScore;
import cn.qingchengfit.model.responese.Cards;
import cn.qingchengfit.model.responese.QcResponseOption;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
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

  //工作人员 卡类型详情
  @GET("/api/coaches/{Staff}/cardtpls/{id}/")
  rx.Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(@Path("Staff") String staff,
      @Path("id") String id, @QueryMap HashMap<String, Object> parasm);

  //获取会员卡
  @GET("/api/coaches/{id}/cards/?order_by=-id")
  rx.Observable<QcDataResponse<CardListWrap>> getAllCards(@Path("id") String staffid,
      @QueryMap HashMap<String, Object> params);

  //购卡
  @POST("/api/coaches/{id}/cashier/cards/create/")
  rx.Observable<QcDataResponse<JsonObject>> qcCreateRealcard(@Path("id") String staffid,
      @Body CardBuyBody body, @QueryMap HashMap<String, Object> params);


  //充值扣费
  @POST("/api/coaches/{coach_id}/cashier/cards/{card_id}/charge/")
  rx.Observable<QcDataResponse<JsonObject>> qcCardCharge(@Path("coach_id") String coach_id,
      @Path("card_id") String cardid, @QueryMap HashMap<String, Object> params,
      @Body CardBuyBody body);

  //工作人员 卡类型
  @GET("/api/coaches/{id}/cardtpls/list/?show_all=1&order_by=-id")
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(@Path("id") String id,
      @QueryMap HashMap<String, Object> params, @Query("type") String type,
      @Query("is_enable") String isEnable);

  //获取筛选列表
  @GET("/api/coaches/{id}/filter/cardtpls/?show_all=1")
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardFilterCondition(@Path("id") String staff,
      @QueryMap HashMap<String, Object> params);


  @GET("/api/coaches/{staff_id}/scores/calu/")
  rx.Observable<QcDataResponse<CacluScore>> qcGetScoreCalu(@Path("staff_id") String staff_id,
      @Query("type") String type, @Query("number") String money, @QueryMap
      ArrayMap<String, String> params);




}
