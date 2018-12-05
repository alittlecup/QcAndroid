package cn.qingchengfit.checkout.view.order;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.checkout.bean.PayChannel;
import cn.qingchengfit.checkout.bean.TestOrderData;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class CheckoutOrderListVM extends BaseViewModel {
  public MutableLiveData<List<TestOrderData>> getDatas() {
    return datas;
  }

  private MutableLiveData<List<TestOrderData>> datas = new MutableLiveData<>();

  @Inject public CheckoutOrderListVM() {
  }

  public void loadOrderList() {

  }

  public void test() {
    List<TestOrderData> data = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      data.add(
          new TestOrderData((i % 2 == 0) ? PayChannel.ALIPAY_QRCODE : PayChannel.WEIXIN_QRCODE));
    }
    datas.setValue(data);
  }
}
