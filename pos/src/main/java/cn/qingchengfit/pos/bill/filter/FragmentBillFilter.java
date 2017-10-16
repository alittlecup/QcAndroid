package cn.qingchengfit.pos.bill.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/11.
 */

public class FragmentBillFilter extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener {

  @BindView(R.id.btn_bill_filter_reset) TextView btnBillFilterReset;
  @BindView(R.id.btn_bill_filter_confirm) TextView btnBillFilterConfirm;
  Unbinder unbinder;
  @Inject BillFilterPresenter presenter;
  @BindView(R.id.recycler_bill_filter) RecyclerView recyclerBillFilter;
  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_bill_filter, container, false);
    unbinder = ButterKnife.bind(this, view);
    initView();
    return view;
  }

  private void initView() {
    adapter = new CommonFlexAdapter(itemList, this);
    recyclerBillFilter.setAdapter(adapter);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    return false;
  }
}
