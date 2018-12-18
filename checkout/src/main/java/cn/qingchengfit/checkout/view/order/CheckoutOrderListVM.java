package cn.qingchengfit.checkout.view.order;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.checkout.bean.CheckoutBill;
import cn.qingchengfit.checkout.repository.CheckoutRepository;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.List;
import javax.inject.Inject;

public class CheckoutOrderListVM extends BaseViewModel {
  @Inject CheckoutRepository repository;

  public LiveData<List<CheckoutBill>> getDatas() {
    return datas;
  }

  private LiveData<List<CheckoutBill>> datas;
  private ActionLiveEvent start = new ActionLiveEvent();

  @Inject public CheckoutOrderListVM(CheckoutRepository repository) {
    this.repository = repository;
    datas = Transformations.switchMap(start,
        input -> Transformations.map(repository.qcLoadCheckouQrOrders(),
            input1 -> dealResource(input1) == null ? null : dealResource(input1).bills));
    start.call();
  }

  public void onRefresh() {
    start.call();
  }
}
