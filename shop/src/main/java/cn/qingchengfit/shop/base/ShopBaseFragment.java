package cn.qingchengfit.shop.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.shop.routers.ShopParamsInjector;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public abstract class ShopBaseFragment<DB extends ViewDataBinding, VM extends ViewModel>
    extends SaasBaseFragment {
  public DB mBinding;
  public VM mViewModel;

  @Inject public ViewModelProvider.Factory factory;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ShopParamsInjector.inject(this);
    mViewModel = ViewModelProviders.of(this, factory).get(getVMClass());
    subscribeUI();
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    //        if (mBinding == null) {
    mBinding = initDataBinding(inflater, container, savedInstanceState);
    //        }
    ViewGroup parent = (ViewGroup) mBinding.getRoot().getParent();
    if (parent != null) {
      parent.removeView(mBinding.getRoot());
      parent.removeAllViews();
    }
    return mBinding.getRoot();
  }

  protected abstract void subscribeUI();

  public abstract DB initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState);

  protected Class<VM> getVMClass() {
    Type[] actualTypeArguments =
        ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
    if (actualTypeArguments.length == 2) {
      return (Class<VM>) actualTypeArguments[1];
    }
    return (Class<VM>) BaseViewModel.class;
  }
}
