package cn.qingchengfit.recruit.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ResumeBaseInfoItem extends AbstractFlexibleItem<ResumeBaseInfoItem.ResumeBaseInfoVH> {

    @Override public int getLayoutRes() {
        return 0;
    }

    @Override public ResumeBaseInfoVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ResumeBaseInfoVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ResumeBaseInfoVH holder, int position, List payloads) {
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ResumeBaseInfoVH extends FlexibleViewHolder {

        public ResumeBaseInfoVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}