package cn.qingchengfit.shop.ui.product.paycardchannel;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saasbase.cards.bean.ICardShopChooseItemData;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.product.CardSwitchItem;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/20.
 */

public class ProductPayViewModel
    extends FlexibleViewModel<List<ICardShopChooseItemData>, CardSwitchItem, String> {
  public final ObservableField<List<CardSwitchItem>> items = new ObservableField<>();
  @Inject ShopRepository repository;

  @Inject public ProductPayViewModel() {
  }

  @NonNull @Override
  protected LiveData<List<ICardShopChooseItemData>> getSource(@NonNull String s) {
    return repository.qcLoadCardTpls(null, s);
  }

  @Override
  protected boolean isSourceValid(@Nullable List<ICardShopChooseItemData> iCardShopChooseItemData) {
    return iCardShopChooseItemData != null && !iCardShopChooseItemData.isEmpty();
  }

  @Override protected List<CardSwitchItem> map(
      @NonNull List<ICardShopChooseItemData> iCardShopChooseItemData) {
    return FlexibleItemProvider.with(
        new CommonItemFactory<ICardShopChooseItemData, CardSwitchItem>(CardSwitchItem.class))
        .from(iCardShopChooseItemData);
  }
}
