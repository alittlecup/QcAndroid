package cn.qingchengfit.checkout.view.order;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.checkout.bean.CheckoutBill;
import cn.qingchengfit.checkout.bean.OrderListItemData;
import cn.qingchengfit.checkout.repository.CheckoutRepository;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import com.google.gson.JsonObject;
import javax.inject.Inject;

public class OrderConfirmVM extends BaseViewModel {
  public LiveData<OrderListItemData> getData() {
    return data;
  }

  private LiveData<OrderListItemData> data;

  public LiveData<OrderListItemData> getPutResult() {
    return putResult;
  }

  private LiveData<OrderListItemData> putResult;
  private MutableLiveData<String> orderId = new MutableLiveData<>();
  private MutableLiveData<JsonObject> remarks = new MutableLiveData<>();

  @Inject public OrderConfirmVM(CheckoutRepository repository) {
    data = Transformations.switchMap(orderId,
        input -> Transformations.map(repository.qcLoadCheckoutQrOrderDetail(input),
            input1 -> dealResource(input1) == null ? null : dealResource(input1).bill));

    putResult = Transformations.switchMap(remarks, input -> Transformations.map(
        repository.qcPutCheckoutQrOrderDetail(orderId.getValue(), input),
        input1 -> dealResource(input1) == null ? null : dealResource(input1).bill));
  }

  public void putOrderDetail(CheckoutBill bill) {
    JsonObject jsonObject=new JsonObject();
    jsonObject.addProperty("remarks",bill.getRemarks());
    remarks.setValue(jsonObject);
  }

  public void loadOrderDetail(String id) {
    orderId.setValue(id);
  }
}
