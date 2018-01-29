package cn.qingchengfit.shop.ui.product.bottom;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.base.ShopBaseViewModel;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.items.category.CategoryChooseItem;
import cn.qingchengfit.shop.vo.Category;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/27.
 */

public class ShopBottomCategoryViewModel extends ShopBaseViewModel {

  public final ObservableField<List<CategoryChooseItem>> items = new ObservableField<>();
  public final ActionLiveEvent cancelEvent = new ActionLiveEvent();
  public final ActionLiveEvent confimEvent = new ActionLiveEvent();
  public final ActionLiveEvent addCategoryEvent = new ActionLiveEvent();

  public void onAddCategoryClick() {
    addCategoryEvent.call();
  }

  public void onCancelClick() {
    cancelEvent.call();
  }

  public void onConfimClick() {
    confimEvent.call();
  }

  public LiveData<List<Category>> loadCategoryList(ShopRepository repository, String staff_id,
      HashMap<String, Object> params) {
    return repository.qcLoadCategories(staff_id, params);
  }
}
