package cn.qingchengfit.saasbase.turnovers;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saascommon.databinding.ItemChooseSalerBinding;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class TurnoverSellerItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemChooseSalerBinding>> {
  public ICommonGirdChooseData getData() {
    return data;
  }

  private ICommonGirdChooseData data;

  public TurnoverSellerItem(ICommonGirdChooseData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_choose_saler;
  }

  @Override public DataBindingViewHolder<ItemChooseSalerBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemChooseSalerBinding> holder, int position, List payloads) {
    ItemChooseSalerBinding dataBinding = holder.getDataBinding();
    int da = data.isMale() ? R.drawable.default_manage_male : R.drawable.default_manager_female;
    PhotoUtils.smallCircle(dataBinding.salerHeaderImg, data.getAvatar(), da, da);
    dataBinding.salerNameTv.setText(data.getName());

    if (adapter.isSelected(position)) {
      dataBinding.salerHeaderImg.setBackgroundResource(R.drawable.ai_annulus_green);
      dataBinding.chooseImg.setVisibility(View.VISIBLE);
    } else {
      dataBinding.salerHeaderImg.setBackgroundResource(R.drawable.circle_outside);
      dataBinding.chooseImg.setVisibility(View.GONE);
    }
  }
}
