package cn.qingchengfit.pos.net;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.cashier.model.CashierBody;
import cn.qingchengfit.pos.cashier.model.CashierWrapper;
import cn.qingchengfit.pos.exchange.beans.ExchangeWrapper;
import cn.qingchengfit.pos.login.model.GetCodeBody;
import cn.qingchengfit.pos.login.model.GymResponse;
import cn.qingchengfit.pos.login.model.Login;
import cn.qingchengfit.pos.login.model.LoginBody;
import cn.qingchengfit.saasbase.student.network.body.AddStdudentBody;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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

  //会员卡可绑定的会员列表
  @GET("/api/rongshu/gyms/{id}/users/?show_all=1")
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(
      @Path("id") String id, @QueryMap HashMap<String, Object> params);

  //工作人员下所有会员
  //    @GET("/api/staffs/{id}/users/all/?show_all=1")
  @GET("/api/staffs/{id}/users/?show_all=1")
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(@Path("id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 登录
   */
  //TODO 后续需要改
  @POST("/api/rongshu/cashier/login/") Observable<QcDataResponse<Login>> qcLogin(@Body LoginBody loginBody);

  /**
   * 根据设备IMEI号查询场馆相关信息
   */
  @GET("/api/rongshu/gym/") rx.Observable<QcDataResponse<GymResponse>> qcGetGym(
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取验证码
   */
  @POST("/api/rongshu/cashier/send/") Observable<QcDataResponse> qcGetCode(
      @Body GetCodeBody body);



  /**
   * 新增会员
   */
  @POST("/api/rongshu/gyms/{id}/users/") rx.Observable<QcDataResponse> qcCreateStudent(
      @Path("id") String id, @QueryMap HashMap<String, Object> params,
      @Body AddStdudentBody body);

  /**
   * 新增收银员
   */
  @POST("/api/rongshu/cashier/") rx.Observable<QcDataResponse> qcAddCashier(
      @Body CashierBody body);

  /**
   * 删除收银员
   */
  @HTTP(method = "DELETE", path = "/api/rongshu/cashier/{cashier_id}/", hasBody = true)
  rx.Observable<QcDataResponse> qcDeleteCashier(
      @Path("cashier_id") String cashierId, @Body HashMap<String, Object> params);

  /**
   * 修改收银员
   */
  @PUT("/api/rongshu/cashier/{cashier_id}/") rx.Observable<QcDataResponse> qcModifyCashier(
      @Path("cashier_id") String cashierId, @Body CashierBody body);

  /**
   * 获取收银员
   */
  @GET("/api/rongshu/cashier/") rx.Observable<QcDataResponse<CashierWrapper>> qcGetCashier(@QueryMap HashMap<String, Object> params);

  /**
   * 交班
   */
  @GET("/api/rongshu/gyms/{gym_id}/exchange/") rx.Observable<QcDataResponse<ExchangeWrapper>> qcGetExchange(@Path("gym_id") String gym_id);


}
