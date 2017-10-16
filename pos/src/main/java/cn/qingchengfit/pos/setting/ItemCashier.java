package cn.qingchengfit.pos.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.pos.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/10/13.
 */

public class ItemCashier extends AbstractFlexibleItem<ItemCashier.ItemCashierVH> {

  @Override public ItemCashierVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new ItemCashierVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, ItemCashierVH holder, int position,
      List payloads) {

  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_cashier;
  }

  class ItemCashierVH extends FlexibleViewHolder {

    @BindView(R.id.img_cashier_head) ImageView imgCashierHead;
    @BindView(R.id.tv_cashier_name) TextView tvCashierName;
    @BindView(R.id.tv_cashier_phone) TextView tvCashierPhone;

    public ItemCashierVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}
