package cn.qingchengfit.shop.ui.home.productlist;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.product.ProductListItem;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.shop.vo.ShopOrderBy;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductsViewModel
    extends FlexibleViewModel<List<Product>, ProductListItem, HashMap<String, Object>> {
  public ActionLiveEvent getProductEvent() {
    return productEvent;
  }
  public final ObservableBoolean isLoading=new ObservableBoolean(false);
  private final ActionLiveEvent productEvent = new ActionLiveEvent();
  public final ObservableField<List<ProductListItem>> items = new ObservableField<>();

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;

  public void setStatus(@IntRange(from = 0, to = 1) int status) {
    params.put("status", status);
  }

  public HashMap<String, Object> getParams() {
    return params;
  }

  private HashMap<String, Object> params = new HashMap<>();

  @Inject public ShopProductsViewModel() {
    params.put("status", 0);
    params.put("order_by", ShopOrderBy.PRRORITY_UP);
    params.put("show_all",1);
  }

  public void onAddProductCall() {
    productEvent.call();
  }

  public void onWeightClick(boolean isArrowUp) {
    params.put("order_by", isArrowUp ? ShopOrderBy.PRRORITY_UP : ShopOrderBy.PRRORITY_DOWN);
    loadSource(params);
  }

  public void onSalesClick(boolean isArrowUp) {
    params.put("order_by", isArrowUp ? ShopOrderBy.TOTAL_SALES_UP : ShopOrderBy.TOTAL_SALES_DOWN);
    loadSource(params);
  }

  public void onInventoryClick(boolean isArrowUp) {
    params.put("order_by", isArrowUp ? ShopOrderBy.INVENTPORY_UP : ShopOrderBy.INVENTPORY_DOWN);
    loadSource(params);
  }

  public void onAdddDateClick(boolean isArrowUp) {
    params.put("order_by", isArrowUp ? ShopOrderBy.CREATED_AT_UP : ShopOrderBy.CREATED_AT_DOWN);
    loadSource(params);
  }

  @Override public void loadSource(@NonNull HashMap<String, Object> map) {
    identifier.setValue(map);
  }
  public void refresh(){
    isLoading.set(true);
    identifier.setValue(params);
  }

  @NonNull @Override
  protected LiveData<List<Product>> getSource(@NonNull HashMap<String, Object> map) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.putAll(map);
    return repository.qcLoadProductList(loginStatus.staff_id(), params);
  }

  @Override protected boolean isSourceValid(@Nullable List<Product> product) {
    return product != null;
  }

  @Override protected List<ProductListItem> map(@NonNull List<Product> products) {
    return FlexibleItemProvider.with(
        new CommonItemFactory<Product, ProductListItem>(ProductListItem.class)).from(products);
  }
}
