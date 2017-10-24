package cn.qingchengfit.pos.net;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.bill.beans.BillLock;
import cn.qingchengfit.saasbase.bill.beans.BillPayStatus;
import cn.qingchengfit.saasbase.bill.beans.BillTotal;
import cn.qingchengfit.saasbase.bill.beans.BusinessBill;
import cn.qingchengfit.saasbase.bill.network.BusinessOrderWrap;
import cn.qingchengfit.saasbase.bill.network.PayRequestListWrap;
import java.util.HashMap;
import java.util.List;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
 * Created by Paper on 2017/10/22.
 */

public interface BillApi {
  @GET("/api/rongshu/gyms/{gym_id}/bills/{billid}/")
  Observable<QcDataResponse<BusinessOrderWrap>> getBillDetail(@Path("gym_id") String gymid, @Path("billid") String billId,@QueryMap HashMap<String,Object> params);

  @GET("/api/rongshu/payments/{billid}/status/")
  Observable<QcDataResponse<BillPayStatus>> getBillStatus(@Path("billid") String billId,@QueryMap HashMap<String,Object> params);

  /**
   * Pay request list
   */
  @GET("/api/rongshu/gyms/{gym_id}/payment/tasks/")
  Observable<QcDataResponse<PayRequestListWrap>> getPayRequsetList(@Path("gym_id") String gymid,@QueryMap HashMap<String,Object> params);

  /**
   * 锁定账单
   */
  @POST("/api/rongshu/tasks/{task_id}/to/payment/")
  Observable<QcDataResponse<BillLock>> lockPayRequest(@Path("task_id") String task_id);

  /**
   * 取消订单
   */
  @DELETE("/api/rongshu/tasks/{task_id}/to/payment/")
  Observable<QcDataResponse> cancelPayRequest(@Path("task_id") String task_id);

  /**
   * 账单列表
   */
  @GET("/api/rongshu/gyms/{gym_id}/bills/")
  rx.Observable<QcDataResponse<List<BusinessBill>>> getBillList(@Path("gym_id") String gym_id);

  /**
   * 账单统计 //TODO 未给出
   */

  @GET("/api/rongshu/gyms/{gym_id}/bills/")
  rx.Observable<QcDataResponse<BillTotal>> getBillToTal(@Path("gym_id") String gym_id);

}
