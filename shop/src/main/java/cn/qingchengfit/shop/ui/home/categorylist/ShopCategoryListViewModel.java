package cn.qingchengfit.shop.ui.home.categorylist;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.category.CategoryListItem;
import cn.qingchengfit.shop.vo.Category;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopCategoryListViewModel
    extends FlexibleViewModel<List<Category>, CategoryListItem, String> {

  public final ObservableField<List<CategoryListItem>> items = new ObservableField<>();
  private final ActionLiveEvent addEvent = new ActionLiveEvent();
  private final ActionLiveEvent deleteEvent = new ActionLiveEvent();

  public ActionLiveEvent getAddEvent() {
    return addEvent;
  }

  public ActionLiveEvent getDeleteEvent() {
    return deleteEvent;
  }

  public ActionLiveEvent getUpdateEvent() {
    return updateEvent;
  }

  private final ActionLiveEvent updateEvent = new ActionLiveEvent();

  @Inject public ShopCategoryListViewModel() {
  }

  public void onAddButtonClick() {
    addEvent.call();
  }

  public void onDeleteButtonClick() {
    deleteEvent.call();
  }

  public void onUpdatebuttonClick() {
    updateEvent.call();
  }

  @NonNull @Override protected LiveData<List<Category>> getSource(@NonNull String s) {
    return null;
  }

  @Override protected boolean isSourceValid(@Nullable List<Category> categories) {
    return categories != null && !categories.isEmpty();
  }

  @Override protected List<CategoryListItem> map(@NonNull List<Category> categories) {
    return FlexibleItemProvider.with(
        new CommonItemFactory<Category, CategoryListItem>(CategoryListItem.class)).from(categories);
  }
}
