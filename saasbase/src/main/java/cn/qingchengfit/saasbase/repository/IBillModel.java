package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.bill.beans.BillLock;
import cn.qingchengfit.saasbase.bill.beans.BillPayStatus;
import cn.qingchengfit.saasbase.bill.beans.BillTotalWrapper;
import cn.qingchengfit.saasbase.bill.beans.BillWrapper;
import cn.qingchengfit.saascommon.filter.model.FilterWrapper;
import cn.qingchengfit.saasbase.bill.network.BusinessOrderWrap;
import cn.qingchengfit.saasbase.bill.network.PayRequestListWrap;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponseWrap;
import java.util.HashMap;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by fb on 2017/10/16.
 */

public interface IBillModel {
  /**
   * 直接支付
   * @param amount 单位（分）
   */
  Observable<QcDataResponse<PayBusinessResponseWrap>> directPay(long  amount);

  /**
   *  支付请求列表
   */
  Observable<QcDataResponse<PayRequestListWrap>> getPayRequestList(int page);

  /**
   *  支付订单详情（都是支付完成才能获取到）
   */
  Observable<QcDataResponse<BusinessOrderWrap>> getBusinessOrderDetail(String businessOrderId);

  Observable<QcDataResponse> editBusinessOrderMark(String businessOrderId,String mark);

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
  Observable<QcDataResponse<BillTotalWrapper>> queryBillTotal(String businessOrderId);

  /**
   * 获取账单列表
   * @param gym_id
   * @return
   */
  Observable<QcDataResponse<BillWrapper>> queryBillList(String gym_id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 筛选列表
   * @return
   */
  Observable<QcDataResponse<FilterWrapper>> getBillFilterList(String gymId);

}
