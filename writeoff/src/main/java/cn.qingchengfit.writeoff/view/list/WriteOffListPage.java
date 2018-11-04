package cn.qingchengfit.writeoff.view.list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.writeoff.R;
import cn.qingchengfit.writeoff.WriteOffBaseFragment;
import cn.qingchengfit.writeoff.WriteOffItem;
import cn.qingchengfit.writeoff.databinding.WrWriteOffListPageBinding;
import cn.qingchengfit.writeoff.view.detail.WriteOffTicketPage;
import cn.qingchengfit.writeoff.view.detail.WriteOffTicketPageParams;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "writeoff", path = "/ticket/list") public class WriteOffListPage
    extends WriteOffBaseFragment<WrWriteOffListPageBinding, WriteOffListViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  @Inject GymWrapper gymWrapper;
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, writeOffItems -> {
      if (writeOffItems != null) {
        if (adapter != null) {
          adapter.updateDataSet(writeOffItems);
          mBinding.tvWriteOffCount.setText(
              String.format(getResources().getString(R.string.wr_has_ticket_verify),
                  String.valueOf(writeOffItems.size())));
        }
      } else {
        adapter.updateDataSet(noDataItem);
      }
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
  public WrWriteOffListPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding == null) {
      mBinding = WrWriteOffListPageBinding.inflate(inflater, container, false);
      mBinding.setToolbarModel(new ToolbarModel("天猫双十一"));
      initToolbar(mBinding.includeToolbar.toolbar);
      initRecyclerView();
      mBinding.btnWriteOff.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          routeTo("/ticket/verify", null);
        }
      });
      initView();
    }
    mViewModel.loadSource(gymWrapper.getGymId());

    return mBinding;
  }

  private void initView() {
    PhotoUtils.smallCircle(mBinding.imgGymPhoto, gymWrapper.getCoachService().getPhoto());
    mBinding.tvGymName.setText(
        gymWrapper.getBrand().getName() + "-" + gymWrapper.getCoachService().getName());
  }

  private List<CommonNoDataItem> noDataItem = new ArrayList<>();

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    mBinding.recyclerView.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
    noDataItem.clear();
    noDataItem.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "", "暂无数据"));
    adapter.updateDataSet(noDataItem);
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof WriteOffItem) {
      String tickerId = ((WriteOffItem) item).getData().getTickerId();
      routeTo("/ticket/detail", new WriteOffTicketPageParams().ticketId(tickerId).build());
    }
    return false;
  }
}
