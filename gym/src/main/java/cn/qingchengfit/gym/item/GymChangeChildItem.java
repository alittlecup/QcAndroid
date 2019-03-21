package cn.qingchengfit.gym.item;

import android.view.View;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyChangeGymChildItemBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GymChangeChildItem extends AbstractFlexibleItem<DataBindingViewHolder<GyChangeGymChildItemBinding>> {
  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.gy_change_gym_child_item;
  }

  @Override public DataBindingViewHolder<GyChangeGymChildItemBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<GyChangeGymChildItemBinding> holder, int position, List payloads) {
    GyChangeGymChildItemBinding dataBinding = holder.getDataBinding();
  }
}
