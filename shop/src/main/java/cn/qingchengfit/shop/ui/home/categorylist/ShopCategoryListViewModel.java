package cn.qingchengfit.shop.ui.home.categorylist;

import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.base.ShopBaseViewModel;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopCategoryListViewModel extends ShopBaseViewModel {
  private final ActionLiveEvent addEvent=new ActionLiveEvent();
  private final ActionLiveEvent deleteEvent=new ActionLiveEvent();

  public ActionLiveEvent getAddEvent() {
    return addEvent;
  }

  public ActionLiveEvent getDeleteEvent() {
    return deleteEvent;
  }

  public ActionLiveEvent getUpdateEvent() {
    return updateEvent;
  }

  private final ActionLiveEvent updateEvent=new ActionLiveEvent();
  @Inject
  public ShopCategoryListViewModel(){}

  public void onAddButtonClick(){
      addEvent.call();
  }
  public void onDeleteButtonClick(){
    deleteEvent.call();
  }
  public void onUpdatebuttonClick(){
    updateEvent.call();
  }
}
