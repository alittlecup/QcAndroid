package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChosenAllGymItem extends AbstractFlexibleItem<ChosenAllGymItem.ChosenAllGymVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_all_gyms;
    }

    @Override public ChosenAllGymVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ChosenAllGymVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChosenAllGymVH holder, int position, List payloads) {
        holder.chosen.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChosenAllGymVH extends FlexibleViewHolder {
        @BindView(R.id.chosen) ImageView chosen;

        public ChosenAllGymVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}