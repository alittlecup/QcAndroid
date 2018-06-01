package com.qingchengfit.fitcoach.items;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.adapter.CommonFlexAdapter;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.event.EventChooseGym;
import com.qingchengfit.fitcoach.event.EventClickManageBrand;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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

    @Override public BrandShopsVH createViewHolder(View view, FlexibleAdapter adapter) {
        BrandShopsVH vh = new BrandShopsVH(view, adapter);
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
        //holder.recyclerInsert.addItemDecoration(new DividerItemDecoration(holder.itemView.getContext()));
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
	ImageView imgBg;
	ImageView imgBrand;
	TextView btnManageBrand;
	TextView tvBrandName;
	RecyclerView recyclerInsert;

        public BrandShopsVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          imgBg = (ImageView) view.findViewById(R.id.img_bg);
          imgBrand = (ImageView) view.findViewById(R.id.img_brand);
          btnManageBrand = (TextView) view.findViewById(R.id.btn_manage_brand);
          tvBrandName = (TextView) view.findViewById(R.id.tv_brand_name);
          recyclerInsert = (RecyclerView) view.findViewById(R.id.recycler_insert);
        }
    }
}