package com.qingchengfit.fitcoach.items;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.event.EventChooseGym;
import com.qingchengfit.fitcoach.event.EventClickManageBrand;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.DividerItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BrandShopsItem extends AbstractFlexibleItem<BrandShopsItem.BrandShopsVH> implements FlexibleAdapter.OnItemClickListener {

    Brand brand;
    List<ChosenGymItem> datas;
    CommonFlexAdapter commonFlexAdapter;
    public BrandShopsItem(Brand brand, List<ChosenGymItem> datas) {
        this.brand = brand;
        this.datas = datas;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_brand_shops;
    }

    @Override public BrandShopsVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        BrandShopsVH vh = new BrandShopsVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
        return vh;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, BrandShopsVH holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getGauss(brand.getPhoto()))
            .placeholder(R.drawable.bg_brand)
            .into(holder.imgBg);
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(brand.getPhoto()))
            .asBitmap()
            .placeholder(R.drawable.ic_default_header)
            .into(new CircleImgWrapper(holder.imgBrand, holder.itemView.getContext()));
        holder.tvBrandName.setText(brand.getName());
        holder.recyclerInsert.setLayoutManager(new SmoothScrollLinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerInsert.addItemDecoration(new DividerItemDecoration(holder.itemView.getContext()));
        commonFlexAdapter = new CommonFlexAdapter(datas, this);
        holder.recyclerInsert.setAdapter(commonFlexAdapter);
        holder.btnManageBrand.setOnClickListener(v -> {
            if (brand.has_permission) {
                RxBus.getBus().post(new EventClickManageBrand(brand));
            } else {
                ToastUtils.show("您没有该场馆权限");
            }
        });
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public boolean onItemClick(int position) {
        if (datas.size() > position && datas.get(position) instanceof ChosenGymItem) {
            RxBus.getBus().post(new EventChooseGym(((ChosenGymItem) datas.get(position)).coachService));
        }
        return true;
    }

    public class BrandShopsVH extends FlexibleViewHolder {
        @BindView(R.id.img_bg) ImageView imgBg;
        @BindView(R.id.img_brand) ImageView imgBrand;
        @BindView(R.id.btn_manage_brand) TextView btnManageBrand;
        @BindView(R.id.tv_brand_name) TextView tvBrandName;
        @BindView(R.id.recycler_insert) RecyclerView recyclerInsert;

        public BrandShopsVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}