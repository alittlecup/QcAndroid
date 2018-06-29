package cn.qingchengfit.saasbase.mvvm_student.view.state;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PageSalerStudentStateBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentListView;
import com.anbillon.flabellum.annotations.Need;

public class SalerStudentStatePage
    extends StudentBaseFragment<PageSalerStudentStateBinding, SalerStudentStateViewModel> {
  @Need String title = "";
  StudentListView listView;
  SalerStateFilterView filterView;

  @Override protected void subscribeUI() {

  }

  @Override
  public PageSalerStudentStateBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageSalerStudentStateBinding.inflate(inflater, container, false);
    initToolbar();
    initFragment();
    return mBinding;
  }

  private void initFragment() {
    listView = new StudentListView();
    filterView = new SalerStateFilterView();

    stuff(R.id.fragment_list_container, listView);
    stuff(R.id.fragment_filter, filterView);
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel(title);
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
