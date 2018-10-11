package cn.qingchengfit.shop.ui.category;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.shop.vo.CategoryWrapper;
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

  public MediatorLiveData<Category> getAddResult() {
    return addResult;
  }

  public MediatorLiveData<Boolean> getDeleteResult() {
    return deleteResult;
  }

  public MediatorLiveData<Boolean> getPutResult() {
    return putResult;
  }

  public final MutableLiveData<String> theSameResponseResult = new MutableLiveData<>();

  private final MediatorLiveData<Category> addResult = new MediatorLiveData<>();
  private final MediatorLiveData<Boolean> deleteResult = new MediatorLiveData<>();
  private final MediatorLiveData<Boolean> putResult = new MediatorLiveData<>();

  private Integer action;

  @Inject public ShopCategoryViewModel() {
  }

  public void addShopCategory(Category category) {
    category.setId(null);
    LiveData<QcDataResponse> booleanLiveData =
        repository.qcPostCategory(loginStatus.staff_id(), category, gymWrapper.getParams());
    addResult.addSource(booleanLiveData, response -> {
      if (response.getData() != null
          && response.getStatus() == 200
          && response.getData() instanceof CategoryWrapper) {
        addResult.setValue(((CategoryWrapper) response.getData()).category);
      } else if (response.getStatus() == 500) {
        theSameResponseResult.setValue(response.getMsg());
      }
      addResult.removeSource(booleanLiveData);
    });
  }

  public void updateShopCategory(Category category) {
    LiveData<Boolean> booleanLiveData =
        repository.qcPutCategory(loginStatus.staff_id(), category.getId(), category,
            gymWrapper.getParams());
    putResult.addSource(booleanLiveData, aBoolean -> {
      putResult.setValue(aBoolean);
      putResult.removeSource(booleanLiveData);
    });
  }

  public void deleteShopCategory(String id) {
    LiveData<Boolean> booleanLiveData =
        repository.qcDeleteCategory(loginStatus.staff_id(), id, gymWrapper.getParams());
    deleteResult.addSource(booleanLiveData, aBoolean -> {
      deleteResult.setValue(aBoolean);
      deleteResult.removeSource(booleanLiveData);
    });
  }

  private void onResult() {
    actionEvent.call();
  }
}
