package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import cn.qingchengfit.saasbase.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AddCardtplStantardItem
    extends AbstractFlexibleItem<AddCardtplStantardItem.AddCardtplStantardVH> {

  @Override public int getLayoutRes() {
    return R.layout.item_cardtpl_standard_add;
  }

  @Override
  public AddCardtplStantardVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new AddCardtplStantardVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, AddCardtplStantardVH holder, int position,
      List payloads) {
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class AddCardtplStantardVH extends FlexibleViewHolder {

    public AddCardtplStantardVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);

    }
  }
}