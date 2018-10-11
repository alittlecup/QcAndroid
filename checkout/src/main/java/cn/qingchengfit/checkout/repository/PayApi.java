package cn.qingchengfit.checkout.repository;

import cn.qingchengfit.checkout.bean.OrderStatusBean;
import cn.qingchengfit.checkout.bean.OrderStatusBeanWrapper;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.network.response.QcDataResponse;
import io.reactivex.Flowable;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface PayApi {
  /**
   * //扫码支付扣款
   * POST: payments/barcode/
   * body: {
   * barcode:"",
   * out_trade_no: "",
   * pay_trade_no: "",
   * }
   * return: {
   * successful：true/false
   * }
   */
  @POST("payments/barcode/") Flowable<QcDataResponse<ScanResultBean>> qcPostScanOrder(
      @Body Map<String, Object> params);

  /**
   * //二维码界面轮询订单状态
   * GET: api/payments/:out_trade_no/status/
   * return:{
   * pay_trade_no: "",
   * status: 1, // 0 失败，1 新建，2 成功，3 已退款
   * success_url: "", // 成功页面
   * fail_url: "",    //失败页面
   * }
   */
  @GET("api/payments/{out_trade_no}/status/") Flowable<QcDataResponse<OrderStatusBeanWrapper>> qcGetOrderStatus(@Path("out_trade_no")String orderNum,@QueryMap
      Map<String,Object> params);

}
