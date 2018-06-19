package cn.qingchengfit.saasbase.mvvm_student.view.state;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.databinding.PageStudentStateInfoBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;

public class StudentStateInfoPage extends
    StudentBaseFragment<PageStudentStateInfoBinding,StudentStateInfoViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageStudentStateInfoBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageStudentStateInfoBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
