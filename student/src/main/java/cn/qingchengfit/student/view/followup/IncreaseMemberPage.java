package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.StudentListSelectEvent;
import cn.qingchengfit.student.databinding.StPageIncreaseMemberBinding;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import javax.inject.Inject;

@Leaf(module = "student", path = "/increase/member") public class IncreaseMemberPage
    extends StudentBaseFragment<StPageIncreaseMemberBinding, IncreaseMemberViewModel> {
  MemberIncreaseFilterView filterView;

  StudentListView listView;

  IncreaseMemberTopView topView;

  IncreaseMemberTopViewModel topViewModel;
  IncreaseMemberSortViewModel mSortViewModel;
  @Inject IPermissionModel permissionModel;
  @Inject LoginStatus loginStatus;

  @Need @IncreaseType String curType = IncreaseType.INCREASE_FOLLOWUP;

  @Override protected void subscribeUI() {
    mSortViewModel.filterVisible.observe(this, aBoolean -> {
      if(aBoolean){
        mBinding.fragmentFilter.setVisibility(View.VISIBLE);
        mBinding.fragmentFilter.setAlpha(0f);
        mBinding.fragmentFilter.animate().alpha(1).setDuration(20).start();
      }else{
        mBinding.fragmentFilter.animate().alpha(0).setDuration(20).withEndAction(new Runnable() {
          @Override public void run() {
            mBinding.fragmentFilter.setVisibility(View.GONE);
          }
        }).start();
      }
    });
    mSortViewModel.filterIndex.observe(this, index -> {
      filterView.showPage(index);
      mBinding.layoutCollapsed.setExpanded(false);
    });
    mSortViewModel.filterAction.observe(this, aBoolean -> {
      listView.setItems(
          mSortViewModel.sortFollowTime(mViewModel.getLiveItems().getValue(), aBoolean));
    });
    mSortViewModel.params.observe(this, params -> {
      mViewModel.loadSource(params);
    });

    mSortViewModel.salerName.observe(this,name->{
      if("全部销售".equals(name)){
        mBinding.qftSaler.setTextColorRes(R.color.text_black);
        mBinding.qftStatus.setButtonDrawableOff(getResources().getDrawable(R.drawable.vd_filter_arrow_down));
        mBinding.qftStatus.setButtonDrawableOn(getResources().getDrawable(R.drawable.vd_filter_arrow_up));
      }else{
        mBinding.qftSaler.setTextColorRes(R.color.colorPrimary);
        mBinding.qftSaler.setButtonDrawableOn(DrawableUtils.tintDrawable(getContext(),R.drawable.vd_filter_arrow_up,R.color.colorPrimary));
        mBinding.qftSaler.setButtonDrawableOff(DrawableUtils.tintDrawable(getContext(),R.drawable.vd_filter_arrow_down,R.color.colorPrimary));

      }
    });
    mSortViewModel.followUpStatus.observe(this,status->{
      if("跟进状态".equals(status)){
        mBinding.qftStatus.setTextColorRes(R.color.text_black);
        mBinding.qftStatus.setButtonDrawableOff(getResources().getDrawable(R.drawable.vd_filter_arrow_down));
        mBinding.qftStatus.setButtonDrawableOn(getResources().getDrawable(R.drawable.vd_filter_arrow_up));
      }else{
        mBinding.qftStatus.setTextColorRes(R.color.colorPrimary);
        mBinding.qftStatus.setButtonDrawableOn(DrawableUtils.tintDrawable(getContext(),R.drawable.vd_filter_arrow_up,R.color.colorPrimary));
        mBinding.qftStatus.setButtonDrawableOff(DrawableUtils.tintDrawable(getContext(),R.drawable.vd_filter_arrow_down,R.color.colorPrimary));
      }
    });

    mViewModel.getLiveItems().observe(this, items -> {
      listView.setItems(items);
    });
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    mSortViewModel = ViewModelProviders.of(this, factory).get(IncreaseMemberSortViewModel.class);
    super.onCreate(savedInstanceState);
  }
  private void initAppbarLayout() {
    ViewGroup.LayoutParams layoutParams = mBinding.layoutCollapsed.getLayoutParams();
    if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
      AppBarLayout.Behavior behavior =
          (AppBarLayout.Behavior) ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
      if (behavior != null) {
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
          @Override public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
            return mBinding.fragmentFilter.getVisibility() != View.VISIBLE;
          }
        });
      }
    }
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
      if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
        toggleToolbar(true, StudentListView.TRAINER_TYPE);
      } else {
        showAlert(R.string.sorry_for_no_permission);
      }
    });
    mBinding.includeAllot.allotSale.setOnClickListener(v -> {
      if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
        toggleToolbar(true, StudentListView.SELLER_TYPE);
      } else {
        showAlert(R.string.sorry_for_no_permission);
      }
    });
    mBinding.includeAllot.allotMsg.setOnClickListener(v -> {
      toggleToolbar(true, StudentListView.MSG_TYPE);
    });
    mBinding.rbSelectAll.setOnCheckedChangeListener(
        (buttonView, isChecked) -> listView.selectAll(isChecked,buttonView));
    if (!permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
      mBinding.qftSaler.setEnabled(false);
      mBinding.qftSaler.setText(loginStatus.getLoginUser().getUsername());
    }
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    initTopViewModel();
    initAppbarLayout();
    RxRegiste(RxBus.getBus()
        .register(StudentListSelectEvent.class)
        .subscribe(event -> {
          mBinding.rbSelectAll.setChecked(event.isSelected());
        }));
    if(!permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)){
      mBinding.qftSaler.setClickable(false);
      String s = loginStatus.staff_name();
      if(TextUtils.isEmpty(s)){
        s=PreferenceUtils.getPrefString(getContext(),Configs.PREFER_WORK_NAME,"");
      }
      mSortViewModel.salerName.setValue(s);
    }
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
      if (!permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
      //修改toolBar
      mBinding.rbSelectAll.setVisibility(View.VISIBLE);
      ToolbarModel toolbarModel = new ToolbarModel(StudentListView.getStringByType(type));
      toolbarModel.setMenu(R.menu.menu_cancel);
      toolbarModel.setListener(item -> {
        toggleToolbar(false, "");
        mBinding.rbSelectAll.setChecked(false);

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
        ToolbarModel toolbarModel = new ToolbarModel("跟进新用户");
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
