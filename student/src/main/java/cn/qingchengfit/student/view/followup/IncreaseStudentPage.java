package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageIncreaseStudentBinding;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.view.allot.StudentAllotPageParams;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.statuslayout.StatusLayoutManager;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.HashMap;

@Leaf(module = "student", path = "/student/increase") public class IncreaseStudentPage
    extends StudentBaseFragment<StPageIncreaseStudentBinding, IncreaseStudentViewModel> {
  StudentListView listView;
  IncreaseStudentTopView topView;
  StudentFilterView filterView;
  FollowUpFilterView followUpFilterView;
  IncreaseStudentSortViewModel mSortViewModel;
  IncreaseStudentTopViewModel studentTopViewModel;

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
      listView.setItems(items);
    });
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    mSortViewModel = ViewModelProviders.of(this, factory).get(IncreaseStudentSortViewModel.class);
    super.onCreate(savedInstanceState);
  }

  @Override
  public StPageIncreaseStudentBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageIncreaseStudentBinding.inflate(inflater, container, false);
    initFragment();
    mBinding.setViewModel(mSortViewModel);
    mBinding.fragmentFilter.setVisibility(View.GONE);
    mBinding.setLifecycleOwner(this);
    initToolbar();
    initListener();
    return mBinding;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    loadSource();
  }

  private void loadSource() {
    studentTopViewModel =
        ViewModelProviders.of(topView, factory).get(IncreaseStudentTopViewModel.class);

    studentTopViewModel.curSelectPositionDate.observe(this, params -> {
      mViewModel.loadSource(params);
    });
  }

  private void initListener() {
    mBinding.includeAllot.allotCoach.setOnClickListener(v -> {
      toggleToolbar(true, StudentListView.TRAINER_TYPE);
    });
    mBinding.includeAllot.allotSale.setOnClickListener(v -> {
      toggleToolbar(true, StudentListView.SELLER_TYPE);
    });
    mBinding.includeAllot.allotMsg.setOnClickListener(v -> {
      toggleToolbar(true, StudentListView.MSG_TYPE);
    });
  }

  private void initToolbar() {
    toggleToolbar(false, "");

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

  private void toggleToolbar(boolean showCheckBox, String type) {
    if (showCheckBox) {
      //修改toolBar
      mBinding.rbSelectAll.setVisibility(View.VISIBLE);
      ToolbarModel toolbarModel = new ToolbarModel(StudentListView.getStringByType(type));
      toolbarModel.setMenu(R.menu.menu_cancel);
      toolbarModel.setListener(item -> {
        toggleToolbar(false, "");
        return false;
      });
      mBinding.setToolbarModel(toolbarModel);
      if (!CompatUtils.less21()
          && mBinding.includeToolbar.toolbar.getParent() instanceof ViewGroup
          && this.isfitSystemPadding()) {
        RelativeLayout.LayoutParams layoutParams =
            (RelativeLayout.LayoutParams) mBinding.rbSelectAll.getLayoutParams();
        layoutParams.setMargins(0, MeasureUtils.getStatusBarHeight(this.getContext()), 0, 0);
        mBinding.rbSelectAll.setLayoutParams(layoutParams);
      }
      //底部分配布局
      mBinding.includeAllot.getRoot().setVisibility(View.GONE);
      //收缩布局
      mSortViewModel.appBarLayoutExpanded.set(false);

      //修改列表内容
      listView.setCurType(type);
    } else {

      //修改toolBar
      mBinding.rbSelectAll.setVisibility(View.GONE);
      ToolbarModel toolbarModel = new ToolbarModel(curType);
      mBinding.setToolbarModel(toolbarModel);
      initToolbar(mBinding.includeToolbar.toolbar);

      //底部分配布局
      mBinding.includeAllot.getRoot().setVisibility(View.VISIBLE);

      mSortViewModel.appBarLayoutExpanded.set(true);

      listView.reset();
    }
  }
}
