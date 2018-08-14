package cn.qingchengfit.shop.ui.product.paycardchannel;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.product.CardSwitchItem;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/20.
 */

public class ProductPayViewModel
     extends BaseViewModel {
  public final ObservableField<List<CardSwitchItem>> items = new ObservableField<>();
  @Inject ShopRepository repository;

  @Inject public ProductPayViewModel() {
  }


}
