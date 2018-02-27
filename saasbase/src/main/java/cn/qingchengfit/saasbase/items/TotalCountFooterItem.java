package cn.qingchengfit.saasbase.items;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by yangming on 16/9/1.
 */
public class TotalCountFooterItem extends AbstractFlexibleItem<TotalCountFooterItem.ItemViewHolder> {

    private int totalCount = 0;

    public TotalCountFooterItem(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_total_count_footer;
    }

    @Override public ItemViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ItemViewHolder(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ItemViewHolder holder, int position, List payloads) {
        holder.tvTotalCount.setText((adapter.getItemCount() - 1) + "名会员");
    }

    @Override public boolean equals(Object o) {
        return o instanceof TotalCountFooterItem;
    }

    public static class ItemViewHolder extends FlexibleViewHolder {

        TextView tvTotalCount;

        public ItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            tvTotalCount = view.findViewById(R.id.tv_total_count);
        }
    }
}