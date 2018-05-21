package com.qingchengfit.fitcoach.items;

import android.view.View;
import android.widget.ImageView;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChosenAllGymItem extends AbstractFlexibleItem<ChosenAllGymItem.ChosenAllGymVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_all_gyms;
    }

    @Override public ChosenAllGymVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChosenAllGymVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChosenAllGymVH holder, int position, List payloads) {
        holder.chosen.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChosenAllGymVH extends FlexibleViewHolder {
	ImageView chosen;

        public ChosenAllGymVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          chosen = (ImageView) view.findViewById(R.id.chosen);
        }
    }
}