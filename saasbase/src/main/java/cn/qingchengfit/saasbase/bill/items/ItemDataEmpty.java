package cn.qingchengfit.saasbase.bill.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/10/24.
 */

public class ItemDataEmpty extends AbstractFlexibleItem<ItemDataEmpty.ItemEmptyVH> {

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public ItemEmptyVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new ItemEmptyVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemEmptyVH holder, int position,
      List payloads) {
  }

  @Override public int getLayoutRes() {
    return R.layout.item_bill_empty;
  }

  class ItemEmptyVH extends FlexibleViewHolder{

    public ItemEmptyVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
    }

  }
}
