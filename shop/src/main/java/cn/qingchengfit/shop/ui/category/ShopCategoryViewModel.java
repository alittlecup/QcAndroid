package cn.qingchengfit.shop.ui.category;

import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.saasbase.common.mvvm.BaseViewModel;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.vo.Category;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopCategoryViewModel extends BaseViewModel {
  @Inject ShopRepository repository;

  public void setAction(Integer action) {
    this.action = action;
  }

  public ActionLiveEvent getActionEvent() {
    return actionEvent;
  }

  private final ActionLiveEvent actionEvent=new ActionLiveEvent();

  private Integer action;
  @Inject
  public ShopCategoryViewModel(){}

  public void addShopCategory(Category category){
    // TODO: 2017/12/19 添加分类
  }
  public void updateShopCategory(Category category){
    // TODO: 2017/12/19 更新分类 
  }
  public void deleteShopCategory(String id){
    // TODO: 2017/12/19 删除分类
  }

  private void onResult(){
    actionEvent.call();
  }
}
