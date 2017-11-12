package cn.qingchengfit.saasbase.student.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

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

    @Override public ItemViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ItemViewHolder holder, int position, List payloads) {
        holder.tvTotalCount.setText((adapter.getItemCount() - 1) + "名会员");
    }

    @Override public boolean equals(Object o) {
        return o instanceof TotalCountFooterItem;
    }

    public static class ItemViewHolder extends FlexibleViewHolder {

        @BindView(R2.id.tv_total_count) TextView tvTotalCount;

        public ItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}