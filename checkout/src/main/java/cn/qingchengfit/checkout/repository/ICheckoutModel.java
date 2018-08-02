package cn.qingchengfit.checkout.repository;

import cn.qingchengfit.checkout.bean.OrderStatusBeanWrapper;
import cn.qingchengfit.checkout.routers.Icheckout;
import cn.qingchengfit.saascommon.bean.CashierBean;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBean;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.network.response.QcDataResponse;
import io.reactivex.Flowable;
import java.util.Map;

public interface ICheckoutModel {
  Flowable<QcDataResponse<HomePageBean>> qcGetHomePageInfo();

  Flowable<QcDataResponse<CashierBean>> qcPostCashierOrder(
      Map<String, Object> params);

  Flowable<QcDataResponse<ScanResultBean>> qcPostScanOrder(Map<String, Object> params);

  Flowable<QcDataResponse<OrderStatusBeanWrapper>> qcGetOrderStatus(String orderNum);
}
