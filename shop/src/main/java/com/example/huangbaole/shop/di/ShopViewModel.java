package com.example.huangbaole.shop.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import cn.qingchengfit.saasbase.common.di.scope.ViewModelKey;
import cn.qingchengfit.saasbase.common.mvvm.ViewModelFactory;
import com.example.huangbaole.shop.ui.category.ShopCategoryViewModel;
import com.example.huangbaole.shop.ui.home.ShopHomeViewModel;
import com.example.huangbaole.shop.ui.home.categorylist.ShopCategoryListViewModel;
import com.example.huangbaole.shop.ui.home.inventorylist.ShopInventoryListViewModel;
import com.example.huangbaole.shop.ui.home.productlist.ShopProductsViewModel;
import com.example.huangbaole.shop.ui.inventory.ShopInventoryViewModel;
import com.example.huangbaole.shop.ui.product.ShopProductViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Module public abstract class ShopViewModel {
  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

  @Binds @IntoMap @ViewModelKey(ShopHomeViewModel.class)
  abstract ViewModel bindShopHomeViewModel(ShopHomeViewModel shopHomeViewModel);

  @Binds @IntoMap @ViewModelKey(ShopProductsViewModel.class)
  abstract ViewModel bindShopProductsViewModel(ShopProductsViewModel shopHomeViewModel);

  @Binds @IntoMap @ViewModelKey(ShopCategoryListViewModel.class)
  abstract ViewModel bindShopCategoryListViewModel(ShopCategoryListViewModel shopHomeViewModel);

  @Binds @IntoMap @ViewModelKey(ShopInventoryListViewModel.class)
  abstract ViewModel bindShopInventoryListViewModel(ShopInventoryListViewModel shopHomeViewModel);

  @Binds @IntoMap @ViewModelKey(ShopCategoryViewModel.class)
  abstract ViewModel bindShopCategoryViewModel(ShopCategoryViewModel shopHomeViewModel);


  @Binds @IntoMap @ViewModelKey(ShopProductViewModel.class)
  abstract ViewModel bindShopProductViewModel(ShopProductViewModel shopHomeViewModel);


  @Binds @IntoMap @ViewModelKey(ShopInventoryViewModel.class)
  abstract ViewModel bindShopInventoryViewModel(ShopInventoryViewModel shopHomeViewModel);
}
