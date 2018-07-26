package cn.qingchengfit.checkout.repository;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.saascommon.bean.CashierBean;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBean;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.saascommon.network.Resource;
import java.util.Map;

public interface CheckoutRepository {

  void qcGetHomePageInfo(MutableLiveData<HomePageBean> result,
      MutableLiveData<Resource<Object>> defaultRes);

  void qcPostCashierOrder(MutableLiveData<CashierBean> result,
      MutableLiveData<Resource<Object>> defaultRes, Map<String, Object> params);

  void qcPostScanOrder(MutableLiveData<ScanResultBean> result,
      MutableLiveData<Resource<Object>> defaultRes, Map<String, Object> params);

  void qcGetOrderStatus(MutableLiveData<OrderStatusBean> result,
      MutableLiveData<Resource<Object>> defaultRes, String orderNum);
}
