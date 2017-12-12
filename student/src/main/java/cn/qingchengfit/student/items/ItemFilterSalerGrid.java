package cn.qingchengfit.student.items;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.bill.filter.model.Content;
import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.saasbase.student.items.*;
import cn.qingchengfit.saasbase.student.items.ChooseStaffItem;
import cn.qingchengfit.student.R;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by huangbaole on 2017/12/11.
 */

public class ItemFilterSalerGrid extends AbstractFlexibleItem<ItemFilterSalerGrid.ViewHolder> {
    private FilterModel filterModel;
    private CommonFlexAdapter flexAdapter;
    private List<AbstractFlexibleItem> itemList = new ArrayList<>();
    private boolean isShowAll;
    private String selectedId;

    public ItemFilterSalerGrid(FilterModel filterModel) {
        this.filterModel = filterModel;
        initItemList();
        flexAdapter = new CommonFlexAdapter(itemList, new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int position) {
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


//    private void setInitList() {
//        for (int i = 0; i < flexAdapter.getItemCount(); i++) {
//            if (flexAdapter.getItem(i) instanceof FilterCommonLinearItem) {
//                FilterCommonLinearItem item = (FilterCommonLinearItem) flexAdapter.getItem(i);
//                if (item.getData().equals(selectedId)) {
//                    flexAdapter.addSelection(i);
//                    flexAdapter.notifyItemChanged(i);
//                    break;
//                }
//            }
//        }
//    }

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
        return R.layout.item_followup_saler;
    }

    public void setShowAll(boolean showAll) {
        isShowAll = showAll;
    }

    public boolean isShowAll() {
        return isShowAll;
    }

    @Override
    public ItemFilterSalerGrid.ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        ItemFilterSalerGrid.ViewHolder holder = new ItemFilterSalerGrid.ViewHolder(view, adapter);
        holder.recyclerFilterList.setLayoutManager(
                new GridLayoutManager(holder.itemView.getContext(), 4));
        holder.recyclerFilterList.setAdapter(flexAdapter);
        return holder;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ItemFilterSalerGrid.ViewHolder holder, int position, List payloads) {
        holder.tvFilterListTitle.setText(filterModel.name);
        if (adapter.isSelected(position)) {
            clearSelect();
            return;
        }

        initItemList();

        VectorDrawableCompat drawableR = VectorDrawableCompat.create(holder.itemView.getResources(),
                isShowAll ? cn.qingchengfit.saasbase.R.drawable.vd_filter_arrow_up : cn.qingchengfit.saasbase.R.drawable.vd_filter_arrow_down, null);
        drawableR.setBounds(0, 0, drawableR.getMinimumWidth(), drawableR.getMinimumHeight());
        holder.tvFilterShowAll.setCompoundDrawables(null, null, drawableR, null);
        if(flexAdapter !=holder.recyclerFilterList.getAdapter()){
            flexAdapter= (CommonFlexAdapter) holder.recyclerFilterList.getAdapter();
        }
        flexAdapter.updateDataSet(itemList);
        holder.tvFilterShowAll.setTag(position);

        if(flexAdapter.getSelectedItemCount() ==0){
            flexAdapter.addSelection(0);
        }
//        if (!TextUtils.isEmpty(selectedId)) {
//            setInitList();
//        }
    }

    private void initItemList() {
        if (filterModel.content.size() > 8) {
            if (itemList.size() > 0 && !isShowAll) {
                itemList.clear();
            }
            if (!isShowAll) {
                for (int i = 0; i < 8; i++) {
                    itemList.add(new FilterGridItem(filterModel.content.get(i).extra.user));
                }
            } else {
                for (int i = 8; i < filterModel.content.size(); i++) {
                    itemList.add(new FilterGridItem(filterModel.content.get(i).extra.user));
                }
            }
        } else {
            itemList.clear();
            for (Content content : filterModel.content) {
                itemList.add(new FilterGridItem(content.extra.user));
            }
        }
    }


    class ViewHolder extends FlexibleViewHolder {

        @BindView(R2.id.recycler_filter_list)
        RecyclerView recyclerFilterList;
        @BindView(R2.id.tv_filter_show_all)
        TextView tvFilterShowAll;
        @BindView(R2.id.tv_filter_list_title)
        TextView tvFilterListTitle;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            tvFilterShowAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    if (adapter.getItem(position) instanceof ItemFilterSalerGrid) {
                        ((ItemFilterSalerGrid) adapter.getItem(position)).setShowAll(
                                !((ItemFilterSalerGrid) adapter.getItem(position)).isShowAll());
//                        adapter.notifyItemChanged(position);

                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
