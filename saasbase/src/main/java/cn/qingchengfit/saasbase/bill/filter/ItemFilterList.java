package cn.qingchengfit.saasbase.bill.filter;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.filter.model.Content;
import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fb on 2017/11/16.
 */

//销售筛选项
public class ItemFilterList extends AbstractFlexibleItem<ItemFilterList.FilterListVH> {

  private FilterModel filterModel;
  private CommonFlexAdapter flexAdapter;
  private List<AbstractFlexibleItem> itemList = new ArrayList<>();
  private boolean isShowAll;

  public ItemFilterList(FilterModel filterModel) {
    this.filterModel = filterModel;
    flexAdapter = new CommonFlexAdapter(itemList, new FlexibleAdapter.OnItemClickListener() {
      @Override public boolean onItemClick(int position) {
        if (flexAdapter.isSelected(position)) {
          flexAdapter.removeSelection(position);
        } else {
          clearSelect();
          flexAdapter.addSelection(position);
        }
        flexAdapter.notifyDataSetChanged();
        return false;
      }
    });
  }

  public FilterModel getFilterModel() {
    return filterModel;
  }

  //返回选中的销售
  public String getSelectedUser() {
    if (flexAdapter.getSelectedPositions().size() > 0) {
      return ((ItemFilterSale) flexAdapter.getItem(flexAdapter.getSelectedPositions().get(0))).getUser().id;
    }
    return "";
  }

  private void clearSelect(){
    for (int p : flexAdapter.getSelectedPositions()) {
      flexAdapter.removeSelection(p);
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_filter_list;
  }

  public void setShowAll(boolean showAll) {
    isShowAll = showAll;
  }

  public boolean isShowAll() {
    return isShowAll;
  }

  @Override public FilterListVH createViewHolder(View view, FlexibleAdapter adapter) {
    FilterListVH holder = new FilterListVH(view, adapter);
    holder.recyclerFilterList.setLayoutManager(
        new LinearLayoutManager(holder.itemView.getContext()));
    holder.recyclerFilterList.setAdapter(flexAdapter);
    return holder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, FilterListVH holder, int position,
      List payloads) {
    holder.tvFilterListTitle.setText(filterModel.name);
    if (adapter.isSelected(position)){
      clearSelect();
      return;
    }
    if (itemList.size() > 0 && !isShowAll) {
      itemList.clear();
    }
    if (filterModel.content.size() > 5) {
      if (!isShowAll) {
        for (int i = 0; i < 5; i++) {
          itemList.add(new ItemFilterSale(filterModel.content.get(i).extra.user));
        }
      } else {
        for (int i = 5; i < filterModel.content.size(); i++) {
          itemList.add(new ItemFilterSale(filterModel.content.get(i).extra.user));
        }
      }
    } else {
      for (Content content : filterModel.content) {
        itemList.add(new ItemFilterSale(content.extra.user));
      }
    }
    VectorDrawableCompat drawableR = VectorDrawableCompat.create(holder.itemView.getResources(),
        isShowAll ? R.drawable.vd_filter_arrow_up : R.drawable.vd_filter_arrow_down, null);
    drawableR.setBounds(0, 0, drawableR.getMinimumWidth(), drawableR.getMinimumHeight());
    holder.tvFilterShowAll.setCompoundDrawables(null,null,drawableR,null);
    flexAdapter.updateDataSet(itemList);
    holder.tvFilterShowAll.setTag(position);
  }

  class FilterListVH extends FlexibleViewHolder {

    @BindView(R2.id.recycler_filter_list) RecyclerView recyclerFilterList;
    @BindView(R2.id.tv_filter_show_all) TextView tvFilterShowAll;
    @BindView(R2.id.tv_filter_list_title) TextView tvFilterListTitle;

    public FilterListVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      tvFilterShowAll.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          int position = (int) view.getTag();
          ((ItemFilterList) adapter.getItem(position)).setShowAll(
              !((ItemFilterList) adapter.getItem(position)).isShowAll());
          adapter.notifyDataSetChanged();
        }
      });
    }
  }
}
