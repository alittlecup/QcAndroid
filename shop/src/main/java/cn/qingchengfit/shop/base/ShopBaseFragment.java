package cn.qingchengfit.shop.base;

import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.shop.BuildConfig;
import cn.qingchengfit.shop.routers.ShopParamsInjector;
import cn.qingchengfit.views.activity.BaseActivity;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public abstract class ShopBaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasBindingFragment<DB,VM> {


  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    ShopParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Override public boolean isBlockTouch() {
    return false;
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
