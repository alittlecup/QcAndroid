package cn.qingchengfit.saasbase.bill.filter;

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
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.filter.model.Content;
import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
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

//侧滑筛选栏
public class FragmentBillFilter extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, ItemFilterCommon.OnCheckedSelectListener {

  @BindView(R2.id.btn_bill_filter_reset) TextView btnBillFilterReset;
  @BindView(R2.id.btn_bill_filter_confirm) TextView btnBillFilterConfirm;
  Unbinder unbinder;
  @Inject BillFilterPresenter presenter;
  @BindView(R2.id.recycler_bill_filter) RecyclerView recyclerBillFilter;
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

  private void dispatchItem(List<FilterModel> filters){
    for (FilterModel filter : filters){
      switch (filter.key){

      }
    }
    adapter.notifyDataSetChanged();
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

  @Override public void onCheckedContent(List<Content> contentList) {

  }
}
