package cn.qingchengfit.shop.ui.product.addsuccess;

import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.base.ShopBaseViewModel;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/20.
 */

public class ProductAddSuccessViewModel extends ShopBaseViewModel {
  public final ActionLiveEvent onBackHomeClick=new ActionLiveEvent();
  public final ActionLiveEvent onAddMoreClick=new ActionLiveEvent();
  @Inject
  public ProductAddSuccessViewModel(){

  }

  public void onBackHome(){
    onBackHomeClick.call();
  }
  public void onAddMore(){
    onAddMoreClick.call();
  }
}
