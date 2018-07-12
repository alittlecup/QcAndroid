package cn.qingchengfit.student.view.allot;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StSalerStudentsPageBinding;
import cn.qingchengfit.student.listener.DrawerListener;
import cn.qingchengfit.student.listener.LoadDataListener;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.student.view.home.StudentRecyclerSortView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.Map;

@Leaf(module = "student", path = "/student/seller/student") public class SalerStudentsPage
    extends StudentBaseFragment<StSalerStudentsPageBinding, SalerStudentsViewModel>
    implements FlexibleAdapter.OnItemClickListener, DrawerListener, LoadDataListener {
  @Need Staff staff;
  @Need Integer type;
  StudentRecyclerSortView listView;
  StudentFilterView filterView;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      listView.setDatas(items);
    });
  }

  @Override public StSalerStudentsPageBinding initDataBinding(LayoutInflater layoutInflater,
      ViewGroup viewGroup, Bundle bundle) {
    mBinding = StSalerStudentsPageBinding.inflate(layoutInflater, viewGroup, false);
    initToolbar();
    initFragment();
    initListener();

    mViewModel.type = type;
    mViewModel.setSalerId(staff.getId());
    return mBinding;
  }

  private void initListener() {
    mBinding.includeAllot.allotCoach.setOnClickListener(v -> {
      routeTo("/student/allot",
          new StudentAllotPageParams().items(new ArrayList<>(mViewModel.getStudentBeans()))
              .staff(staff)
              .curType(StudentListView.TRAINER_TYPE).build());
    });
    mBinding.includeAllot.allotSale.setOnClickListener(v -> {
      routeTo("/student/allot",
          new StudentAllotPageParams().items(new ArrayList<>(mViewModel.getStudentBeans()))
              .staff(staff)
              .curType(StudentListView.SELLER_TYPE).build());
    });
  }

  private void initFragment() {
    listView = new StudentRecyclerSortView();
    filterView = new StudentFilterView();
    stuff(R.id.frame_student_filter, filterView);
    stuff(R.id.list_container, listView);
    listView.setListener(this);
    listView.setFilterView(filterView);
    listView.setLoadDataListener(this);
  }

  private void initToolbar() {
    String title;
    if (TextUtils.isEmpty(staff.getId())) {
      title = "未分配";
    }else{
      title=(type==0?"销售":"教练")+staff.getUsername();
    }
    mBinding.setToolbarModel(new ToolbarModel(title));
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

  @Override public boolean onItemClick(int position) {
    return false;
  }
}
