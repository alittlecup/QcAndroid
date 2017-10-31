package cn.qingchengfit.pos.net;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.model.QcResponsePostions;
import cn.qingchengfit.saasbase.staff.model.body.ChangeSuBody;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
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
 * Created by Paper on 2017/10/18.
 */

public interface StaffApi {
  /**
   * 获取当前用户信息
   */
  @GET("/api/info/current/user/")
  rx.Observable<QcDataResponse<UserWrap>> getCurrentUser(@QueryMap HashMap<String, Object> params);


  /**
   * 工作人员列表
   */
  @GET("/api/staffs/{id}/managers/?show_all=1")
  rx.Observable<QcDataResponse<SalerListWrap>> getStaffs(@Path("id") String staff_id,
    @QueryMap HashMap<String, Object> params, @Query("q") String keywork);

  /**
   * 职位列表
   */
  @GET("/api/staffs/{id}/positions/") rx.Observable<QcResponsePostions> getPostions(
    @Path("id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 无视权限的职位
   */
  @GET("/api/staffs/{id}/permissions/positions/")
  rx.Observable<QcResponsePostions> qcGetPermissionPostions(@Path("id") String staff_id,
    @QueryMap HashMap<String, Object> params, @Query("key") String permission);





  /**
   * 更改超级管理员
   */
  @PUT("/api/staffs/{staff_id}/superuser/") rx.Observable<QcDataResponse> changeSu(
    @Path("staff_id") String staffid, @QueryMap HashMap<String, Object> params,
    @Body ChangeSuBody body);


  //获取销售 卖卡  包含销售和教练
  @GET("/api/rongshu/seller/") rx.Observable<QcDataResponse<SalerListWrap>> qcGetSalers(
    @QueryMap HashMap<String, Object> params);
  /**
   * 新增工作人员
   */
  @POST("/api/rongshu/seller/") rx.Observable<QcDataResponse> addManager(
    @QueryMap HashMap<String, Object> params, @Body ManagerBody body);

  /**
   * 删除工作人员
   */
  @DELETE("/api/rongshu/seller/{mid}/") rx.Observable<QcDataResponse> delManager(
     @Path("mid") String cid,@QueryMap HashMap<String, Object> params);

  /**
   *  更新工作人员
   */
  @PUT("/api/rongshu/seller/{mid}/") rx.Observable<QcDataResponse> updateManager(
     @Path("mid") String cid, @QueryMap HashMap<String, Object> params
    , @Body ManagerBody body);


}

