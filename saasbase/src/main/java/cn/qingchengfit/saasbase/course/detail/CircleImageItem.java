package cn.qingchengfit.saasbase.course.detail;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ItemCircleImageBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class CircleImageItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemCircleImageBinding>> {
  String url;

  public CircleImageItem(String url) {
    this.url = url;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_circle_image;
  }

  @Override public DataBindingViewHolder<ItemCircleImageBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemCircleImageBinding> holder, int position, List payloads) {
    ItemCircleImageBinding dataBinding = holder.getDataBinding();
    PhotoUtils.smallCircle(dataBinding.imageView, url);
  }
}
