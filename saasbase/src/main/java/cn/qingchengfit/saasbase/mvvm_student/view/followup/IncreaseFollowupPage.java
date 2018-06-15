package cn.qingchengfit.saasbase.mvvm_student.view.followup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.databinding.PageIncreaseFollowupBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;

public class IncreaseFollowupPage
    extends StudentBaseFragment<PageIncreaseFollowupBinding, IncreaseFollowViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageIncreaseFollowupBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageIncreaseFollowupBinding.inflate(inflater, container, false);
    return mBinding;
  }
}
