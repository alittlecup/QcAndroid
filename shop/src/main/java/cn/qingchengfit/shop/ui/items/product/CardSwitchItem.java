package cn.qingchengfit.shop.ui.items.product;

import android.view.View;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemCardSwitchBinding;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/31.
 */

public class CardSwitchItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemCardSwitchBinding>> {
  //private ICardShopChooseItemData data;

  //public CardSwitchItem(ICardShopChooseItemData data) {
  //  this.data = data;
  //}


  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_card_switch;
  }

  @Override public DataBindingViewHolder<ItemCardSwitchBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }





  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemCardSwitchBinding> holder, int position, List payloads) {
    ItemCardSwitchBinding dataBinding = holder.getDataBinding();
    //dataBinding.cardTpl.setLabel(data.getShopCardTplName());
    dataBinding.cardTpl.setClickable(false);
    if (adapter.isSelected(position)) {
      dataBinding.cardTpl.setOpen(true);
    } else {
      dataBinding.cardTpl.setOpen(false);
    }
  }
}
