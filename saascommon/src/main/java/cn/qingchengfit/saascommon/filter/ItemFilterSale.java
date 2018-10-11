package cn.qingchengfit.saascommon.filter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/11/16.
 */

public class ItemFilterSale extends AbstractFlexibleItem<ItemFilterSale.FilterSaleVH> {

  public User user;

  public ItemFilterSale(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_filter_sale;
  }

  @Override public FilterSaleVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new FilterSaleVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, FilterSaleVH holder, int position,
      List payloads) {

    Glide.with(holder.itemView.getContext())
        .load(PhotoUtils.getSmall(user.avatar))
        .asBitmap()
        .into(new CircleImgWrapper(holder.filterSellerHead, holder.itemView.getContext()));

    holder.tvFilterSellerName.setText(user.username);
    holder.tvFilterSellerPhone.setText(user.phone);
    if (adapter.isSelected(position)){
      holder.tvFilterSellerName.setTextColor(holder.itemView.getResources().getColor(R.color.colorPrimary));
      holder.tvFilterSellerPhone.setTextColor(holder.itemView.getResources().getColor(R.color.colorPrimary));
      holder.imgHook.setVisibility(View.VISIBLE);
    }else{
      holder.tvFilterSellerName.setTextColor(holder.itemView.getResources().getColor(R.color.qc_text_black));
      holder.tvFilterSellerPhone.setTextColor(holder.itemView.getResources().getColor(R.color.qc_text_black));
      holder.imgHook.setVisibility(View.GONE);
    }

  }

  class FilterSaleVH extends FlexibleViewHolder {

	ImageView filterSellerHead;
	TextView tvFilterSellerName;
	TextView tvFilterSellerPhone;
	ImageView imgHook;

    public FilterSaleVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      filterSellerHead = view.findViewById(R.id.filter_seller_head);
      tvFilterSellerName = view.findViewById(R.id.tv_filter_seller_name);
      tvFilterSellerPhone = view.findViewById(R.id.tv_filter_seller_phone);
      imgHook = view.findViewById(R.id.img_hook);
    }
  }
}
