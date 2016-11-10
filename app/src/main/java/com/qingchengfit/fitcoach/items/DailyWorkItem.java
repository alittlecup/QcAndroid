package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;

import java.util.List;

import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class DailyWorkItem extends AbstractFlexibleItem<DailyWorkItem.DailyWorkVH> {

    @Override
    public int getLayoutRes() {
        return R.layout.item_daily_work;
    }

    @Override
    public DailyWorkVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new DailyWorkVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, DailyWorkVH holder, int position, List payloads) {
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public class DailyWorkVH extends FlexibleViewHolder {

        public DailyWorkVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}