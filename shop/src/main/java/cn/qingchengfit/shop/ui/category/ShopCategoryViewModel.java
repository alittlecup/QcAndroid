package cn.qingchengfit.shop.ui.category;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
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
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  public void setAction(Integer action) {
    this.action = action;
  }

  public ActionLiveEvent getActionEvent() {
    return actionEvent;
  }

  private final ActionLiveEvent actionEvent = new ActionLiveEvent();
  private final MutableLiveData<Boolean> addResult = new MutableLiveData<>();
  private final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();
  private final MutableLiveData<Boolean> putResult = new MutableLiveData<>();

  private Integer action;

  @Inject public ShopCategoryViewModel() {
  }

  public void addShopCategory(Category category) {
    // TODO: 2017/12/19 添加分类
    addResult.setValue(repository.qcPostCategory(loginStatus.staff_id(), category).getValue());
  }

  public void updateShopCategory(Category category) {
    // TODO: 2017/12/19 更新分类
    putResult.setValue(
        repository.qcPutCategory(loginStatus.staff_id(), category.getId(), category).getValue());
  }

  public void deleteShopCategory(int id) {
    // TODO: 2017/12/19 删除分类
    deleteResult.setValue(repository.qcDeleteCategory(loginStatus.staff_id(), id).getValue());
  }

  private void onResult() {
    actionEvent.call();
  }
}
