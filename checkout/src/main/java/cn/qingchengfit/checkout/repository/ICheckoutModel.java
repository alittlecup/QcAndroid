package cn.qingchengfit.checkout.repository;

import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBean;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.network.response.QcDataResponse;
import io.reactivex.Flowable;
import java.util.Map;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ICheckoutModel {
  Flowable<QcDataResponse<HomePageBean>> qcGetHomePageInfo(String staff_id,
      Map<String, Object> params);

  Flowable<QcDataResponse<CashierBean>> qcPostCashierOrder(String staff_id,
      Map<String, Object> params);

  Flowable<QcDataResponse<ScanResultBean>> qcPostScanOrder(Map<String, Object> params);

  Flowable<QcDataResponse<OrderStatusBean>> qcGetOrderStatus(String orderNum,Map<String,Object> params);
}
