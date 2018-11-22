package cn.qingchengfit.staffkit.dianping.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.staff.routers.StaffParamsInjector;
import cn.qingchengfit.staffkit.databinding.PageDianpingChooseBinding;
import cn.qingchengfit.staffkit.dianping.item.SimpleChooseDataItem;
import cn.qingchengfit.staffkit.dianping.vo.DianPingChooseDataEvent;
import cn.qingchengfit.staffkit.dianping.vo.DianPingChooseType;
import cn.qingchengfit.staffkit.dianping.vo.ISimpleChooseData;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.google.android.flexbox.FlexboxItemDecoration;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "dianping", path = "/dianping/choose") public class DianPingChoosePage
    extends SaasBindingFragment<PageDianpingChooseBinding, DianPingChooseViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  CommonFlexAdapter adapter;
  @Need @DianPingChooseType Integer type = DianPingChooseType.CHOOSE_FACILITY;
  @Need ArrayList<ISimpleChooseData> selectedDatas;

  @Override protected void subscribeUI() {
    mViewModel.chooseDatas.observe(this, datas -> {
      if (datas == null || datas.isEmpty()) {

      } else {
        List<SimpleChooseDataItem> items = new ArrayList<>();
        for (ISimpleChooseData data : datas) {
          items.add(new SimpleChooseDataItem(data));
          if (selectedDatas != null && selectedDatas.contains(data)) {
            adapter.addSelection(items.size() - 1);
          }
        }

        adapter.updateDataSet(items);
      }
    });
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    StaffParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Override
  public PageDianpingChooseBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageDianpingChooseBinding.inflate(inflater, container, false);
    initToolBar();
    initRecyclerView();
    mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        setSelectedDataBack();
      }
    });
    return mBinding;
  }

  private void setSelectedDataBack() {
    List<Integer> selectedPositions = adapter.getSelectedPositions();
    List<ISimpleChooseData> datas = new ArrayList<>();
    if (selectedPositions != null && !selectedPositions.isEmpty()) {
      for (Integer index : selectedPositions) {
        datas.add(((SimpleChooseDataItem) adapter.getItem(index)).getData());
      }
    }
    RxBus.getBus().post(new DianPingChooseDataEvent(datas, type));
    getActivity().onBackPressed();
  }

  private void initToolBar() {
    mBinding.setToolbarModel(
        new ToolbarModel(type == DianPingChooseType.CHOOSE_TAGS ? "选择场馆标签" : "选择场馆设施"));
    initToolbar(mBinding.includeToolbar.toolbar);
    if (type == DianPingChooseType.CHOOSE_TAGS) {
      mViewModel.loadGymTags();
    } else {
      mViewModel.loadGymFacilities();
    }
  }

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
  }

  @Override public boolean onItemClick(int position) {
    if (type == DianPingChooseType.CHOOSE_TAGS
        && adapter.getSelectedItemCount() >= 5
        && !adapter.isSelected(position)) {
      ToastUtils.show("最多可选五项");
      return false;
    }
    adapter.toggleSelection(position);
    adapter.notifyItemChanged(position);
    return false;
  }
}
