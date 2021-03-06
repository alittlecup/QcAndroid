package cn.qingchengfit.shop.ui.home.categorylist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.listener.CategotyItemClickListener;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.items.category.CategoryListItem;
import cn.qingchengfit.shop.ui.items.category.ICategotyItemData;
import cn.qingchengfit.shop.vo.Category;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopCategoryListViewModel
    extends FlexibleViewModel<List<Category>, CategoryListItem, Map<String, Object>>
    implements CategotyItemClickListener {

  public final ObservableField<List<AbstractFlexibleItem>> items = new ObservableField<>();
  private final ActionLiveEvent addEvent = new ActionLiveEvent();
  private final MutableLiveData<Category> deleteEvent = new MutableLiveData<>();

  public ActionLiveEvent getAddEvent() {
    return addEvent;
  }

  public MutableLiveData<Category> getDeleteEvent() {
    return deleteEvent;
  }

  public MutableLiveData<Category> getUpdateEvent() {
    return updateEvent;
  }

  private final MutableLiveData<Category> updateEvent = new MutableLiveData<>();
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;

  @Inject public ShopCategoryListViewModel() {
  }

  public void onAddButtonClick() {
    addEvent.call();
  }

  public void onDeleteButtonClick(Category category) {
    deleteEvent.setValue(category);
  }

  public void onUpdatebuttonClick(Category category) {
    updateEvent.setValue(category);
  }

  @NonNull @Override
  protected LiveData<List<Category>> getSource(@NonNull Map<String, Object> map) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.putAll(map);
    params.put("show_all", 1);
    return repository.qcLoadCategories(loginStatus.staff_id(), params);
  }

  @Override protected boolean isSourceValid(@Nullable List<Category> categories) {
    return categories != null;
  }

  @Override protected List<CategoryListItem> map(@NonNull List<Category> categories) {
    return FlexibleItemProvider.with(new CategoryItemFactory(this)).from(categories);
  }

  class CategoryItemFactory implements FlexibleItemProvider.Factory<Category, CategoryListItem> {
    private CategotyItemClickListener listener;

    public CategoryItemFactory(CategotyItemClickListener listener) {
      this.listener = listener;
    }

    @NonNull @Override public CategoryListItem create(Category category) {
      return new CategoryListItem(category, listener);
    }
  }

  @Override public void onDeleteClick(ICategotyItemData category) {
    if (category instanceof Category) {
      onDeleteButtonClick((Category) category);
    }
  }

  @Override public void onPutClick(ICategotyItemData category) {
    if (category instanceof Category) {
      onUpdatebuttonClick((Category) category);
    }
  }

  @Override public void loadSource(@NonNull Map<String, Object> s) {
    identifier.setValue(s);
  }
}
