package cn.qingchengfit.checkout.repository;

import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBean;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.network.response.QcDataResponse;
import io.reactivex.Flowable;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface CheckoutApi {

  /**
   * // 收银台
   * GET: api/staffs/:staff_id/cashier/stat/
   * {
   * sum: 1000.00
   * new: 10,
   * charge: 10,
   * cashier: 10,
   * }
   */
  @GET("api/staffs/{staff_id}/cashier/stat/")
  Flowable<QcDataResponse<HomePageBean>> qcGetCeckoutHomeInfo(@Path("staff_id") String staff_id,
      @QueryMap Map<String, Object> params);

  /**
   * //收银台直接收银下单
   * POST: api/staffs/:staff_id/cashier/create/
   * body:{
   * channel: ALIPAY_QRCODE / WEIXIN_QRCODE / ALIPAY_BARCODE / WEIXIN_BARCODE,
   * price: 10.00,
   * }
   * return:{
   * pay_trade_no: "",
   * out_trade_no: "",    # 根据此单号轮询
   * url: "",    # 支付地址，需要转成二维码展示
   * }
   */

  @POST("api/staffs/{staff_id}/cashier/create/")
  Flowable<QcDataResponse<CashierBean>> qcPostCashierOrder(@Path("staff_id") String staff_id,
      @Body Map<String, Object> params);

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
  @GET("api/payments/{out_trade_no}/status/") Flowable<QcDataResponse<OrderStatusBean>> qcGetOrderStatus(@Path("out_trade_no")String orderNum,@QueryMap Map<String,Object> params);
}
