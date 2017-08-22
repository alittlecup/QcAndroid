package cn.qingchengfit.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class RectAddItem extends AbstractFlexibleItem<RectAddItem.RectAddVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_rect_add;
    }

    @Override public RectAddVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new RectAddVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, RectAddVH holder, int position, List payloads) {
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class RectAddVH extends FlexibleViewHolder {

        public RectAddVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}