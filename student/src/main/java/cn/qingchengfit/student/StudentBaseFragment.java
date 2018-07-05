package cn.qingchengfit.student;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.student.routers.StudentParamsInjector;

public abstract class StudentBaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasBindingFragment<DB, VM> {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StudentParamsInjector.inject(this);
  }

}
