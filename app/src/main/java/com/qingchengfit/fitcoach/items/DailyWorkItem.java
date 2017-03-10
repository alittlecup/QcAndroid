package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.FunctionBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class DailyWorkItem extends AbstractFlexibleItem<DailyWorkItem.DailyWorkVH> {

    public FunctionBean bean;


    public DailyWorkItem(FunctionBean bean) {
        this.bean = bean;
    }

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
        if (bean != null) {
            holder.text.setText(bean.text);
            holder.img.setImageResource(bean.resImg);
        }
//        Glide.with(holder.itemView.getContext()).load(bean.resImg).into(holder.img);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public class DailyWorkVH extends FlexibleViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.text)
        TextView text;
        public DailyWorkVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}