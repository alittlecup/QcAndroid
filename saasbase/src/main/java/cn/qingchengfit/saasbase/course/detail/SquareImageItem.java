package cn.qingchengfit.saasbase.course.detail;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhoto;
import cn.qingchengfit.saasbase.databinding.ItemSquareImageBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class SquareImageItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemSquareImageBinding>> {
  public SchedulePhoto getPhoto() {
    return photo;
  }

  SchedulePhoto photo;

  public SquareImageItem(SchedulePhoto url) {
    this.photo = url;
  }

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
    PhotoUtils.origin(dataBinding.imageView, photo.getPhoto());
    if (adapter instanceof CommonFlexAdapter) {
      dataBinding.checkBox.setVisibility(
          ((CommonFlexAdapter) adapter).getStatus() == 1 ? View.VISIBLE : View.GONE);
      dataBinding.checkBox.setChecked(adapter.isSelected(position));
    }
  }
}
