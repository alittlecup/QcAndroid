package cn.qingchengfit.pos.net;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import java.util.HashMap;
import retrofit2.http.GET;
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
 * Created by Paper on 2017/9/25.
 */

public interface PosApi {
  //工作人员 卡类型
  @GET("/api/v2/staffs/{id}/cardtpls/all/?show_all=1&order_by=-id") rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(
      @Path("id") String id, @QueryMap HashMap<String, Object> params, @Query("type") String type, @Query("is_enable") String isEnable);

  //工作人员 卡类型详情
  @GET("/api/staffs/{Staff}/cardtpls/{id}/") rx.Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(
      @Path("Staff") String staff, @Path("id") String id, @QueryMap HashMap<String, Object> parasm);

  // 工作人员 卡类型 规格
  @GET("/api/staffs/{staff_id}/cardtpls/{cardtps_id}/options/") rx.Observable<QcDataResponse<CardTplOptionListWrap>> qcGetOptions(
      @Path("staff_id") String staff_id, @Path("cardtps_id") String cardtps_id, @QueryMap HashMap<String, Object> params );





  //会员卡可绑定的会员列表
  @GET("/api/staffs/{staff_id}/users/?show_all=1") rx.Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(
      @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);

  //工作人员下所有会员
  //    @GET("/api/staffs/{id}/users/all/?show_all=1")
  @GET("/api/staffs/{id}/users/?show_all=1") rx.Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(@Path("id") String id,
      @QueryMap HashMap<String, Object> params);

  //获取会员卡
  @GET("/api/staffs/{id}/cards/all/?order_by=-id") rx.Observable<QcDataResponse<CardListWrap>> getAllCards(@Path("id") String staffid,
      @QueryMap HashMap<String, Object> params);
}
