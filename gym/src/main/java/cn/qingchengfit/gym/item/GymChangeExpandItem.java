package cn.qingchengfit.gym.item;

import android.databinding.DataBindingUtil;
import android.view.View;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyChangeGymItemBinding;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractExpandableItem;
import eu.davidea.viewholders.ExpandableViewHolder;
import java.util.List;

public class GymChangeExpandItem extends
    AbstractExpandableItem<GymChangeExpandItem.GymChangeExpandViewHolder, GymChangeChildItem> {
  public GymChangeExpandItem() {
    setExpanded(true);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.gy_change_gym_item;
  }

  @Override public GymChangeExpandViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
    return new GymChangeExpandViewHolder(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, GymChangeExpandViewHolder holder,
      int position, List payloads) {
    GyChangeGymItemBinding mBinding = holder.mBinding;
  }

  class GymChangeExpandViewHolder extends ExpandableViewHolder {
    GyChangeGymItemBinding mBinding;

    public GymChangeExpandViewHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      mBinding = DataBindingUtil.bind(view);
    }
  }
}
