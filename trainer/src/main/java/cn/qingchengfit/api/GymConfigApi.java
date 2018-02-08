package cn.qingchengfit.api;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.gymconfig.network.response.SpaceListWrap;
import cn.qingchengfit.saasbase.network.response.GymList;
import java.util.HashMap;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
 * Created by Paper on 2018/1/29.
 */

public interface GymConfigApi {
  //获取某个健身房的场地列表
  @GET("/api/v1/coaches/{coach_id}/spaces/") rx.Observable<QcDataResponse<SpaceListWrap>> qcGetGymSitesPermisson(@Path("coach_id") String coach_id,
    @QueryMap HashMap<String, Object> params);

  //获取教练系统
  @GET("/api/v1/coaches/{id}/services/") rx.Observable<QcDataResponse<GymList>> qcGetCoachService(@Path("id") String id);

  ///**
  // * 查询场馆配置
  // */
  //@GET("/api/staffs/{staff_id}/shops/configs/")
  //rx.Observable<QcDataResponse<ShopConfigListWrap>> qcGetShopConfig(@Path("staff_id") String staff_id,
  //  @Query("keys") String key, @QueryMap HashMap<String, Object> params);
  //
  ///**
  // * 健身房所有设置
  // * http://192.168.1.7:8000/api/staffs/3281/shops/configs/?id=5370&model=staff_gym
  // */
  //@PUT("/api/staffs/{staff_id}/shops/configs/")
  //rx.Observable<QcDataResponse> saveShopConfigs(
  //  @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params, @Body
  //  ShopConfigBody body);
}
