package cn.qingchengfit.saasbase.bill.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.event.BillFilterEvent;
import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/11.
 */

//侧滑筛选栏
public class BillFilterFragment extends BaseFragment
    implements FlexibleAdapter.OnItemClickListener, BillFilterPresenter.MVPView,
    ItemFilterTime.OnTimeChooseListener {

  @BindView(R2.id.btn_bill_filter_reset) TextView btnBillFilterReset;
  @BindView(R2.id.btn_bill_filter_confirm) TextView btnBillFilterConfirm;
  @Inject BillFilterPresenter presenter;
  @BindView(R2.id.recycler_bill_filter) RecyclerView recyclerBillFilter;
  @BindView(R2.id.filter_layout) LinearLayout filterLayout;
  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();
  HashMap<String, Object> map = new HashMap<>();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_bill_filter, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    presenter.getFilterList();
    initView();
    return view;
  }

  private void initView() {
    adapter = new CommonFlexAdapter(itemList, this);
    recyclerBillFilter.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerBillFilter.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDivider(R.drawable.divider_grey_left_margin)
            .withBottomEdge(true));
    recyclerBillFilter.setAdapter(adapter);
  }

  @OnClick(R2.id.btn_bill_filter_confirm)
  public void onConfirm(){
    String key = "";
    for (int i = 0; i < adapter.getItemCount(); i++) {
      if (adapter.getItem(i) instanceof ItemFilterCommon) {
        ItemFilterCommon item = ((ItemFilterCommon) adapter.getItem(i));
        key = item.getData().key;
        if (item.getCheckedContent().size() > 0) {
          map.put(key, "");
        }else{
          if (map.containsKey(key)){
            map.remove(key);
          }
          continue;
        }
        for (int j = 0; j < item.getCheckedContent().size(); j++){
          map.put(key, (TextUtils.isEmpty(String.valueOf(map.get(key))) ? map.get(key)
              : (map.get(key) + ",")) + item.getCheckedContent().get(j).value);
        }
      }
    }
    RxBus.getBus().post(new BillFilterEvent(map));
  }

  @OnClick(R2.id.btn_bill_filter_reset)
  public void onReset(){
    for (int i = 0; i < adapter.getItemCount(); i++){
      adapter.addSelection(i);
    }
    adapter.notifyDataSetChanged();
    map.clear();
    RxBus.getBus().post(new BillFilterEvent(map));
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int position) {
    return false;
  }

  @Override public void onGetFilter(List<FilterModel> filters) {
    for (FilterModel filter : filters){
      if (filter.type == 2){
        itemList.add(new ItemFilterTime(filter, this));
      }else {
        itemList.add(new ItemFilterCommon(filter));
      }
    }
    adapter.notifyDataSetChanged();
  }

  @Override public void onTimeStart(String start, String key) {
    map.put(key + "__gte", start + "T00:00:00");
  }

  @Override public void onTimeEnd(String end, String key) {
    map.put(key + "__lte", end + "T00:00:00");
  }
}
