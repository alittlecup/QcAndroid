package cn.qingchengfit.saasbase.turnovers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.databinding.TurnoversFilterSelectFragmentBinding;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import java.util.ArrayList;
import java.util.List;

public class TurnoverFilterSimpleChooseFragment extends SaasCommonFragment
    implements FlexibleAdapter.OnItemClickListener {
  TurnoversFilterSelectFragmentBinding mBinding;
  CommonFlexAdapter adapter;
  List<? extends ITurnoverFilterItemData> datas;
  FlexibleAdapter.OnItemClickListener listener;

  public static TurnoverFilterSimpleChooseFragment newInstance(
      List<? extends ITurnoverFilterItemData> datas, FlexibleAdapter.OnItemClickListener listener) {
    TurnoverFilterSimpleChooseFragment fragment = new TurnoverFilterSimpleChooseFragment();
    fragment.listener = listener;
    Bundle bundle = new Bundle();
    if (datas != null) {
      bundle.putParcelableArrayList("datas", new ArrayList<>(datas));
    }
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = TurnoversFilterSelectFragmentBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  public void setDatas(List<? extends ITurnoverFilterItemData> datas) {
    if (datas == null) {
      return;
    }
    this.datas = datas;
    List<TurnoversFilterItem> items = new ArrayList<>();
    for (ITurnoverFilterItemData data : datas) {
      items.add(new TurnoversFilterItem(data));
    }
    if (adapter != null) {
      adapter.updateDataSet(items);
    }
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.turnoversFilterRecycler.setLayoutManager(
        new SmoothScrollGridLayoutManager(getContext(), 3));
    adapter = new CommonFlexAdapter(new ArrayList(), this);
    adapter.setMode(SelectableAdapter.Mode.SINGLE);
    mBinding.turnoversFilterRecycler.setAdapter(adapter);
    setDatas(getArguments().getParcelableArrayList("datas"));
  }

  @Override public boolean onItemClick(int position) {
    adapter.toggleSelection(position);
    adapter.notifyDataSetChanged();
    if (listener != null) {
      listener.onItemClick(position);
    }
    return false;
  }
}
