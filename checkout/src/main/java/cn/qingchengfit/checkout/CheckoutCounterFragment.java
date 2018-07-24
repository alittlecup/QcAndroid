package cn.qingchengfit.checkout;

import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import cn.qingchengfit.checkout.routers.CheckoutParamsInjector;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;

public abstract class CheckoutCounterFragment<DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasBindingFragment<DB, VM> {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    CheckoutParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
    if (mViewModel != null) {
      mViewModel.getErrorMsg().observe(this, str -> ToastUtils.show(str));
    }
    mViewModel.defaultResult.observe(this, objectResource -> {
      dealResource(objectResource);
    });
  }

  public <T> void dealResource(Resource<T> resource) {
    switch (resource.status) {
      case SUCCESS:
        if (resource.data != null && resource.data instanceof String
            && !TextUtils.isEmpty((String)resource.data)
            ){
          handleHttpSuccess((String) resource.data);
        }
        break;
      case ERROR:
        ToastUtils.show(resource.message);
        break;
      case LOADING:
        break;
    }

  }

  protected void handleHttpSuccess(String s){

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
