package cn.qingchengfit.shop.ui.home.productlist;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.product.ProductListItem;
import cn.qingchengfit.shop.vo.Product;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductsViewModel
    extends FlexibleViewModel<List<Product>, ProductListItem, String> {
  public ActionLiveEvent getProductEvent() {
    return productEvent;
  }

  private final ActionLiveEvent productEvent = new ActionLiveEvent();
  public final ObservableField<List<ProductListItem>> items = new ObservableField<>();

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;

  @Inject public ShopProductsViewModel() {
  }

  public void onAddProductCall() {
    productEvent.call();
  }

  public void onWeightClick(boolean isArrowUp) {
    Log.d("TAG", "onWeightClick: " + isArrowUp);
  }

  public void onSalesClick(boolean isArrowUp) {
    Log.d("TAG", "onWeightClick: ");
  }

  public void onInventoryClick(boolean isArrowUp) {
    Log.d("TAG", "onWeightClick: ");
  }

  public void onAdddDateClick(boolean isArrowUp) {
    Log.d("TAG", "onWeightClick: ");
  }

  @NonNull @Override protected LiveData<List<Product>> getSource(@NonNull String s) {

    return null;
  }

  @Override protected boolean isSourceValid(@Nullable List<Product> product) {
    return product != null && !product.isEmpty();
  }

  @Override protected List<ProductListItem> map(@NonNull List<Product> products) {
    return FlexibleItemProvider.with(new CommonItemFactory<Product, ProductListItem>(ProductListItem.class))
        .from(products);
  }
}
