package cn.qingchengfit.staffkit.views.gym.items;

import android.view.View;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.ItemGymServiceSettingBinding;
import cn.qingchengfit.utils.DrawableUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class GymServiceSettingItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemGymServiceSettingBinding>> {
  public int getType() {
    return type;
  }

  private int type;

  public GymServiceSettingItem(int type) {
    this.type = type;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_gym_service_setting;
  }

  @Override public DataBindingViewHolder<ItemGymServiceSettingBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemGymServiceSettingBinding> holder, int position, List payloads) {
    ItemGymServiceSettingBinding dataBinding = holder.getDataBinding();
    switch (type) {
      case 1:
        dataBinding.tvName.setText("团课");
        dataBinding.imgIcon.setImageResource(R.drawable.moudule_service_group);
        break;
      case 2:
        dataBinding.tvName.setText("私教");
        dataBinding.imgIcon.setImageResource(R.drawable.moudule_service_private);

        break;
      case 3:
        dataBinding.tvName.setText("自主训练");
        dataBinding.imgIcon.setImageResource(R.drawable.moudule_service_free_train);
        break;
      case 4:
        dataBinding.tvName.setText("店内商品");
        dataBinding.imgIcon.setImageResource(R.drawable.moudule_service_shop);

        break;
    }
    if (adapter.isSelected(position)) {
      dataBinding.tvSetting.setText("完成设置");
      dataBinding.tvSetting.setCompoundDrawables(null, null, null, null);
      dataBinding.tvSettingPoint.setVisibility(View.GONE);
    } else {
      dataBinding.tvSetting.setText("去设置");
      dataBinding.tvSetting.setCompoundDrawables(null, null,
          DrawableUtils.tintDrawable(holder.getDataBinding().getRoot().getContext(),
              R.drawable.vd_arrow_right_grey_small, R.color.primary), null);
      dataBinding.tvSettingPoint.setVisibility(View.VISIBLE);
    }
  }
}
