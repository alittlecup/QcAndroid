package cn.qingchengfit.saascommon.mvvm;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/11/15.
 */

public abstract class SaasBindingFragment<DB extends ViewDataBinding, VM extends BaseViewModel>
    extends SaasCommonFragment {

  public DB mBinding;
  public VM mViewModel;

  @Inject public ViewModelProvider.Factory factory;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mViewModel = ViewModelProviders.of(this, factory).get(getVMClass());
    subscribeUI();
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = initDataBinding(inflater, container, savedInstanceState);
    return mBinding.getRoot();
  }

  protected abstract void subscribeUI();

  public abstract DB initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState);

  protected Class<VM> getVMClass() {
    ParameterizedType type ;
    if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
      type = (ParameterizedType) getClass().getGenericSuperclass();
    } else {
      type = (ParameterizedType) getClass().getSuperclass().getGenericSuperclass();
    }
    Type[] actualTypeArguments = type.getActualTypeArguments();
    if (actualTypeArguments.length == 2) {
      return (Class<VM>) actualTypeArguments[1];
    }
    return (Class<VM>) BaseViewModel.class;
  }
}





