package cn.qingcheng.gym.item;

import android.view.View;
import cn.qingcheng.gym.vo.IGymSearchItemData;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymSearchItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GymSearchItem
    extends AbstractFlexibleItem<DataBindingViewHolder<GyGymSearchItemBinding>> {
  public IGymSearchItemData getData() {
    return data;
  }

  private IGymSearchItemData data;

  public GymSearchItem(IGymSearchItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.gy_gym_search_item;
  }

  @Override public DataBindingViewHolder<GyGymSearchItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<GyGymSearchItemBinding> holder, int position, List payloads) {
    GyGymSearchItemBinding dataBinding = holder.getDataBinding();
    dataBinding.setData(data);
    if (data.getBrandPhoto() != null) {
      PhotoUtils.smallCircle(dataBinding.imgGymPhoto, data.getBrandPhoto());
    }
  }
}
