package cn.qingchengfit.student.view.attendance.absent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageAttendanceAbsentBinding;
import cn.qingchengfit.student.item.AttendanceStudentItem;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.ActionSheetDialog;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/11/16.
 */
@Leaf(module = "student", path = "/attendance/absent") public class AttendanceAbsentPage
    extends StudentBaseFragment<PageAttendanceAbsentBinding, AttendanceAbsentViewModel>
    implements FlexibleAdapter.OnItemClickListener {

  private AttendanceAbsentView filterFragment;
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, listitem -> {
      mViewModel.items.set(listitem);
    });
    mViewModel.getFilterIndex().observe(this, index -> {
      filterFragment.showPage(index);
    });
  }

  @Override
  public PageAttendanceAbsentBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageAttendanceAbsentBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("缺勤统计"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.setViewModel(mViewModel);
    initFragment();
    mViewModel.loadSource(new Pair<>("7", "30"));
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter.addListener(this);
    return mBinding;
  }

  private void initFragment() {
    filterFragment = new AttendanceAbsentView();
    stuff(R.id.frag_filter, filterFragment);
  }

  private void showBottomSheet(String phone) {
    List<CharSequence> contents = new ArrayList<>();
    contents.add("呼叫" + phone);
    contents.add("发短信");
    ActionSheetDialog dialog = new ActionSheetDialog(getContext(), "联系会员", contents);
    dialog.show();
    dialog.setOnItemClickListener(position -> {
      if (position == 0) {
        AppUtils.doCallPhoneTo(getContext(), phone);
      } else {
        AppUtils.doSendSMSTo(getContext(), phone);
      }
      return false;
    });
  }

  @Override public boolean onItemClick(int position) {
    Boolean contact = (Boolean) adapter.getTag("contact");
    AttendanceStudentItem item = (AttendanceStudentItem) adapter.getItem(position);
    if (contact != null && contact) {
      showBottomSheet(item.getData().user.phone);
    } else {
      QcStudentBean user = item.getData().user;
      QcRouteUtil.setRouteOptions(new RouteOptions("staff").setActionName("/home/student")
          .addParam("qcstudent", user)).call();
    }
    return false;
  }
}
