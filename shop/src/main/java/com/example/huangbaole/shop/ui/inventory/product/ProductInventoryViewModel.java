package com.example.huangbaole.shop.ui.inventory.product;

import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import com.example.huangbaole.shop.base.ShopBaseViewModel;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ProductInventoryViewModel extends ShopBaseViewModel {

  private final ActionLiveEvent addInventoryEvent = new ActionLiveEvent();

  public ActionLiveEvent getAddInventoryEvent() {
    return addInventoryEvent;
  }

  public ActionLiveEvent getReduceInventoryEvent() {
    return reduceInventoryEvent;
  }

  private final ActionLiveEvent reduceInventoryEvent = new ActionLiveEvent();

  @Inject public ProductInventoryViewModel() {
  }

  public void onAddInventoryClick() {
    addInventoryEvent.call();
  }

  public void onReduceInventoryClick() {
    reduceInventoryEvent.call();
  }
}
