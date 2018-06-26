package cn.qingchengfit.shop.ui.items.category;

import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemCategotyListBinding;
import cn.qingchengfit.shop.listener.CategotyItemClickListener;
import cn.qingchengfit.saasbase.common.flexble.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/15.
 */

public class CategoryListItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemCategotyListBinding>> {
  private ICategotyItemData data;
  private CategotyItemClickListener listener;

  public CategoryListItem(ICategotyItemData data, CategotyItemClickListener listener) {
    this.data = data;
    this.listener = listener;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_categoty_list;
  }

  @Override public DataBindingViewHolder<ItemCategotyListBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    DataBindingViewHolder<ItemCategotyListBinding> viewHolder =
        new DataBindingViewHolder<>(view, adapter);

    return viewHolder;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemCategotyListBinding> holder, int position, List payloads) {
    ItemCategotyListBinding dataBinding = holder.getDataBinding();
    dataBinding.categoryName.setText(data.getCategoryName());
    if (data.getCategoryPriority() != 0) {
      dataBinding.categoryWeight.setText("权重" + String.valueOf(data.getCategoryPriority()));
    } else {
      dataBinding.categoryWeight.setText("");
    }
    dataBinding.categoryProductCount.setText(dataBinding.getRoot()
        .getContext()
        .getString(R.string.category_products, data.getCategoryProductCount()));
    dataBinding.categoryDelete.setOnClickListener(v -> listener.onDeleteClick(data));
    dataBinding.categoryUpdate.setOnClickListener(v -> listener.onPutClick(data));
  }
}
