package cn.qingchengfit.writeoff.view.list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.writeoff.R;
import cn.qingchengfit.writeoff.WriteOffBaseFragment;
import cn.qingchengfit.writeoff.WriteOffItem;
import cn.qingchengfit.writeoff.databinding.WrWriteOffListPageBinding;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
@Leaf(module = "writeoff",path = "/ticket/list")
public class WriteOffListPage
    extends WriteOffBaseFragment<WrWriteOffListPageBinding, WriteOffListViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  @Inject GymWrapper gymWrapper;
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, writeOffItems -> {
      if (writeOffItems != null) {
        if (adapter != null) {
          adapter.updateDataSet(writeOffItems);
        }
      } else {
        adapter.updateDataSet(noDataItem);
      }
    });
  }

  @Override
  public WrWriteOffListPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = WrWriteOffListPageBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("天猫双十一"));
    initToolbar(mBinding.includeToolbar.toolbar);
    initRecyclerView();
    mViewModel.loadSource(gymWrapper.getGymId());
    mBinding.btnWriteOff.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeTo("/ticket/verify",null);
      }
    });
    return mBinding;
  }

  private List<CommonNoDataItem> noDataItem = new ArrayList<>();

  private void initRecyclerView() {
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    noDataItem.clear();
    noDataItem.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe, "", "暂无数据"));
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item instanceof WriteOffItem) {
      String tickerId = ((WriteOffItem) item).getData().getTickerId();
      routeTo("/ticket/detail/",null);
    } return false;
  }
}
