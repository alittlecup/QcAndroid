package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.bill.beans.BillLock;
import cn.qingchengfit.saasbase.bill.beans.BillPayStatus;
import cn.qingchengfit.saasbase.bill.beans.BillTotal;
import cn.qingchengfit.saasbase.bill.beans.BusinessBill;
import cn.qingchengfit.saasbase.bill.network.BusinessOrderWrap;
import cn.qingchengfit.saasbase.bill.network.PayRequestListWrap;
import java.util.HashMap;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import java.util.List;
import rx.Observable;

/**
 * Created by fb on 2017/10/16.
 */

public interface IBillModel {
  /**
   *  支付请求列表
   */
  Observable<QcDataResponse<PayRequestListWrap>> getPayRequestList(int page);

  /**
   *  支付订单详情（都是支付完成才能获取到）
   */
  Observable<QcDataResponse<BusinessOrderWrap>> getBusinessOrderDetail(String businessOrderId);

  /**
   * 获取订单列表（筛选 ）
   */
  Observable<QcDataResponse<BusinessOrderWrap>> getBusinessOrderList(HashMap<String,Object> params);

  /**
   *  获取支付订单的状态（是否已经支付
   */
  //api/rongshu/payments/:order_no/status/
  Observable<QcDataResponse<BillPayStatus>> queryBillStatus(String businessOrderId);

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

   /** 获取账单总计
   * @param businessOrderId
   * @return
   */
  Observable<QcDataResponse<BillTotal>> queryBillTotal(String businessOrderId);

  /**
   * 获取账单列表
   * @param gym_id
   * @return
   */
  Observable<QcDataResponse<List<BusinessBill>>> queryBillList(String gym_id);

}
