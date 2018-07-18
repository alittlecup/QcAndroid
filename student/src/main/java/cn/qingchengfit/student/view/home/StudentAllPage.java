package cn.qingchengfit.student.view.home;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageAllStudentBinding;
import cn.qingchengfit.student.listener.DrawerListener;
import cn.qingchengfit.student.listener.LoadDataListener;
import cn.qingchengfit.student.view.allot.StudentAllotPageParams;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.Map;

@Leaf(module = "student", path = "/student/all") public class StudentAllPage
    extends StudentBaseFragment<StPageAllStudentBinding, StudentAllViewModel>
    implements DrawerListener, LoadDataListener, SearchView.OnQueryTextListener
    {
  StudentRecyclerSortView listView;
  private StudentFilterView filterView;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      listView.setDatas(items);
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
    toggleToolbar(false,"");
    return mBinding;
  }

  private void initListener() {
    mBinding.fabAddStudent.setOnClickListener(v -> {

    });
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

  private void initFragment() {
    listView = new StudentRecyclerSortView();
    stuff(R.id.list_container, listView);
    filterView = new StudentFilterView();
    stuff(R.id.frame_student_filter, filterView);
    listView.setFilterView(filterView);
    listView.setListener(this);
    listView.setLoadDataListener(this);
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("全部会员");
    toolbarModel.setMenu(R.menu.menu_search);
    toolbarModel.setListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        SearchView actionView = (SearchView) item.getActionView();
        actionView.setQueryHint("输入学员姓名或者手机号");
        actionView.setOnQueryTextListener(StudentAllPage.this);
        actionView.setOnQueryTextFocusChangeListener(
            (v, hasFocus) -> mBinding.includeToolbar.toolbarTitle.setVisibility(hasFocus ? View.GONE : View.VISIBLE));
        return false;
      }
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
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
  private void toggleToolbar(boolean show,String type){
    if(show){
      //修改toolBar
      mBinding.rbSelectAll.setVisibility(View.VISIBLE);
      mBinding.fabAddStudent.setVisibility(View.GONE);
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
    }else{
      mBinding.rbSelectAll.setVisibility(View.GONE);
      mBinding.fabAddStudent.setVisibility(View.VISIBLE);
      initToolbar();
      if(listView.getListView()!=null){
        listView.getListView().reset();
      }
      mBinding.includeAllot.getRoot().setVisibility(View.VISIBLE);

    }
  }

}
