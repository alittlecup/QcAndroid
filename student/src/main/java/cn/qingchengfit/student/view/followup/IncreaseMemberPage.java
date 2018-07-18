package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageIncreaseMemberBinding;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

@Leaf(module = "student", path = "/increase/member") public class IncreaseMemberPage
    extends StudentBaseFragment<StPageIncreaseMemberBinding, IncreaseMemberViewModel> {
  MemberIncreaseFilterView filterView;

  StudentListView listView;

  IncreaseMemberTopView topView;

  IncreaseMemberTopViewModel topViewModel;
  IncreaseMemberSortViewModel mSortViewModel;

  @Need @IncreaseType String curType = IncreaseType.INCREASE_FOLLOWUP;

  @Override protected void subscribeUI() {
    mSortViewModel.filterVisible.observe(this, aBoolean -> {
      mBinding.fragmentFilter.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
    });
    mSortViewModel.filterIndex.observe(this, index -> {
      filterView.showPage(index);
    });
    mSortViewModel.filterAction.observe(this, aBoolean -> {
      listView.setItems(
          mSortViewModel.sortFollowTime(mViewModel.getLiveItems().getValue(), aBoolean));
    });
    mSortViewModel.params.observe(this, params -> {
      mViewModel.loadSource(params);
    });

    mViewModel.getLiveItems().observe(this, items -> {
      listView.setItems(items);
    });
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    mSortViewModel = ViewModelProviders.of(this, factory).get(IncreaseMemberSortViewModel.class);
    super.onCreate(savedInstanceState);
  }

  @Override
  public StPageIncreaseMemberBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageIncreaseMemberBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mSortViewModel);
    mBinding.setLifecycleOwner(this);
    initFragment();
    initToolbar();
    initListener();
    return mBinding;
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
    mBinding.rbSelectAll.setOnCheckedChangeListener(
        (buttonView, isChecked) -> listView.selectAll(isChecked));
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    initTopViewModel();
  }

  private void initTopViewModel() {
    if (topViewModel == null) {

      topViewModel = ViewModelProviders.of(topView, factory).get(IncreaseMemberTopViewModel.class);
      topViewModel.getDates().observe(this, params -> {
        mViewModel.loadSourceByDate(params);
      });
    }
  }

  private void initToolbar() {
    toggleToolbar(false, "");
  }

  private void initFragment() {
    if (topView != null) return;

    filterView = new MemberIncreaseFilterView();
    stuff(R.id.fragment_filter, filterView);

    listView = new StudentListView();
    stuff(R.id.fragment_list_container, listView);

    topView = new IncreaseMemberTopView();
    stuff(R.id.frag_chart, topView);

    mBinding.qftFollowTime.setButtonDrawableOff(
        getResources().getDrawable(R.drawable.vd_student_increase_follow_right));
    mBinding.qftFollowTime.setButtonDrawableOn(
        getResources().getDrawable(R.drawable.vd_student_increase_follow_left));
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
      ViewGroup.LayoutParams layoutParams = mBinding.fragChart.getLayoutParams();
      layoutParams.height = 0;
      mBinding.fragChart.setLayoutParams(layoutParams);
      //修改列表内容
      listView.setCurType(type);
    } else {

      //修改toolBar
      mBinding.rbSelectAll.setVisibility(View.GONE);

      topView.setType(curType);
      if (curType.equals(IncreaseType.INCREASE_FOLLOWUP)) {
        ToolbarModel toolbarModel = new ToolbarModel("新用户跟进");
        mViewModel.dataType = 1;
        mBinding.setToolbarModel(toolbarModel);
      } else if (curType.equals(IncreaseType.INCREASE_STUDENT)) {
        ToolbarModel toolbarModel = new ToolbarModel("会员维护");
        mBinding.setToolbarModel(toolbarModel);
        mViewModel.dataType = 2;
      }
      initToolbar(mBinding.includeToolbar.toolbar);

      //底部分配布局
      mBinding.includeAllot.getRoot().setVisibility(View.VISIBLE);

      mSortViewModel.appBarLayoutExpanded.set(true);
      ViewGroup.LayoutParams layoutParams = mBinding.fragChart.getLayoutParams();
      layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
      mBinding.fragChart.setLayoutParams(layoutParams);
      listView.reset();
    }
  }
}
