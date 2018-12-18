package cn.qingchengfit.checkout.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.CheckoutBillWrapper;
import cn.qingchengfit.checkout.bean.CheckoutBills;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBeanWrapper;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.saascommon.network.Resource;
import com.google.gson.JsonObject;
import java.util.Map;

public interface CheckoutRepository {

  void qcGetHomePageInfo(MutableLiveData<HomePageBean> result,
      MutableLiveData<Resource<Object>> defaultRes);

  void qcPostCashierOrder(MutableLiveData<CashierBean> result,
      MutableLiveData<Resource<Object>> defaultRes, Map<String, Object> params);

  void qcPostScanOrder(MutableLiveData<ScanResultBean> result,
      MutableLiveData<Resource<Object>> defaultRes, Map<String, Object> params);

  void qcGetOrderStatus(MutableLiveData<OrderStatusBeanWrapper> result,
      MutableLiveData<Resource<Object>> defaultRes, String orderNum);

  LiveData<Resource<CheckoutBills>> qcLoadCheckouQrOrders();
  LiveData<Resource<CheckoutBillWrapper>> qcLoadCheckoutQrOrderDetail(String id);
  LiveData<Resource<CheckoutBillWrapper>> qcPutCheckoutQrOrderDetail(String id, JsonObject body);
}
