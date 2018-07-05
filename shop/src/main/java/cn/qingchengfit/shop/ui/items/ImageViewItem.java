package cn.qingchengfit.shop.ui.items;

import android.view.View;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemEmptyImageBinding;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/29.
 */

public class ImageViewItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemEmptyImageBinding>> {
  private String uri;

  public ImageViewItem(String uri) {
    this.uri = uri;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_empty_image;
  }

  @Override public DataBindingViewHolder<ItemEmptyImageBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemEmptyImageBinding> holder, int position, List payloads) {
    ItemEmptyImageBinding dataBinding = holder.getDataBinding();
    Glide.with(dataBinding.getRoot().getContext()).load(uri).asBitmap().into(dataBinding.image);
  }
}
