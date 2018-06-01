package cn.qingchengfit.staffkit.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseShopItemItem extends AbstractFlexibleItem<ChooseShopItemItem.ChooseShopItemVH> {

    String img;
    String name;

    public ChooseShopItemItem(String img, String name) {
        this.img = img;
        this.name = name;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_image_text_choose;
    }

    @Override public ChooseShopItemVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseShopItemVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseShopItemVH holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext()).load(img).asBitmap().into(new CircleImgWrapper(holder.img, holder.itemView.getContext()));
        holder.tvName.setText(name);
        holder.chosen.setVisibility(adapter.isSelected(position) ? View.VISIBLE : View.GONE);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChooseShopItemVH extends FlexibleViewHolder {
	ImageView img;
	TextView tvName;
	ImageView chosen;

        public ChooseShopItemVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          img = (ImageView) view.findViewById(R.id.img);
          tvName = (TextView) view.findViewById(R.id.tv_name);
          chosen = (ImageView) view.findViewById(R.id.chosen);
        }
    }
}