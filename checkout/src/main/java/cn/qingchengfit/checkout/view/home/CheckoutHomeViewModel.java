package cn.qingchengfit.checkout.view.home;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.repository.CheckoutRepository;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import javax.inject.Inject;

public class CheckoutHomeViewModel extends BaseViewModel {
  public final MutableLiveData<HomePageBean> homeInfo = new MutableLiveData<>();
  @Inject CheckoutRepository checkoutRepository;

  @Inject public CheckoutHomeViewModel() {

  }

  public void loadHomePageInfo() {
    checkoutRepository.qcGetHomePageInfo(homeInfo,defaultResult);
  }
}
