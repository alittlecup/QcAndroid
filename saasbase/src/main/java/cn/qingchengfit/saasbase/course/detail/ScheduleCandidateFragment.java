package cn.qingchengfit.saasbase.course.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.FragmentScheduleOrdersBinding;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "course", path = "/schedule/candidate") public class ScheduleCandidateFragment
    extends SaasBindingFragment<FragmentScheduleOrdersBinding, ScheduleDetailVM> {
  @Need String scheduleID;
  CommonFlexAdapter adapter;
  boolean openCandidate;

  @Override

  protected void subscribeUI() {
    mViewModel = ViewModelProviders.of(getParentFragment()).get(ScheduleDetailVM.class);
    mViewModel.detailCandidate.observe(this, this::updateItems);
    mViewModel.signOpen.observe(this, aBoolean -> {
      this.openCandidate = aBoolean;
    });
  }

  int count = 0;

  private void updateItems(ScheduleCandidates scheduleCandidate) {
    List<AbstractFlexibleItem> items = new ArrayList<>();

    if (scheduleCandidate == null
        || scheduleCandidate.candidates == null
        || scheduleCandidate.candidates.isEmpty()) {
      if (!openCandidate) {
        items.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "未开启空位订阅"));
      } else {
        items.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "目前没有空位订阅"));
      }
    } else {
      count = 0;
      List<ScheduleCandidate> candidates = scheduleCandidate.candidates;
      for (ScheduleCandidate candidate : candidates) {
        items.add(new ScheduleCandidateItem(candidate, scheduleCandidate.schedule.end));
        if (candidate.getStatus() != 2) {
          count++;
        }
      }
    }
    adapter.updateDataSet(items);
    setTitle();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Override
  public FragmentScheduleOrdersBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = FragmentScheduleOrdersBinding.inflate(inflater, container, false);
    initRecyclerView();
    loadCandidateConfig();
    mViewModel.loadScheduleCandidate(scheduleID);
    return mBinding;
  }

  private void loadCandidateConfig() {
    mViewModel.loadCourseConfig("team_course_can_wait");
  }

  private void setTitle() {
    StringBuilder stringBuilder = new StringBuilder("空位订阅");
    if (count != 0) {
      stringBuilder.append("(").append(count).append(")");
    }
    mViewModel.orderDetailSecondTab.setValue(stringBuilder.toString());
  }

  private void initRecyclerView() {
    mBinding.recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerOrders.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    mBinding.recyclerOrders.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
  }
}
