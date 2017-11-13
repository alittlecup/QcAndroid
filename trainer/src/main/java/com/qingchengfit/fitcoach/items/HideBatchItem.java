package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class HideBatchItem extends AbstractFlexibleItem<HideBatchItem.HideBatchVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_hide_batch;
    }

    @Override public HideBatchVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new HideBatchVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, HideBatchVH holder, int position, List payloads) {
        holder.btnShow.setText(adapter.isSelected(position) ? R.string.hide_out_batch : R.string.show_out_batch);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class HideBatchVH extends FlexibleViewHolder {
        @BindView(R.id.btn_show) TextView btnShow;

        public HideBatchVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}