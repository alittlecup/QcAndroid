package cn.qingchengfit.staffkit.staff;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
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
 * Created by Paper on 2017/12/1.
 */

public interface StaffApi {

  //获取某个健身房的教练列表
  @GET("/api/staffs/{id}/coaches/") rx.Observable<QcDataResponse<SalerListWrap>> qcGetGymCoaches(@Path("id") String id,
    @Query("id") String gymid, @Query("model") String model, @Query("q") String keyword);

  //获取某个健身房的(无视权限)教练列表
  @GET("/api/staffs/{id}/method/coaches/") rx.Observable<QcDataResponse<SalerListWrap>> qcGetGymCoachesPermission(@Path("id") String id,
    @QueryMap HashMap<String, Object> params);

  //获取销售 卖卡  包含销售和教练
  @GET("/api/staffs/{staff_id}/sellers/") rx.Observable<QcDataResponse<SalerListWrap>> qcGetSalers(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

}
