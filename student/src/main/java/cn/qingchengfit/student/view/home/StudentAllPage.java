package cn.qingchengfit.student.view.home;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageAllStudentBinding;
import cn.qingchengfit.student.listener.DrawerListener;
import cn.qingchengfit.student.listener.LoadDataListener;
import cn.qingchengfit.student.view.allot.StudentAllotPageParams;
import cn.qingchengfit.student.widget.SearchView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.Map;
import javax.inject.Inject;

@Leaf(module = "student", path = "/student/all") public class StudentAllPage
    extends StudentBaseFragment<StPageAllStudentBinding, StudentAllViewModel>
    implements DrawerListener, LoadDataListener, SearchView.OnQueryTextListener {
  StudentRecyclerSortView listView;
  private StudentFilterView filterView;
  @Inject IPermissionModel permissionModel;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      listView.setDatas(items);
      listView.getListView().setAdapterTag("choose", -1);
    });
    mViewModel.showLoading.observe(this, aBoolean -> {
      if (aBoolean) {
        showLoading();
      } else {
        hideLoading();
      }
    });
  }

  @Override
  public StPageAllStudentBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageAllStudentBinding.inflate(inflater, container, false);
    initFragment();
    initListener();
    toggleToolbar(false, "");
    return mBinding;
  }

  private void initListener() {

    mBinding.includeAllot.allotCoach.setOnClickListener(v -> {
      if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
        toggleToolbar(true, StudentListView.TRAINER_TYPE);
      } else {
        showAlert(R.string.sorry_for_no_permission);
      }
    });
    mBinding.includeAllot.allotSale.setOnClickListener(v -> {
      if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
        toggleToolbar(true, StudentListView.SELLER_TYPE);
      } else {
        showAlert(R.string.sorry_for_no_permission);
      }
    });
    mBinding.includeAllot.allotMsg.setOnClickListener(v -> {
      toggleToolbar(true, StudentListView.MSG_TYPE);
    });
    mBinding.rbSelectAll.setOnCheckedChangeListener(
        (buttonView, isChecked) -> listView.selectAll(isChecked));
  }

  private void initFragment() {
    if(listView!=null)return;
    listView = new StudentRecyclerSortView();
    stuff(R.id.list_container, listView);
    filterView = new StudentFilterView();
    stuff(R.id.frame_student_filter, filterView);
    listView.setFilterView(filterView);
    listView.setListener(this);
    listView.setLoadDataListener(this);

    filterView.setFilterShowAll(true);
    filterView.setListener(params -> {
      mViewModel.loadSource(params);
      mBinding.drawer.closeDrawer(GravityCompat.END);
    });
  }

  @Override public void onResume() {
    super.onResume();
    setBackPress();
  }

  @Override public void onPause() {
    super.onPause();
    setBackPressNull();
  }

  @Override public boolean onFragmentBackPress() {
    if (actionView!=null&&actionView.isExpanded()) {
      mBinding.includeToolbar.toolbar.collapseActionView();
      return true;
    } else {
      return super.onFragmentBackPress();
    }
  }

  Toolbar toolbar;
  SearchView actionView;
  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("全部会员");
    toolbarModel.setMenu(R.menu.menu_search_shop);
    toolbarModel.setListener(item -> {
      actionView = (SearchView) item.getActionView();
      actionView.setQueryHint("输入学员姓名或者手机号");
      actionView.setOnQueryTextListener(StudentAllPage.this);
      actionView.setOnActionViewCollapsed(new SearchView.onActionViewListener() {
        @Override public void onActionViewCollapsed() {
          mBinding.includeToolbar.toolbarTitle.setVisibility(View.VISIBLE);
        }
        @Override public void onActionViewExpanded() {
          mBinding.includeToolbar.toolbarTitle.setVisibility(View.GONE);
        }
      });
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    toolbar=mBinding.includeToolbar.toolbar;
    initToolbar(toolbar);
  }

  @Override public void openDrawer() {
    mBinding.drawer.openDrawer(GravityCompat.END);
  }

  @Override public void closeDrawer() {
    mBinding.drawer.closeDrawer(GravityCompat.END);
  }

  @Override public void loadData(Map<String, Object> params) {
    mViewModel.loadSource(params);
  }

  @Override public boolean onQueryTextSubmit(String query) {

    return false;
  }

  @Override public boolean onQueryTextChange(String newText) {
    listView.filter(newText);
    return false;
  }

  private void toggleToolbar(boolean show, String type) {
    if (show) {
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
      //修改列表内容
      listView.getListView().setCurType(type);
    } else {
      mBinding.rbSelectAll.setVisibility(View.GONE);
      initToolbar();
      if (listView.getListView() != null) {
        listView.getListView().reset();
      }
      mBinding.includeAllot.getRoot().setVisibility(View.VISIBLE);
    }
  }
}
