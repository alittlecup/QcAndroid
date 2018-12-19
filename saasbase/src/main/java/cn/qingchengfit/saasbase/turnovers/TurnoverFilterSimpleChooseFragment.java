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
  List<ITurnoverFilterItemData> datas;
  FlexibleAdapter.OnItemClickListener listener;

  public static TurnoverFilterSimpleChooseFragment newInstance(List<ITurnoverFilterItemData> datas,
      FlexibleAdapter.OnItemClickListener listener) {
    TurnoverFilterSimpleChooseFragment fragment = new TurnoverFilterSimpleChooseFragment();
    fragment.listener = listener;
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList("datas", new ArrayList<>(datas));
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = TurnoversFilterSelectFragmentBinding.inflate(inflater, container, false);
    datas = getArguments().getParcelableArrayList("datas");
    return mBinding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.turnoversFilterRecycler.setLayoutManager(
        new SmoothScrollGridLayoutManager(getContext(), 3));
    if (datas == null) {
      return;
    }
    List<TurnoversFilterItem> items = new ArrayList<>();
    for (ITurnoverFilterItemData data : datas) {
      items.add(new TurnoversFilterItem(data));
    }
    adapter = new CommonFlexAdapter(items, this);
    adapter.setMode(SelectableAdapter.Mode.SINGLE);
    mBinding.turnoversFilterRecycler.setAdapter(adapter);
  }

  @Override public boolean onItemClick(int position) {
    adapter.toggleSelection(position);
    adapter.notifyDataSetChanged();
    return false;
  }
}
