package cn.qingchengfit.shop.ui.items.product;

import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemCategoryBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/29.
 */

public class GoodProductItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemCategoryBinding>> {
  public static final int SHOW_CARD_PRICE = 2;
  private boolean isExpend = false;
  private Good good;

  public GoodProductItem(Good good) {
    this.good = good;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_category;
  }

  @Override public DataBindingViewHolder<ItemCategoryBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    DataBindingViewHolder<ItemCategoryBinding> viewHolder =
        new DataBindingViewHolder<>(view, adapter);
    return viewHolder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemCategoryBinding> holder, int position, List payloads) {
    ItemCategoryBinding dataBinding = holder.getDataBinding();

    dataBinding.imDelete.setVisibility(View.VISIBLE);
    dataBinding.categoryName.setVisibility(View.VISIBLE);

    if (adapter.getItemCount() == 1) {
      dataBinding.imDelete.setVisibility(View.GONE);
      dataBinding.categoryName.setVisibility(View.GONE);
    }

    if (adapter instanceof CommonFlexAdapter) {
      int status = ((CommonFlexAdapter) adapter).getStatus();
      if (status == SHOW_CARD_PRICE) {
        dataBinding.categoryPriceCard.setVisibility(View.VISIBLE);
      } else {
        dataBinding.categoryPriceCard.setVisibility(View.GONE);
      }
    }
    if (isExpend) {
      dataBinding.imDelete.setVisibility(View.VISIBLE);
      dataBinding.categoryName.setVisibility(View.VISIBLE);
    }

    dataBinding.setGood(good);
    dataBinding.imDelete.setOnClickListener(v -> {
      if (adapter.getItemCount() == 1) {
        dataBinding.imDelete.setVisibility(View.GONE);
        dataBinding.categoryName.setVisibility(View.GONE);
        ((GoodProductItem) adapter.getItem(0)).setExpend(false);
      } else {
        adapter.removeItem(position);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() == 2) {
          ((GoodProductItem) adapter.getItem(0)).setExpend(true);
        }
      }
      ViewUtil.resetRecyclerViewHeight(adapter.getRecyclerView());
    });
  }

  public void setExpend(boolean isExpend) {
    this.isExpend = isExpend;
  }

  public boolean isExpend() {
    return isExpend;
  }
}
