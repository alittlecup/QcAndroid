package cn.qingchengfit.saasbase.course.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.databinding.FragmentCourseDetailBinding;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;

public class CourseDetailFragment extends SaasBindingFragment<FragmentCourseDetailBinding, BaseViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public FragmentCourseDetailBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return null;
  }
}
