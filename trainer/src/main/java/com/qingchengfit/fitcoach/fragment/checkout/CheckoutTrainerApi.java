package com.qingchengfit.fitcoach.fragment.checkout;

import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.bean.CashierBean;
import io.reactivex.Flowable;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface CheckoutTrainerApi {

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
  @GET("api/coaches/{coach_id}/cashier/stat/")
  Flowable<QcDataResponse<HomePageBean>> qcGetCheckoutHomeInfo(@Path("coach_id") String staff_id,
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

  @POST("api/coaches/{coach_id}/cashier/create/")
  Flowable<QcDataResponse<CashierBean>> qcPostCashierOrder(@Path("coach_id") String staff_id,
      @Body Map<String, Object> params);

  }
