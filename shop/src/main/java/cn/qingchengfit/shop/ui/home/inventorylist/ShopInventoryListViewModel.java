package cn.qingchengfit.shop.ui.home.inventorylist;

import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.base.ShopBaseViewModel;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopInventoryListViewModel extends ShopBaseViewModel {
  public ActionLiveEvent getShowAllRecord() {
    return showAllRecord;
  }

  private final ActionLiveEvent showAllRecord = new ActionLiveEvent();

  @Inject public ShopInventoryListViewModel() {
  }

  public void onAllInventoryListClick() {
    showAllRecord.call();
  }
}
