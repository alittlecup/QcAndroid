package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.databinding.TurnoverGirdSellerFragmentBinding;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import java.util.ArrayList;

public class TurnoverGirdSellerFilterFragment extends SaasCommonFragment
    implements FlexibleAdapter.OnItemClickListener {
  TurnoverGirdSellerFragmentBinding mBinding;
  CommonFlexAdapter adapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = TurnoverGirdSellerFragmentBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getContext(), 4);
    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    mBinding.recyclerView.setLayoutManager(gridLayoutManager);
    mBinding.recyclerView.setItemViewCacheSize(4);
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    adapter.setMode(SelectableAdapter.Mode.SINGLE);
  }

  @Override public boolean onItemClick(int position) {
    adapter.toggleSelection(position);
    adapter.notifyItemChanged(position);
    return false;
  }
}
