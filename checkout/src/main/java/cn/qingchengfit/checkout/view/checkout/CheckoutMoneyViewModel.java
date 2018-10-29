package cn.qingchengfit.checkout.view.checkout;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;
import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.PayChannel;
import cn.qingchengfit.checkout.repository.CheckoutRepository;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class CheckoutMoneyViewModel extends BaseViewModel {
  public final MutableLiveData<String> count = new MutableLiveData<>();
  public final MutableLiveData<Boolean> enable = new MutableLiveData<>();
  public final MutableLiveData<CashierBean> cashierBean = new MutableLiveData<>();
  @Inject CheckoutRepository repository;

  public int getType() {
    return type;
  }

  private @PayChannel int type;

  @Inject public CheckoutMoneyViewModel() {
    count.setValue("");
  }

  public boolean showOverPrices(String count) {
    if (TextUtils.isEmpty(count)) {
      return false;
    }
    Double price = Double.valueOf(count);
    if (price > 20000) {
      return true;
    } else {
      return false;
    }
  }

  public void getOrderInfo(@PayChannel int channel, String price, String remarks) {
    Map<String, Object> params = new HashMap<>();
    type = channel;
    params.put("channel", "ALIPAY_QRCODE");
    switch (channel) {
      case PayChannel.ALIPAY_QRCODE:
        break;
      case PayChannel.WEIXIN_QRCODE:
        params.put("channel", "WEIXIN_QRCODE");
        break;
      default:
        return;
    }
    params.put("price", Double.parseDouble(price));
    params.put("remarks", remarks);
    repository.qcPostCashierOrder(cashierBean, defaultResult, params);
  }
}
