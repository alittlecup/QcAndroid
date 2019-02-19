package cn.qingcheng.gym.item;

import android.view.View;
import cn.qingcheng.gym.vo.IGymBrandItemData;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymBrandItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GymBrandItem
    extends AbstractFlexibleItem<DataBindingViewHolder<GyGymBrandItemBinding>> {
  public IGymBrandItemData getData() {
    return data;
  }

  IGymBrandItemData data;

  public GymBrandItem(IGymBrandItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.gy_gym_brand_item;
  }

  @Override public DataBindingViewHolder<GyGymBrandItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<GyGymBrandItemBinding> holder, int position, List payloads) {
    GyGymBrandItemBinding dataBinding = holder.getDataBinding();
    dataBinding.setData(data);
    if (data.getGymPhoto() != null) {
      PhotoUtils.smallCircle(dataBinding.imgGymPhoto, data.getGymPhoto());
    }
  }
}
