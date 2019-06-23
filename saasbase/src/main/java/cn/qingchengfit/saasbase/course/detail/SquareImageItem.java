package cn.qingchengfit.saasbase.course.detail;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ItemSquareImageBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class SquareImageItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemSquareImageBinding>> {
  String url;

  public SquareImageItem(String url) {
    this.url = url;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_circle_image;
  }

  @Override public DataBindingViewHolder<ItemSquareImageBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemSquareImageBinding> holder, int position, List payloads) {
    ItemSquareImageBinding dataBinding = holder.getDataBinding();
    PhotoUtils.origin(dataBinding.imageView, url);
  }
}
