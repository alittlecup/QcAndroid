package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ImageItem extends AbstractFlexibleItem<ImageItem.ImageVH> {

    String imgPath;
    String id;
    public ImageItem(String imgPath, String id) {
        this.imgPath = imgPath;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_image_wall_choose;
    }

    @Override public ImageVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ImageVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ImageVH holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext()).load(imgPath).placeholder(R.drawable.img_loadingimage).into(holder.img);
        if (adapter.getMode() == SelectableAdapter.MODE_IDLE) {
            holder.checkbox.setVisibility(View.GONE);
        } else {
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setChecked(adapter.isSelected(position));
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ImageVH extends FlexibleViewHolder {
        @BindView(R.id.img) ImageView img;
        @BindView(R.id.checkbox) CheckBox checkbox;

        public ImageVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}