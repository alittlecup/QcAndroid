package cn.qingchengfit.saasbase.cards.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
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
  public AddCardtplStantardVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new AddCardtplStantardVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
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
      ButterKnife.bind(this, view);
    }
  }
}