package cn.qingchengfit.student.view.attendance.rank;

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
import cn.qingchengfit.student.databinding.PageAttendanceRankBinding;
import cn.qingchengfit.student.item.AttendanceRankItem;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/11/16.
 */
@Leaf(module = "student", path = "/attendance/rank") public class AttendanceRankPage
    extends StudentBaseFragment<PageAttendanceRankBinding, AttendanceRankViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  private AttendanceRankView rankFilterView;
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      if (items == null || items.isEmpty()) {
        List<CommonNoDataItem> noDataItems = new ArrayList<>();
        noDataItems.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "暂无记录"));
        adapter.updateDataSet(noDataItems);
      } else {
        mViewModel.items.set(items);
      }
    });
    mViewModel.getFilterIndex().observe(this, index -> {
      rankFilterView.showPage(index);
    });
  }

  @Override
  public PageAttendanceRankBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageAttendanceRankBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("出勤统计"));
    mBinding.setViewModel(mViewModel);
    initToolbar(mBinding.includeToolbar.toolbar);
    initFragment();
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mViewModel.loadSource(mViewModel.getLoadData());
    return mBinding;
  }

  private void initFragment() {
    rankFilterView = new AttendanceRankView();
    stuff(R.id.frag_filter, rankFilterView);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof AttendanceRankItem) {
      QcStudentBean qcStudentBean = ((AttendanceRankItem) item).getData().student;
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
