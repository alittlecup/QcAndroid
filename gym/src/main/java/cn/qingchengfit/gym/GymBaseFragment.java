package cn.qingchengfit.gym;

import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import cn.qingchengfit.gym.BuildConfig;
import cn.qingchengfit.gym.routers.GymParamsInjector;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;

public abstract class GymBaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasBindingFragment<DB, VM> {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    GymParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
    if (mViewModel != null) {
      mViewModel.getErrorMsg().observe(this, str -> {
        if (!TextUtils.isEmpty(str)) {
          ToastUtils.show(str);
        }
      });
    }
  }

  @Override protected void routeTo(String uri, Bundle bd, boolean b) {
    if (BuildConfig.RUN_AS_APP) {
      if (!uri.startsWith("/")) {
        uri = "/" + uri;
      }
      if (this.getActivity() instanceof BaseActivity) {
        this.routeTo(Uri.parse(BuildConfig.PROJECT_NAME
            + "://"
            + ((BaseActivity) this.getActivity()).getModuleName()
            + uri), bd, b);
      }
    } else {
      super.routeTo(uri, bd, b);
    }
  }
}
