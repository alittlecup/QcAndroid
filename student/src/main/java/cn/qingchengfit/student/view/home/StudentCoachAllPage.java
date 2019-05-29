package cn.qingchengfit.student.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.databinding.StPageAllStudentBinding;
import cn.qingchengfit.student.routers.StudentParamsInjector;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.Map;

@Leaf(module = "student", path = "/student/coach/all") public class StudentCoachAllPage
    extends StudentAllPage {

  @Need String filterName;
  @Need String filterValue;

  @Override protected void subscribeUI() {
    StudentParamsInjector.inject(this);
    super.subscribeUI();
  }

  @Override public void setFilterView() {
    filterView = new StudentCoachFilterView();
  }

  @Override
  public StPageAllStudentBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = super.initDataBinding(inflater, container, savedInstanceState);
    mBinding.includeAllot.llBottomAllot.setVisibility(View.GONE);
    if (!filterName.isEmpty() && !filterValue.isEmpty()) {
      ((StudentCoachFilterView) filterView).setFilterItem(filterName, filterValue);
      mBinding.fabAddStudent.setVisibility(View.GONE);
    }
    return mBinding;
  }

  @Override public void loadData(Map<String, Object> params) {
    if (filterName.isEmpty() && filterValue.isEmpty()) super.loadData(params);
  }

  @Override public void addStudent() {
    if (permissionModel.check(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS_CAN_WRITE)) {
      QcRouteUtil.setRouteOptions(new RouteOptions("trainer").setActionName("/add/student")).call();
    } else {
      showAlert(R.string.sorry_for_no_permission);
    }
  }

  @Override public void onResume() {
    super.onResume();
  }
}
