package cn.qingchengfit.checkout.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.CheckoutBillWrapper;
import cn.qingchengfit.checkout.bean.CheckoutBills;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBeanWrapper;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.network.BindLiveData;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import com.google.gson.JsonObject;
import io.reactivex.Flowable;
import java.util.Map;
import javax.inject.Inject;

public class CheckoutRepositoryImpl implements CheckoutRepository {
  @Inject ICheckoutModel remoteService;

  @Inject public CheckoutRepositoryImpl() {
  }

  static <T> void bindToLiveData(MutableLiveData<T> liveData,
      Flowable<QcDataResponse<T>> observable, MutableLiveData<Resource<Object>> result,
      String tag) {
    BindLiveData.bindLiveData(observable.compose(RxHelper.schedulersTransformerFlow()), liveData,
        result, tag);
  }

  static <T> LiveData<Resource<T>> toLiveData(Flowable<QcDataResponse<T>> observable) {
    return LiveDataTransfer.fromPublisher(observable.compose(RxHelper.schedulersTransformerFlow()));
  }

  @Override public void qcGetHomePageInfo(MutableLiveData<HomePageBean> result,
      MutableLiveData<Resource<Object>> defaultRes) {
    bindToLiveData(result, remoteService.qcGetHomePageInfo(), defaultRes, "");
  }

  @Override public void qcPostCashierOrder(MutableLiveData<CashierBean> result,
      MutableLiveData<Resource<Object>> defaultRes, Map<String, Object> params) {
    bindToLiveData(result, remoteService.qcPostCashierOrder(params), defaultRes, "");
  }

  @Override public void qcPostScanOrder(MutableLiveData<ScanResultBean> result,
      MutableLiveData<Resource<Object>> defaultRes, Map<String, Object> params) {
    bindToLiveData(result, remoteService.qcPostScanOrder(params), defaultRes, "");
  }

  @Override public void qcGetOrderStatus(MutableLiveData<OrderStatusBeanWrapper> result,
      MutableLiveData<Resource<Object>> defaultRes, String orderNum) {
    bindToLiveData(result, remoteService.qcGetOrderStatus(orderNum), defaultRes, "");
  }

  @Override public LiveData<Resource<CheckoutBills>> qcLoadCheckouQrOrders() {
    return toLiveData(remoteService.qcLoadCheckoutQrOrders());
  }

  @Override public LiveData<Resource<CheckoutBillWrapper>> qcLoadCheckoutQrOrderDetail(String id) {
    return toLiveData(remoteService.qcLoadCheckoutQrOrderDetail(id));
  }

  @Override public LiveData<Resource<CheckoutBillWrapper>> qcPutCheckoutQrOrderDetail(String id,
      JsonObject body) {
    return toLiveData(remoteService.qcPutCheckoutQrOrderDetail(id, body));
  }
}
