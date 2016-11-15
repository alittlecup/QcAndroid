package com.qingchengfit.fitcoach.items;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.TextpaperUtils;
import com.qingchengfit.fitcoach.bean.FunctionBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.utils.CompatUtils;
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
        if (!TextpaperUtils.isEmpty(bean.subname)){
            holder.subTitle.setVisibility(View.VISIBLE);
            holder.subTitle.setText(bean.subname);
        }else holder.subTitle.setVisibility(View.GONE);
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
           drawable = holder.image.getContext().getResources().getDrawable(bean.resImg,null);
        }else drawable = holder.image.getContext().getResources().getDrawable(bean.resImg);
        DrawableCompat.setTint(drawable, CompatUtils.getColor(holder.itemView.getContext(),R.color.text_grey));
        holder.image.setImageDrawable(drawable);
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