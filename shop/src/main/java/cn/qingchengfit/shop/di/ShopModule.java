package cn.qingchengfit.shop.di;

import dagger.Module;
import dagger.Subcomponent;
import javax.inject.Singleton;

/**
 * Created by huangbaole on 2017/12/19.
 */
@Module(subcomponents = { ShopModule.ShopModelComponent.class }) public abstract class ShopModule {
  @Singleton @Subcomponent(modules = { BindShopActivity.class, ShopViewModel.class })
  interface ShopModelComponent {
    @Subcomponent.Builder interface Builder {
      ShopModelComponent build();
    }
  }
}
