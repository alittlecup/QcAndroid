package cn.qingchengfit.checkout.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBean;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.BindLiveData;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class CheckoutRepositoryImpl implements CheckoutRepository {
  @Inject ICheckoutModel remoteService;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject public CheckoutRepositoryImpl(){}

  static <T> void bindToLiveData(MutableLiveData<T> liveData,
      Flowable<QcDataResponse<T>> observable, MutableLiveData<Resource<Object>> result,
      String tag) {
    BindLiveData.bindLiveData(observable.compose(RxHelper.schedulersTransformerFlow()), liveData,
        result, tag);
  }

  @Override public void qcGetHomePageInfo(MutableLiveData<HomePageBean> result,
      MutableLiveData<Resource<Object>> defaultRes) {
    bindToLiveData(result,
        remoteService.qcGetHomePageInfo(loginStatus.staff_id(), gymWrapper.getParams()), defaultRes,
        "");
  }

  @Override public void qcPostCashierOrder(MutableLiveData<CashierBean> result,
      MutableLiveData<Resource<Object>> defaultRes, Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    bindToLiveData(result,remoteService.qcPostCashierOrder(loginStatus.staff_id(),params1),defaultRes,"");
  }

  @Override public void qcPostScanOrder(MutableLiveData<ScanResultBean> result,
      MutableLiveData<Resource<Object>> defaultRes, Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    bindToLiveData(result,remoteService.qcPostScanOrder(params1),defaultRes,"");
  }

  @Override public void qcGetOrderStatus(MutableLiveData<OrderStatusBean> result,
      MutableLiveData<Resource<Object>> defaultRes, String orderNum) {
    bindToLiveData(result,remoteService.qcGetOrderStatus(orderNum,gymWrapper.getParams()),defaultRes,"");
  }
}
