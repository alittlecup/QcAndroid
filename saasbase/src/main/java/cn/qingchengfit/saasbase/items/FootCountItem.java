package cn.qingchengfit.saasbase.items;

import android.view.View;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class FootCountItem extends AbstractFlexibleItem<FootCountItem.FootCountVH> {
    String count;

    public FootCountItem(String count) {
        this.count = count;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_foot_count;
    }

    @Override public FootCountVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new FootCountVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, FootCountVH holder, int position, List payloads) {
        holder.tvFootStr.setText(count);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class FootCountVH extends FlexibleViewHolder {
	TextView tvFootStr;

        public FootCountVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          tvFootStr = (TextView) view.findViewById(R.id.tv_foot_str);
        }
    }
}