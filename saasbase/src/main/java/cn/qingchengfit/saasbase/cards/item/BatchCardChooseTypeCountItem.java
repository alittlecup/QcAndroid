package cn.qingchengfit.saasbase.cards.item;

import android.content.Context;
import android.view.View;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ItemBatchCardChooseTypeHeadBinding;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.saascommon.flexble.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.IHeader;
import java.util.List;

public class BatchCardChooseTypeCountItem
    extends AbstractHeaderItem<DataBindingViewHolder<ItemBatchCardChooseTypeHeadBinding>> {
  public int getType() {
    return type;
  }

  private int type;//卡种类的区别
  private int totalCount;
  private int openCount;

  public BatchCardChooseTypeCountItem(int type, int totalCount, int openCount) {
    this.type = type;
    this.totalCount = totalCount;
    this.openCount = openCount;
  }

  public void setOpenCount(int openCount) {
    if (openCount <= totalCount) {
      this.openCount = openCount;
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_batch_card_choose_type_head;
  }

  @Override
  public DataBindingViewHolder<ItemBatchCardChooseTypeHeadBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemBatchCardChooseTypeHeadBinding> holder, int position,
      List payloads) {
    ItemBatchCardChooseTypeHeadBinding dataBinding = holder.getDataBinding();
    Context context = holder.itemView.getContext();
    dataBinding.txTypeCount.setText(
        String.format(context.getString(R.string.card_simple_count_total),
            CardBusinessUtils.getCardTypeCategorySimple(type, context), openCount, totalCount));
  }
}
