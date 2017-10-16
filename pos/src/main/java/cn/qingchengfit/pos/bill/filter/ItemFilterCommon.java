package cn.qingchengfit.pos.bill.filter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.widgets.QcAutoLineRadioGroup;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/10/12.
 */

public class ItemFilterCommon extends AbstractFlexibleItem<ItemFilterCommon.ItemFilterVH> {

  @Override public ItemFilterVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return super.createViewHolder(adapter, inflater, parent);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemFilterVH holder, int position,
      List payloads) {
    super.bindViewHolder(adapter, holder, position, payloads);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_filter_common;
  }

  class ItemFilterVH extends FlexibleViewHolder {

    @BindView(R.id.bill_filter_common) QcAutoLineRadioGroup billFilterCommon;

    public ItemFilterVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}
