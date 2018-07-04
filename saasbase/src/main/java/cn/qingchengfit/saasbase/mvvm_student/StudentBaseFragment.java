package cn.qingchengfit.saasbase.mvvm_student;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;

public abstract class StudentBaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasBindingFragment<DB, VM> {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  public StudentActivityViewModel getActivityViewModel() {
    return ViewModelProviders.of(getActivity(), factory).get(StudentActivityViewModel.class);
  }
}
