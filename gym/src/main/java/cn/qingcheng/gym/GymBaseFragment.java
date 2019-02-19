package cn.qingcheng.gym;

import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.gym.BuildConfig;
import cn.qingchengfit.gym.routers.GymParamsInjector;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.views.activity.BaseActivity;

public abstract class GymBaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasBindingFragment<DB, VM> {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    GymParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Override public void routeTo(String model, String path, Bundle bd) {
    if (BuildConfig.RUN_AS_APP) {
      String uri = model + path;
      if (!uri.startsWith("/")) uri = "/" + uri;
      if (getActivity() instanceof BaseActivity) {
        routeTo(Uri.parse("qcgym:/" + uri), bd);
      }
    } else {
      super.routeTo(model, path, bd);
    }
  }
}
