package cn.qingchengfit.shop.ui.items;

import android.support.annotation.NonNull;
import cn.qingchengfit.saasbase.common.flexble.FlexibleFactory;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2018/1/15.
 */

public class CommonItemFactory<Data, Item extends AbstractFlexibleItem>
    implements FlexibleItemProvider.Factory<Data, Item> {
  private Class<Item> itemClass;

  public CommonItemFactory(Class<Item> itemClass) {
    this.itemClass = itemClass;
  }

  @NonNull @Override public Item create(Data data) {
    return FlexibleFactory.create(itemClass, data);
  }
}
