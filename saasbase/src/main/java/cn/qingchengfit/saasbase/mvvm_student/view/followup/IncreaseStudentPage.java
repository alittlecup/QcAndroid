package cn.qingchengfit.saasbase.mvvm_student.view.followup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.databinding.PageIncreaseStudentBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;

public class IncreaseStudentPage extends StudentBaseFragment<PageIncreaseStudentBinding,IncreaseStudentViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageIncreaseStudentBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageIncreaseStudentBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
