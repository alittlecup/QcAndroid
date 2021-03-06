package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.qingchengfit.student.databinding.StPageIncreaseStudentBinding;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.listener.onRightFilterCloseListener;
import cn.qingchengfit.student.view.allot.StudentAllotPageParams;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.view.home.StudentFilterViewModel;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
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
  @Inject LoginStatus loginStatus;

  @Need @IncreaseType String curType = IncreaseType.INCREASE_MEMBER;

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override protected void subscribeUI() {
    mSortViewModel.filterVisible.observe(this, aBoolean -> {
      if (aBoolean) {
        mBinding.fragmentFilter.setVisibility(View.VISIBLE);
        mBinding.fragmentFilter.setAlpha(0f);
        mBinding.fragmentFilter.animate().alpha(1).setDuration(20).start();
      } else {
        mBinding.fragmentFilter.animate().alpha(0).setDuration(20).withEndAction(new Runnable() {
          @Override public void run() {
            mBinding.fragmentFilter.setVisibility(View.GONE);
          }
        }).start();
      }
    });
    mSortViewModel.filterIndex.observe(this, index -> {
      followUpFilterView.showPage(index);
      mBinding.layoutCollapsed.setExpanded(false);
    });
    mSortViewModel.filterAction.observe(this, aVoid -> {
      mBinding.drawer.openDrawer(GravityCompat.END);
    });
    mSortViewModel.params.observe(this, params -> {
      mViewModel.loadSourceByStatus(params);
    });
    mSortViewModel.salerName.observe(this, name -> {
      if ("全部销售".equals(name)) {
        mBinding.qftSaler.setTextColorRes(R.color.text_black);
        mBinding.qftSaler.setButtonDrawableOff(
            getResources().getDrawable(R.drawable.vd_filter_arrow_down));
        mBinding.qftSaler.setButtonDrawableOn(
            getResources().getDrawable(R.drawable.vd_filter_arrow_up));
      } else {
        mBinding.qftSaler.setTextColorRes(R.color.colorPrimary);
        mBinding.qftSaler.setButtonDrawableOn(
            DrawableUtils.tintDrawable(getContext(), R.drawable.vd_filter_arrow_up,
                R.color.colorPrimary));
        mBinding.qftSaler.setButtonDrawableOff(
            DrawableUtils.tintDrawable(getContext(), R.drawable.vd_filter_arrow_down,
                R.color.colorPrimary));
      }
    });
    mSortViewModel.gender.observe(this, gender -> {
      if ("性别".equals(gender)) {
        mBinding.qftGender.setTextColorRes(R.color.text_black);
        mBinding.qftGender.setButtonDrawableOff(
            getResources().getDrawable(R.drawable.vd_filter_arrow_down));
        mBinding.qftGender.setButtonDrawableOn(
            getResources().getDrawable(R.drawable.vd_filter_arrow_up));
      } else {
        mBinding.qftGender.setTextColorRes(R.color.colorPrimary);

        mBinding.qftGender.setButtonDrawableOn(
            DrawableUtils.tintDrawable(getContext(), R.drawable.vd_filter_arrow_up,
                R.color.colorPrimary));
        mBinding.qftGender.setButtonDrawableOff(
            DrawableUtils.tintDrawable(getContext(), R.drawable.vd_filter_arrow_down,
                R.color.colorPrimary));
      }
    });
    mSortViewModel.studentStatus.observe(this, status -> {
      if ("会员状态".equals(status)) {
        mBinding.qftStatus.setTextColorRes(R.color.text_black);
        mBinding.qftStatus.setButtonDrawableOff(
            getResources().getDrawable(R.drawable.vd_filter_arrow_down));
        mBinding.qftStatus.setButtonDrawableOn(
            getResources().getDrawable(R.drawable.vd_filter_arrow_up));
      } else {
        mBinding.qftStatus.setTextColorRes(R.color.colorPrimary);

        mBinding.qftStatus.setButtonDrawableOn(
            DrawableUtils.tintDrawable(getContext(), R.drawable.vd_filter_arrow_up,
                R.color.colorPrimary));
        mBinding.qftStatus.setButtonDrawableOff(
            DrawableUtils.tintDrawable(getContext(), R.drawable.vd_filter_arrow_down,
                R.color.colorPrimary));
      }
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

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    loadSource();
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
      if(TextUtils.isEmpty(s)){
        s=PreferenceUtils.getPrefString(getContext(),Configs.PREFER_WORK_NAME_MIRROR,"");
      }
      mSortViewModel.salerName.setValue(s);
    }

  }

  private void loadSource() {
    if (studentTopViewModel == null) {
      studentTopViewModel =
          ViewModelProviders.of(topView, factory).get(IncreaseStudentTopViewModel.class);
      studentTopViewModel.curSelectPositionDate.observe(this, params -> {

        mViewModel.loadSoutceByDate(params);
      });
    }
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
        (view, checked) -> listView.selectAll(checked, view));
    if (!permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
      mBinding.qftSaler.setEnabled(false);
      mBinding.qftSaler.setText(loginStatus.getLoginUser().getUsername());
    }
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
    if (topView != null) return;
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
      String title = "";
      if (curType.equals(IncreaseType.INCREASE_MEMBER)) {
        title = "新注册用户";
      } else if (curType.equals(IncreaseType.INCREASE_STUDENT)) {
        title = "新购卡会员";
        filterView.setFilterStatusIds(false);
      }
      ToolbarModel toolbarModel = new ToolbarModel(title);
      mBinding.setToolbarModel(toolbarModel);
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
