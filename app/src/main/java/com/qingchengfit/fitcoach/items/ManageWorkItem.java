package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.TextpaperUtils;
import com.qingchengfit.fitcoach.bean.FunctionBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class ManageWorkItem extends AbstractFlexibleItem<ManageWorkItem.ManageWorkVH> {

    public FunctionBean bean;


    public ManageWorkItem(FunctionBean bean) {
        this.bean = bean;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_manage_work;
    }

    @Override
    public ManageWorkVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ManageWorkVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ManageWorkVH holder, int position, List payloads) {
        holder.title.setText(bean.text);
        if (TextpaperUtils.isEmpty(bean.subname)){
            holder.subTitle.setVisibility(View.VISIBLE);
            holder.subTitle.setText(bean.subname);
        }else holder.subTitle.setVisibility(View.GONE);
        Glide.with(holder.itemView.getContext()).load(bean.resImg).into(holder.image);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public class ManageWorkVH extends FlexibleViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.subTitle)
        TextView subTitle;
        public ManageWorkVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}