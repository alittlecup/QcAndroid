package cn.qingchengfit.checkout.view.checkout;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import javax.inject.Inject;

public class CheckoutMoneyViewModel extends BaseViewModel {
  public final MutableLiveData<String> count = new MutableLiveData<>();
  public final MutableLiveData<Boolean> enable = new MutableLiveData<>();

  @Inject public CheckoutMoneyViewModel() {
    count.setValue("");
  }

  public boolean showOverPrices(String count) {
    if(TextUtils.isEmpty(count)){
      return false;
    }
    Double price = Double.valueOf(count);
    if (price > 20000) {
      return true;
    } else {
      return false;
    }
  }
}
