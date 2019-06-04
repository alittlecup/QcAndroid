package cn.qingchengfit.student.view.attendance.nosign;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageAttendanceNosignBinding;
import cn.qingchengfit.student.item.NotSignClassItem;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.fragments.ActionSheetDialog;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/11/17.
 */
@Leaf(module = "student", path = "/attendance/nosign") public class AttendanceNosignPage
    extends StudentBaseFragment<PageAttendanceNosignBinding, AttendanceNosignViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  private AttendanceNosignView filterView;
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

    mViewModel.getFilterIndex().observe(this, index -> filterView.showPage(index));

    mViewModel.getLiveItems().observe(this, notSignClassItems -> {
      if (notSignClassItems == null || notSignClassItems.isEmpty()) {
        List<CommonNoDataItem> items = new ArrayList<>();
        items.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无记录"));
      }
      mViewModel.items.set(notSignClassItems);
      mViewModel.textDetail.set(getString(R.string.text_not_sign_tip,
          notSignClassItems == null ? 0 : notSignClassItems.size()));
    });
  }

  @Override
  public PageAttendanceNosignBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageAttendanceNosignBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.setToolbarModel(new ToolbarModel("未签课统计"));
    initToolbar(mBinding.includeToolbar.toolbar);
    initFragment();
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mViewModel.loadSource(mViewModel.getDataHolder());
    return mBinding;
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

  private void initFragment() {
    filterView = new AttendanceNosignView();
    stuff(R.id.frag_filter, filterView);
  }

  @Override public boolean onItemClick(int position) {
    Boolean connect = (Boolean) adapter.getTag("connect");
    NotSignClassItem item = (NotSignClassItem) adapter.getItem(position);
    if (connect != null && connect) {
      showBottomSheet(item.getData().getPhone());
      return false;
    } else {
      QcStudentBean qcStudentBean = ((NotSignClassItem) item).getData();
      if (AppUtils.getCurAppSchema(getContext()).equals("qccoach")) {
        toTrainStudentInfo(qcStudentBean.id, qcStudentBean.getShipId());
        return false;
      }
      QcRouteUtil.setRouteOptions(new RouteOptions("staff").setActionName("/home/student")
          .addParam("qcstudent", qcStudentBean)).call();
    }

    return false;
  }
}
