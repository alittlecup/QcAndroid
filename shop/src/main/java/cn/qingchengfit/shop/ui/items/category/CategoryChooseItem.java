package cn.qingchengfit.shop.ui.items.category;

import android.view.View;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ItemChooseCategoryBinding;
import cn.qingchengfit.shop.ui.items.DataBindingViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/27.
 */

public class CategoryChooseItem
    extends AbstractFlexibleItem<DataBindingViewHolder<ItemChooseCategoryBinding>> {
  public ICategotyItemData data;

  public CategoryChooseItem(ICategotyItemData data) {
    this.data = data;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_choose_category;
  }

  @Override public DataBindingViewHolder<ItemChooseCategoryBinding> createViewHolder(View view,
      FlexibleAdapter adapter) {
    return new DataBindingViewHolder<>(view, adapter);
  }

  public ICategotyItemData getData() {
    return data;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      DataBindingViewHolder<ItemChooseCategoryBinding> holder, int position, List payloads) {
    ItemChooseCategoryBinding dataBinding = holder.getDataBinding();
    if (adapter.isSelected(position)) {
      dataBinding.checkCategory.setChecked(true);
    } else {
      dataBinding.checkCategory.setChecked(false);
    }
    dataBinding.checkCategory.setText(data.getCategoryName());
  }
}
