package cn.qingchengfit.saasbase.course.detail;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ItemSquareImageBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GirdImageAddItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemSquareImageBinding>> {
  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_square_image;
  }

  @Override public DataBindingViewHolder<ItemSquareImageBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemSquareImageBinding> holder, int position, List payloads) {
    ItemSquareImageBinding dataBinding = holder.getDataBinding();
    dataBinding.imageView.setImageResource(R.drawable.vd_add_grey_40dp);
    dataBinding.imageView.setBackgroundResource(R.drawable.bg_rect_square_img);
  }
}
