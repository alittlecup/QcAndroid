package cn.qingchengfit.staffkit.views.abstractflexibleitem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
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

    @Override public FootCountVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new FootCountVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, FootCountVH holder, int position, List payloads) {
        holder.tvFootStr.setText(count);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class FootCountVH extends FlexibleViewHolder {
        @BindView(R.id.tv_foot_str) TextView tvFootStr;

        public FootCountVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}