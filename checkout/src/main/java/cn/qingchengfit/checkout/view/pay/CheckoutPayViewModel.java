package cn.qingchengfit.checkout.view.pay;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.checkout.bean.OrderStatusBeanWrapper;
import cn.qingchengfit.checkout.repository.CheckoutRepository;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.checkout.bean.IOrderData;
import javax.inject.Inject;

public class CheckoutPayViewModel extends BaseViewModel {
  @Inject CheckoutRepository repository;
  public final MutableLiveData<OrderStatusBeanWrapper> orderStatusBean = new MutableLiveData<>();
  public final MutableLiveData<IOrderData> IOrderData = new MutableLiveData<>();

  @Inject public CheckoutPayViewModel() {

  }

  public void pollingOrderresult(String pollingNumber) {
    repository.qcGetOrderStatus(orderStatusBean, defaultResult, pollingNumber);
  }
}
