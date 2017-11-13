package cn.qingchengfit.staffkit.views.gym.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.GymMoreAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class EmptyFunItem extends AbstractFlexibleItem<EmptyFunItem.EmptyFunVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_empty_fun;
    }

    @Override public EmptyFunVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new EmptyFunVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, EmptyFunVH holder, int position, List payloads) {
        if (adapter instanceof GymMoreAdapter) {
            if (((GymMoreAdapter) adapter).getStatus() > 0) {
                holder.tvHint.setText(holder.tvHint.getContext().getString(R.string.hint_empty_fun_edit));
            } else {
                holder.tvHint.setText(holder.tvHint.getContext().getString(R.string.hint_empty_fun_nomal));
            }
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class EmptyFunVH extends FlexibleViewHolder {
        @BindView(R.id.tv_hint) TextView tvHint;

        public EmptyFunVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}