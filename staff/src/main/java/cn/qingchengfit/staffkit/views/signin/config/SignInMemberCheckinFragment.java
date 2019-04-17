package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.staffkit.databinding.FragmentSignInMemberBinding;

public class SignInMemberCheckinFragment
    extends SaasBindingFragment<FragmentSignInMemberBinding, SignInMemberVM> {
  @Override protected void subscribeUI() {

  }

  @Override
  public FragmentSignInMemberBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = FragmentSignInMemberBinding.inflate(inflater, container, false);
    return mBinding;
  }
}
