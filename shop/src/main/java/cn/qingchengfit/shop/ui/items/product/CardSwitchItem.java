package cn.qingchengfit.shop.ui.items.product;

import android.view.View;
import android.widget.CompoundButton;
import cn.qingchengfit.saasbase.cards.bean.ICardShopChooseItemData;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemCardSwitchBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/31.
 */

public class CardSwitchItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemCardSwitchBinding>> {
  private ICardShopChooseItemData data;

  public CardSwitchItem(ICardShopChooseItemData data) {
    this.data = data;
  }

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

  public ICardShopChooseItemData getData() {
    return data;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemCardSwitchBinding> holder, int position, List payloads) {
    ItemCardSwitchBinding dataBinding = holder.getDataBinding();
    dataBinding.cardTpl.setLabel(data.getShopCardTplName());
    if (adapter.isSelected(position)) {
      dataBinding.cardTpl.setOpen(true);
    } else {
      dataBinding.cardTpl.setOpen(false);
    }
    dataBinding.cardTpl.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          adapter.addSelection(position);
        } else {
          adapter.removeSelection(position);
        }

      }
    });
  }
}
