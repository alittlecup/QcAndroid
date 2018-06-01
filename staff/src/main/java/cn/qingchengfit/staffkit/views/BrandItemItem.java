package cn.qingchengfit.staffkit.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.staffkit.R;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BrandItemItem extends AbstractFlexibleItem<BrandItemItem.BrandItemVH> {

    Brand mBrand;
    Context mContext;

    public BrandItemItem(Brand brand, Context context) {
        mBrand = brand;
        mContext = context;
    }

    public Brand getBrand() {
        return mBrand;
    }

    public void setBrand(Brand brand) {
        mBrand = brand;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_brand;
    }

    @Override public BrandItemVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new BrandItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, BrandItemVH holder, int position, List payloads) {
        Glide.with(mContext)
            .load(mBrand.getPhoto())
            .asBitmap()
            .placeholder(R.drawable.ic_default_header)
            .into(new CircleImgWrapper(holder.brandAvatar, mContext));
        holder.brandName.setText(mBrand.getName());
        String user;
        if (mBrand.getCreated_by() != null && mBrand.getCreated_by().getUsername() != null) {
            user = mBrand.getCreated_by().getUsername();
        } else {
            user = "";
        }
        holder.brandContent.setText(mContext.getString(R.string.choose_brand_content, mBrand.getCname(), user));
        holder.chosen.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class BrandItemVH extends FlexibleViewHolder {
	ImageView brandAvatar;
	TextView brandName;
	TextView brandContent;
	ImageView chosen;

        public BrandItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          brandAvatar = (ImageView) view.findViewById(R.id.brand_avatar);
          brandName = (TextView) view.findViewById(R.id.brand_name);
          brandContent = (TextView) view.findViewById(R.id.brand_content);
          chosen = (ImageView) view.findViewById(R.id.chosen);
        }
    }
}