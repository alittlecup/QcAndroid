package cn.qingchengfit.saasbase.mvvm_student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PageIncreaseMemberBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentListView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;


@Leaf(module = "student",path = "/increase/member")
public class IncreaseMemberPage
    extends StudentBaseFragment<PageIncreaseMemberBinding, IncreaseMemberViewModel> {
  MemberIncreaseFilterView filterView;

  StudentListView listView;

  IncreaseMemberTopView topView;
  IncreaseMemberSortViewModel mSortViewModel;

  @Need @IncreaseStudentPage.IncreaseType String curType =
      IncreaseStudentPage.IncreaseType.INCREASE_FOLLOWUP;

  @Override protected void subscribeUI() {
    mSortViewModel.filterVisible.observe(this, aBoolean -> {
      mBinding.fragmentFilter.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
    });
    mSortViewModel.filterIndex.observe(this, index -> {
      filterView.showPage(index);
    });
    mSortViewModel.filterAction.observe(this, aVoid -> {
      // TODO: 2018/6/25  sort
    });
  }


  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    mSortViewModel = ViewModelProviders.of(this, factory).get(IncreaseMemberSortViewModel.class);
    super.onCreate(savedInstanceState);
  }
  @Override
  public PageIncreaseMemberBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageIncreaseMemberBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mSortViewModel);
    initFragment();
    initToolbar();
    return mBinding;
  }

  private void initToolbar() {
    topView.setType(curType);
    if (curType.equals(IncreaseStudentPage.IncreaseType.INCREASE_FOLLOWUP)) {
      ToolbarModel toolbarModel = new ToolbarModel("新用户跟进");
      mBinding.setToolbarModel(toolbarModel);
    } else if (curType.equals(IncreaseStudentPage.IncreaseType.INCREASE_STUDENT)) {
      ToolbarModel toolbarModel = new ToolbarModel("会员维护");
      mBinding.setToolbarModel(toolbarModel);
    }
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initFragment() {
    filterView = new MemberIncreaseFilterView();
    stuff(R.id.fragment_filter, filterView);

    listView = new StudentListView();
    stuff(R.id.fragment_list_container, listView);

    topView = new IncreaseMemberTopView();
    stuff(R.id.frag_chart, topView);
  }
}
