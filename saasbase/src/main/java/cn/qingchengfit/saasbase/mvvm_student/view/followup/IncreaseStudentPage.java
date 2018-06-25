package cn.qingchengfit.saasbase.mvvm_student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PageIncreaseStudentBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.view.allot.StudentAllotPageParams;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentFilterView;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentListView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;

@Leaf(module = "student", path = "/student/increase") public class IncreaseStudentPage
    extends StudentBaseFragment<PageIncreaseStudentBinding, IncreaseStudentViewModel> {
  StudentListView listView;
  IncreaseStudentTopView topView;
  StudentFilterView filterView;
  FollowUpFilterView followUpFilterView;
  IncreaseStudentSortViewModel mSortViewModel;

  @Need @IncreaseType String curType = IncreaseType.INCREASE_MEMBER;

  @Override protected void subscribeUI() {
    mSortViewModel.filterVisible.observe(this, aBoolean -> {
      mBinding.fragmentFilter.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
    });
    mSortViewModel.filterIndex.observe(this, index -> {
      followUpFilterView.showPage(index);
    });
    mSortViewModel.filterAction.observe(this, aVoid -> {
      mBinding.drawer.openDrawer(GravityCompat.END);
    });

    mViewModel.getLiveItems().observe(this, items -> {

    });
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    mSortViewModel = ViewModelProviders.of(this, factory).get(IncreaseStudentSortViewModel.class);
    super.onCreate(savedInstanceState);
  }

  @Override
  public PageIncreaseStudentBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageIncreaseStudentBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mSortViewModel);
    mBinding.fragmentFilter.setVisibility(View.GONE);
    mBinding.setLifecycleOwner(this);
    initFragment();
    initToolbar();
    initListener();
    mViewModel.loadSource(new HashMap<>());
    return mBinding;
  }

  private void initListener() {
    mBinding.includeAllot.allotCoach.setOnClickListener(v -> {
      routeTo("/student/allot",
          new StudentAllotPageParams().items(new ArrayList<>())
              .curType(StudentListView.TRAINER_TYPE)
              .build());
    });
    mBinding.includeAllot.allotSale.setOnClickListener(v -> {
      routeTo("/student/allot",
          new StudentAllotPageParams().items(new ArrayList<>(listView.getSelectDataBeans()))
              .curType(StudentListView.SELLER_TYPE)
              .build());
    });
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel(curType);
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);

    topView.setType(curType);
    if (curType.equals(IncreaseType.INCREASE_MEMBER)) {
      mBinding.qftGender.setVisibility(View.GONE);
      mViewModel.dataType = 0;
    } else if (curType.equals(IncreaseType.INCREASE_STUDENT)) {
      mBinding.qftStatus.setVisibility(View.GONE);
      mViewModel.dataType = 1;
    }
  }

  private void initFragment() {
    topView = new IncreaseStudentTopView();
    listView = new StudentListView();
    filterView = new StudentFilterView();
    followUpFilterView = new FollowUpFilterView();

    stuff(R.id.frag_chart, topView);
    stuff(R.id.fragment_filter, followUpFilterView);
    stuff(R.id.fragment_list_container, listView);
    stuff(R.id.frame_student_filter, filterView);
  }

  @StringDef(value = { IncreaseType.INCREASE_MEMBER, IncreaseType.INCREASE_STUDENT })
  @Retention(RetentionPolicy.RUNTIME) public @interface IncreaseType {
    String INCREASE_MEMBER = "新注册";
    String INCREASE_STUDENT = "会员";
    String INCREASE_FOLLOWUP = "新用户";
  }
}
