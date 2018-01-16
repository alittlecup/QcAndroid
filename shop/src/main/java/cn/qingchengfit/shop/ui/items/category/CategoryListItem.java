package cn.qingchengfit.shop.ui.items.category;

import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemCategotyListBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/15.
 */

public class CategoryListItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemCategotyListBinding>> {
  private ICategotyItemData data;

  public CategoryListItem(ICategotyItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_categoty_list;
  }

  @Override public DataBindingViewHolder<ItemCategotyListBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemCategotyListBinding> holder, int position, List payloads) {
    ItemCategotyListBinding dataBinding = holder.getDataBinding();
    dataBinding.categoryName.setText(data.getCategoryName());
    dataBinding.categoryWeight.setText(data.getCategoryPriority());
    dataBinding.categoryProductCount.setText(dataBinding.getRoot()
        .getContext()
        .getString(R.string.category_products, data.getCategoryProductCount()));
  }
}
