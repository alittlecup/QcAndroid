package cn.qingchengfit.shop.ui.home.productlist;

import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.base.ShopBaseViewModel;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductsViewModel extends ShopBaseViewModel {
  public ActionLiveEvent getProductEvent() {
    return productEvent;
  }

  private final ActionLiveEvent productEvent=new ActionLiveEvent();
  @Inject
  public ShopProductsViewModel(){}

  public void onAddProductCall(){
    productEvent.call();
  }
}
