package cn.qingchengfit.saasbase.bill;

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



import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.bill.event.BillFilterEvent;
import cn.qingchengfit.saascommon.filter.ItemFilterCommon;
import cn.qingchengfit.saascommon.filter.ItemFilterList;
import cn.qingchengfit.saascommon.filter.ItemFilterTime;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Arrays;
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

	TextView btnBillFilterReset;
	TextView btnBillFilterConfirm;
  @Inject BillFilterPresenter presenter;
	RecyclerView recyclerBillFilter;
	LinearLayout filterLayout;
  private CommonFlexAdapter adapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();
  private HashMap<String, Object> map = new HashMap<>();
  @Need HashMap<String, Object> alreadyMap = new HashMap<>();
  //@Inject
  //FilterViewModel filterModel;

  public static BillFilterFragment newInstance(HashMap<String, Object> map)  {
     Bundle args = new Bundle();
     args.putSerializable("filter", map);
     BillFilterFragment fragment = new BillFilterFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //filterModel = ViewModelProviders.of(this).get(FilterViewModel.class);
    //final Observer<HashMap<String, Object>> filterObserver = new Observer<HashMap<String, Object>>() {
    //  @Override public void onChanged(@Nullable HashMap<String, Object> stringObjectHashMap) {
    //    map = stringObjectHashMap;
    //  }
    //};
    //filterModel.getFitlerData().observe(this, filterObserver);

    if (getArguments() != null){
      map.clear();
      map.putAll((HashMap<String, Object>)getArguments().getSerializable("filter"));
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_bill_filter, container, false);
    btnBillFilterReset = (TextView) view.findViewById(R.id.btn_bill_filter_reset);
    btnBillFilterConfirm = (TextView) view.findViewById(R.id.btn_bill_filter_confirm);
    recyclerBillFilter = (RecyclerView) view.findViewById(R.id.recycler_bill_filter);
    filterLayout = (LinearLayout) view.findViewById(R.id.filter_layout);
    view.findViewById(R.id.btn_bill_filter_confirm).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onConfirm();
      }
    });
    view.findViewById(R.id.btn_bill_filter_reset).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onReset();
      }
    });

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

 public void onConfirm() {
    String key = "";
    for (int i = 0; i < adapter.getItemCount(); i++) {
      if (adapter.getItem(i) instanceof ItemFilterCommon) {
        ItemFilterCommon item = ((ItemFilterCommon) adapter.getItem(i));
        key = item.getData().key;
        if (item.getCheckedContent().size() > 0) {
          map.put(key, "");
        } else {
          if (map.containsKey(key)) {
            map.remove(key);
          }
          continue;
        }
        for (int j = 0; j < item.getCheckedContent().size(); j++) {
          map.put(key, (TextUtils.isEmpty(String.valueOf(map.get(key))) ? map.get(key)
              : (map.get(key) + ",")) + item.getCheckedContent().get(j).value);
        }
      } else if (adapter.getItem(i) instanceof ItemFilterList) {
        ItemFilterList item = (ItemFilterList) adapter.getItem(i);
        if (!TextUtils.isEmpty(item.getSelectedUser())) {
          map.put(item.getFilterModel().key, item.getSelectedUser());
        } else {
          if (map.containsKey(item.getFilterModel().key)) {
            map.remove(item.getFilterModel().key);
          }
        }
      }
    }
    RxBus.getBus().post(new BillFilterEvent(map));
    //filterModel.getFitlerData().setValue(map);
  }

 public void onReset() {
    for (int i = 0; i < adapter.getItemCount(); i++) {
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

  private void refreshView(){
    for (int i = 0; i < adapter.getItemCount(); i++ ){
     if (adapter.getItem(i) instanceof ItemFilterTime){
       String start = "", end = "";
       if (map.containsKey(((ItemFilterTime)adapter.getItem(i)).getFilterModel().key + "__gte")){
         start =
             (String) map.get(((ItemFilterTime)adapter.getItem(i)).getFilterModel().key + "__gte");
       }
       if(map.containsKey(((ItemFilterTime)adapter.getItem(i)).getFilterModel().key + "__lte")){
         end = (String) map.get(((ItemFilterTime)adapter.getItem(i)).getFilterModel().key + "__lte");
       }
       ((ItemFilterTime)adapter.getItem(i)).setInitTime(start, end);
     }else if (adapter.getItem(i) instanceof ItemFilterList){
       if (map.containsKey(((ItemFilterList)adapter.getItem(i)).getFilterModel().key)){
         ((ItemFilterList) adapter.getItem(i)).setSelectedId(
             (String) map.get(((ItemFilterList) adapter.getItem(i)).getFilterModel().key));
       }
     }else if (map.containsKey(((ItemFilterCommon)adapter.getItem(i)).getData().key)){
         String[] str =
             ((String) map.get(((ItemFilterCommon) adapter.getItem(i)).getData().key)).split(",");
         List<String> list = Arrays.asList(str);
         ((ItemFilterCommon) adapter.getItem(i)).setValueList(list);
       }
     }
  }

  @Override public void onGetFilter(List<FilterModel> filters) {
    if (itemList.size() > 0) {
      itemList.clear();
    }
    for (FilterModel filter : filters) {
      if (filter.type == 2) {
        itemList.add(new ItemFilterTime(filter, this));
      } else if (filter.type == 3) {
        itemList.add(new ItemFilterList(filter));
      } else {
        itemList.add(new ItemFilterCommon(filter));
      }
    }
    adapter.updateDataSet(itemList);
    if (map.size() > 0)
      refreshView();
  }

  @Override public void onTimeStart(String start, String key) {
    map.put(key + "__gte", start + "T00:00:00");
  }

  @Override public void onTimeEnd(String end, String key) {
    map.put(key + "__lte", end + "T00:00:00");
  }
}
