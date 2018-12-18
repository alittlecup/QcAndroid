package cn.qingchengfit.checkout.repository;

import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.CheckoutBillWrapper;
import cn.qingchengfit.checkout.bean.CheckoutBills;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBeanWrapper;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.network.response.QcDataResponse;
import com.google.gson.JsonObject;
import io.reactivex.Flowable;
import java.util.Map;

public interface ICheckoutModel {
  Flowable<QcDataResponse<HomePageBean>> qcGetHomePageInfo();

  Flowable<QcDataResponse<CashierBean>> qcPostCashierOrder(Map<String, Object> params);

  Flowable<QcDataResponse<ScanResultBean>> qcPostScanOrder(Map<String, Object> params);

  Flowable<QcDataResponse<OrderStatusBeanWrapper>> qcGetOrderStatus(String orderNum);

  Flowable<QcDataResponse<CheckoutBills>> qcLoadCheckoutQrOrders();


  Flowable<QcDataResponse<CheckoutBillWrapper>> qcLoadCheckoutQrOrderDetail(String id);
  Flowable<QcDataResponse<CheckoutBillWrapper>> qcPutCheckoutQrOrderDetail(String id,
      JsonObject body);
}
