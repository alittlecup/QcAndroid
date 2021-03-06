package cn.qingchengfit.student.item;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.saascommon.filter.model.Content;
import cn.qingchengfit.saascommon.filter.model.FilterModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/8.
 */

public class ItemFilterRecommend extends AbstractFlexibleItem<ItemFilterRecommend.ViewHolder> {
    private FilterModel filterModel;
    private CommonFlexAdapter flexAdapter;
    private List<AbstractFlexibleItem> itemList = new ArrayList<>();
    private boolean isShowAll;

    public ItemFilterRecommend(FilterModel filterModel) {
        this.filterModel = filterModel;
        flexAdapter = new CommonFlexAdapter(itemList,
            (FlexibleAdapter.OnItemClickListener) position -> {
                if (flexAdapter.isSelected(position)) {
                    flexAdapter.removeSelection(position);
                } else {
                    clearSelect();
                    flexAdapter.addSelection(position);
                }
                flexAdapter.notifyDataSetChanged();
                return false;
            });
    }

    public FilterModel getFilterModel() {
        return filterModel;
    }

    public String getSelectedSaler() {
        if (flexAdapter.getSelectedPositions().size() > 0) {
            return filterModel.content.get(flexAdapter.getSelectedPositions().get(0)).extra.user.id;
        }
        return "";
    }

    private void clearSelect() {
        for (int p : flexAdapter.getSelectedPositions()) {
            flexAdapter.removeSelection(p);
            flexAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_filter_saler_list2;
    }

    public void setShowAll(boolean showAll) {
        isShowAll = showAll;
    }

    public boolean isShowAll() {
        return isShowAll;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        ViewHolder holder = new ViewHolder(view, adapter);
        holder.recyclerFilterList.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerFilterList.setAdapter(flexAdapter);
        return holder;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.tvFilterListTitle.setText(filterModel.name);
        if (adapter.isSelected(position)) {
            clearSelect();
            return;
        }
        if (filterModel.content.size() > 3) {
            if (itemList.size() > 0 && !isShowAll) {
                itemList.clear();
            }
            if (!isShowAll) {
                for (int i = 0; i < 3; i++) {
                    itemList.add(new FilterCommonLinearItem(filterModel.content.get(i).extra.user.username));
                }
            } else {
                for (int i = 3; i < filterModel.content.size(); i++) {
                    itemList.add(new FilterCommonLinearItem(filterModel.content.get(i).extra.user.username));
                }
            }
        } else {
            itemList.clear();
            for (Content content : filterModel.content) {
                itemList.add(new FilterCommonLinearItem(content.extra.user.username));
            }
        }
        VectorDrawableCompat drawableR = VectorDrawableCompat.create(holder.itemView.getResources(),
                isShowAll ? R.drawable.vd_filter_arrow_up : R.drawable.vd_filter_arrow_down, null);
        drawableR.setBounds(0, 0, drawableR.getMinimumWidth(), drawableR.getMinimumHeight());
        holder.tvFilterShowAll.setCompoundDrawables(null, null, drawableR, null);

        if(flexAdapter !=holder.recyclerFilterList.getAdapter()){
            flexAdapter= (CommonFlexAdapter) holder.recyclerFilterList.getAdapter();
        }
        flexAdapter.updateDataSet(itemList);
        holder.tvFilterShowAll.setTag(position);
    }


    class ViewHolder extends FlexibleViewHolder {


        RecyclerView recyclerFilterList;

        TextView tvFilterShowAll;

        TextView tvFilterListTitle;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          recyclerFilterList = view.findViewById(R.id.recycler_filter_list);
          tvFilterShowAll = view.findViewById(R.id.tv_filter_show_all);
          tvFilterListTitle = view.findViewById(R.id.tv_filter_list_title);

          tvFilterShowAll.setOnClickListener(view1 -> {
              int position = (int) view1.getTag();
              if (adapter.getItem(position) instanceof ItemFilterRecommend) {
                  ((ItemFilterRecommend) adapter.getItem(position)).setShowAll(
                          !((ItemFilterRecommend) adapter.getItem(position)).isShowAll());
                  adapter.notifyItemChanged(position);
              }
          });
        }
    }
}
