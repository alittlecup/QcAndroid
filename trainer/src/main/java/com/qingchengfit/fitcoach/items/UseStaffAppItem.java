package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class UseStaffAppItem extends AbstractFlexibleItem<UseStaffAppItem.UseStaffAppVH> {

    @Override public int getLayoutRes() {
        return R.layout.item_use_staff_app;
    }

    @Override public UseStaffAppVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new UseStaffAppVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, UseStaffAppVH holder, int position, List payloads) {
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class UseStaffAppVH extends FlexibleViewHolder {

        public UseStaffAppVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}