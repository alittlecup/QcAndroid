package cn.qingcheng.gym.item;

import android.view.View;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymSimpleListItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GymSimpleListItem
    extends AbstractFlexibleItem<DataBindingViewHolder<GyGymSimpleListItemBinding>> {
  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.gy_gym_simple_list_item;
  }

  @Override public DataBindingViewHolder<GyGymSimpleListItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<GyGymSimpleListItemBinding> holder, int position, List payloads) {
    GyGymSimpleListItemBinding dataBinding = holder.getDataBinding();
  }
}
