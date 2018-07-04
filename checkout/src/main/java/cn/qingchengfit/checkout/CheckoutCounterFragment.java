package cn.qingchengfit.checkout;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.checkout.routers.CheckoutParamsInjector;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;

public abstract class CheckoutCounterFragment<DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasBindingFragment<DB, VM> {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    CheckoutParamsInjector.inject(this);
  }
}
