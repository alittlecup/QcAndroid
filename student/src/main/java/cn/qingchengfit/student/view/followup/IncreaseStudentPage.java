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
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageIncreaseStudentBinding;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.listener.onRightFilterCloseListener;
import cn.qingchengfit.student.view.allot.StudentAllotPageParams;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.view.home.StudentFilterViewModel;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.statuslayout.StatusLayoutManager;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

@Leaf(module = "student", path = "/student/increase") public class IncreaseStudentPage
    extends StudentBaseFragment<StPageIncreaseStudentBinding, IncreaseStudentViewModel> {
  StudentListView listView;
  IncreaseStudentTopView topView;
  StudentFilterView filterView;
  FollowUpFilterView followUpFilterView;
  IncreaseStudentSortViewModel mSortViewModel;
  IncreaseStudentTopViewModel studentTopViewModel;
  @Inject IPermissionModel permissionModel;

  @Need @IncreaseType String curType = IncreaseType.INCREASE_MEMBER;

  @Override protected void subscribeUI() {
    mSortViewModel.filterVisible.observe(this, aBoolean -> {
      mBinding.fragmentFilter.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
    });
    mSortViewModel.filterIndex.observe(this, index -> {
      followUpFilterView.showPage(index);
      mBinding.layoutCollapsed.setExpanded(false);

    });
    mSortViewModel.filterAction.observe(this, aVoid -> {
      mBinding.drawer.openDrawer(GravityCompat.END);
    });
    mSortViewModel.params.observe(this,params->{
      mViewModel.loadSourceByStatus(params);
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
    mBinding.setViewModel(mSortViewModel);
    mBinding.fragmentFilter.setVisibility(View.GONE);
    mBinding.setLifecycleOwner(this);

    initFragment();
    initToolbar();
    initListener();

    return mBinding;
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    loadSource();
  }

  private void loadSource() {
    if(studentTopViewModel==null){
      studentTopViewModel = ViewModelProviders.of(topView, factory).get(IncreaseStudentTopViewModel.class);
      studentTopViewModel.curSelectPositionDate.observe(this, params -> {

        mViewModel.loadSoutceByDate(params);
      });
    }

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
    mBinding.rbSelectAll.setOnCheckedChangeListener((view,checked)->listView.selectAll(checked));
  }

  private void initToolbar() {
    toggleToolbar(false, "");

    topView.setType(curType);
    if (curType.equals(IncreaseType.INCREASE_MEMBER)) {
      mBinding.qftGender.setVisibility(View.GONE);
      mViewModel.dataType = 0;
    } else if (curType.equals(IncreaseType.INCREASE_STUDENT)) {
      mBinding.qftStatus.setVisibility(View.GONE);
      mViewModel.dataType = 2;
    }
  }

  private void initFragment() {
    if(topView!=null)return;
    topView = new IncreaseStudentTopView();
    listView = new StudentListView();
    filterView = new StudentFilterView();
    filterView.setFilterTimeVisible(false);
    followUpFilterView = new FollowUpFilterView();

    stuff(R.id.frag_chart, topView);
    stuff(R.id.fragment_filter, followUpFilterView);
    stuff(R.id.fragment_list_container, listView);
    stuff(R.id.frame_student_filter, filterView);

    filterView.setListener(params -> {
      mViewModel.loadSourceRight(params);
      mBinding.drawer.closeDrawer(GravityCompat.END);
    });
  }

  private void toggleToolbar(boolean showCheckBox, String type) {
    if (showCheckBox) {
      if(!permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)){
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
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
      ViewGroup.LayoutParams layoutParams = mBinding.fragChart.getLayoutParams();
      layoutParams.height=0;
      mBinding.fragChart.setLayoutParams(layoutParams);
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
      ViewGroup.LayoutParams layoutParams = mBinding.fragChart.getLayoutParams();
      layoutParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;
      mBinding.fragChart.setLayoutParams(layoutParams);
      listView.reset();
    }
  }
}
