package cn.qingchengfit.checkout.repository;

import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.CheckoutBillWrapper;
import cn.qingchengfit.checkout.bean.CheckoutBills;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.network.response.QcDataResponse;
import com.google.gson.JsonObject;
import io.reactivex.Flowable;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
  Flowable<QcDataResponse<HomePageBean>> qcGetCheckoutHomeInfo(@Path("staff_id") String staff_id,
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

  @GET("api/v2/staffs/{staff_id}/shop-qrcode/bills/?show_all=1")
  Flowable<QcDataResponse<CheckoutBills>> qcLoadCheckoutBills(@Path("staff_id") String staff_id,
      @QueryMap Map<String, Object> params);

  @GET("api/v2/staffs/{staff_id}/shop-qrcode/bills/{bill_id}/")
  Flowable<QcDataResponse<CheckoutBillWrapper>> qcLoadCheckoutBillDetail(
      @Path("staff_id") String staff_id, @Path("bill_id") String bill_id,
      @QueryMap Map<String, Object> params);

  @PUT("api/v2/staffs/{staff_id}/shop-qrcode/bills/{bill_id}/")
  Flowable<QcDataResponse<CheckoutBillWrapper>> qcPutBillDetail(@Path("staff_id") String staff_id,
      @Path("bill_id") String bill_id, @Body JsonObject body, @QueryMap Map<String, Object> params);
}
