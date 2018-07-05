package cn.qingchengfit.student.view.home;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageAllStudentBinding;
import cn.qingchengfit.student.inter.DrawerListener;
import cn.qingchengfit.student.inter.LoadDataListener;
import cn.qingchengfit.student.view.allot.StudentAllotPageParams;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.Map;

@Leaf(module = "student", path = "/student/all") public class StudentAllPage
    extends StudentBaseFragment<StPageAllStudentBinding, StudentAllViewModel>
    implements DrawerListener, LoadDataListener {
  StudentRecyclerSortView listView;
  private StudentFilterView filterView;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      listView.setDatas(items);
    });
  }

  @Override
  public StPageAllStudentBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageAllStudentBinding.inflate(inflater, container, false);
    initToolbar();
    initFragment();
    initListener();
    return mBinding;
  }

  private void initListener() {
    mBinding.fabAddStudent.setOnClickListener(v -> {

    });
    mBinding.includeAllot.allotCoach.setOnClickListener(v -> {
      routeTo("/student/allot",new StudentAllotPageParams().curType(StudentListView.TRAINER_TYPE).build());
    });
    mBinding.includeAllot.allotSale.setOnClickListener(v -> {
      routeTo("/student/allot",new StudentAllotPageParams().curType(StudentListView.SELLER_TYPE).build());
    });
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
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public void openDrawer() {
    mBinding.drawer.openDrawer(GravityCompat.END);
  }

  @Override public void closeDrawer() {
    mBinding.drawer.closeDrawer(GravityCompat.END);
  }

  @Override public void loadData(Map<String, ?> params) {
    mViewModel.loadSource(params);
  }
}
