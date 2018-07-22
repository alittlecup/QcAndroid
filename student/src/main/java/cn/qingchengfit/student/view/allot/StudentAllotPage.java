package cn.qingchengfit.student.view.allot;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.databinding.PageStudentAllotBinding;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.listener.DrawerListener;
import cn.qingchengfit.student.listener.LoadDataListener;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.student.view.home.StudentRecyclerSortView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.Map;

@Leaf(module = "student", path = "/student/allot") public class StudentAllotPage
    extends StudentBaseFragment<PageStudentAllotBinding, StudentAllotViewModel>
    implements DrawerListener, LoadDataListener {
  StudentRecyclerSortView listView;
  StudentFilterView filterView;
  @Need ArrayList<QcStudentBeanWithFollow> items;
  @Need Staff staff;
  @Need Boolean sortVisible=true;
  @Need @StudentListView.AllotType String curType = StudentListView.SELLER_TYPE;

  @Override protected void subscribeUI() {
    mViewModel.setSalerId(staff == null ? "" : staff.getId());
    mViewModel.getLiveItems().observe(this, items -> {
      listView.setDatas(items);
    });
  }

  @Override
  public PageStudentAllotBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageStudentAllotBinding.inflate(inflater, container, false);
    mViewModel.type = curType.equals(StudentListView.SELLER_TYPE) ? 0 : 1;
    initToolbar();
    initFragment();
    initListener();
    return mBinding;
  }

  private void initListener() {
    mBinding.rbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
      listView.selectAll(isChecked);
    });
  }


  private void initFragment() {
    listView = StudentRecyclerSortView.newInstanceWithType(curType);
    filterView = new StudentFilterView();
    stuff(R.id.frame_student_filter, filterView);
    stuff(R.id.fragment_sort_container, listView);
    listView.setListener(this);
    listView.setFilterView(filterView);
    listView.setLoadDataListener(this);
    listView.setCurId(staff == null ? "" : staff.getId());
    listView.setSortFilterVisible(sortVisible);
    filterView.setListener(params -> {
      mViewModel.loadSource(params);
      mBinding.drawer.closeDrawer(GravityCompat.END);
    });
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel(StudentListView.getStringByType(curType));
    toolbarModel.setMenu(R.menu.menu_cancel);
    toolbarModel.setListener(item -> {
      getActivity().onBackPressed();
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    if (!CompatUtils.less21()
        && mBinding.includeToolbar.toolbar.getParent() instanceof ViewGroup
        && this.isfitSystemPadding()) {
      ((ViewGroup) mBinding.includeToolbar.toolbar.getParent()).setPadding(0,
          MeasureUtils.getStatusBarHeight(this.getContext()), 0, 0);
      RelativeLayout.LayoutParams layoutParams =
          (RelativeLayout.LayoutParams) mBinding.rbSelectAll.getLayoutParams();
      layoutParams.setMargins(0, MeasureUtils.getStatusBarHeight(this.getContext()), 0, 0);
      mBinding.rbSelectAll.setLayoutParams(layoutParams);
    }
  }

  @Override public void openDrawer() {
    mBinding.drawer.openDrawer(GravityCompat.END);
  }

  @Override public void closeDrawer() {
    mBinding.drawer.closeDrawer(GravityCompat.END);
  }

  @Override public void loadData(Map<String, Object> params) {
    if (items == null) {
      mViewModel.loadSource(params);
    } else {
      listView.setDatas(mViewModel.map(items));
    }
  }
}
