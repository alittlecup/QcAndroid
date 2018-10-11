package cn.qingchengfit.shop.di;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.repository.ShopRepositoryImpl;
import cn.qingchengfit.shop.repository.remote.ShopRemoteRepository;
import cn.qingchengfit.shop.repository.remote.ShopRemoteRepositoryImpl;
import cn.qingchengfit.shop.routers.ShopRouterCenter;
import cn.qingchengfit.shop.routers.shopImpl;
import cn.qingchengfit.shop.ui.category.ShopCategoryViewModel;
import cn.qingchengfit.shop.ui.home.ShopHomeViewModel;
import cn.qingchengfit.shop.ui.home.categorylist.ShopCategoryListViewModel;
import cn.qingchengfit.shop.ui.home.inventorylist.ShopInventoryListViewModel;
import cn.qingchengfit.shop.ui.home.productlist.ShopProductsViewModel;
import cn.qingchengfit.shop.ui.inventory.ShopInventoryViewModel;
import cn.qingchengfit.shop.ui.inventory.product.ProductInventoryViewModel;
import cn.qingchengfit.shop.ui.inventory.product.UpdateInventoryViewModel;
import cn.qingchengfit.shop.ui.product.ShopProductViewModel;
import cn.qingchengfit.shop.ui.product.addsuccess.ProductAddSuccessViewModel;
import cn.qingchengfit.shop.ui.product.deliverchannel.ProductDeliverViewModel;
import cn.qingchengfit.shop.ui.product.paycardchannel.ProductPayViewModel;
import cn.qingchengfit.shop.ui.product.prices.ProductPricesViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Module public abstract class ShopViewModel {
  //@Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
  private static ShopRouterCenter shopRouterCenter = new ShopRouterCenter().registe(new shopImpl());

  @Provides public static ShopRouterCenter provideShopRouterCenter() {
    return shopRouterCenter;
  }

  @Binds
  abstract ShopRemoteRepository bindShopRemoteService(ShopRemoteRepositoryImpl remoteRepository);

  @Binds abstract ShopRepository bindShopRepository(ShopRepositoryImpl repository);

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

  @Binds @IntoMap @ViewModelKey(ProductInventoryViewModel.class)
  abstract ViewModel bindProductInventoryViewModel(ProductInventoryViewModel shopHomeViewModel);

  @Binds @IntoMap @ViewModelKey(UpdateInventoryViewModel.class)
  abstract ViewModel bindUpdateInventoryViewModel(UpdateInventoryViewModel shopHomeViewModel);

  @Binds @IntoMap @ViewModelKey(ProductAddSuccessViewModel.class)
  abstract ViewModel bindProductAddSuccessViewModel(ProductAddSuccessViewModel shopHomeViewModel);

  @Binds @IntoMap @ViewModelKey(ProductPricesViewModel.class)
  abstract ViewModel bindProductPricesViewModel(ProductPricesViewModel shopHomeViewModel);

  @Binds @IntoMap @ViewModelKey(ProductDeliverViewModel.class)
  abstract ViewModel bindProductDeliverViewModel(ProductDeliverViewModel shopHomeViewModel);

  @Binds @IntoMap @ViewModelKey(ProductPayViewModel.class)
  abstract ViewModel bindProductPayViewModel(ProductPayViewModel shopHomeViewModel);
}

