package cn.qingchengfit.writeoff;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.writeoff.routers.WriteoffParamsInjector;

public abstract class WriteOffBaseFragment <DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasBindingFragment<DB, VM> {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    WriteoffParamsInjector.inject(this);
    super.onCreate(savedInstanceState);

  }
}
